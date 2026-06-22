package br.ufrn.pedrogalvao.atlas.export;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.exception.SensorNotFoundException;
import br.ufrn.pedrogalvao.atlas.mission.MissionRepository;
import br.ufrn.pedrogalvao.atlas.sensor.Sensor;
import br.ufrn.pedrogalvao.atlas.sensor.SensorRepository;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryByMission;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryByMissionRepository;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryBySensor;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryBySensorRepository;

@Service
public class ExportService {

	private final MissionRepository missionRepository;
    private final SensorRepository sensorRepository;
    
    private final TelemetryByMissionRepository telemetryByMissionRepository;
    private final TelemetryBySensorRepository telemetryBySensorRepository;

    public ExportService(
            MissionRepository missionRepository,
            SensorRepository sensorRepository,
            TelemetryByMissionRepository telemetryByMissionRepository,
            TelemetryBySensorRepository telemetryBySensorRepository) {

        this.missionRepository = missionRepository;
        this.sensorRepository = sensorRepository;
        this.telemetryByMissionRepository = telemetryByMissionRepository;
        this.telemetryBySensorRepository = telemetryBySensorRepository;
    }
    
    private Sensor findSensor(
            Long missionId,
            Integer sensorNumber) {

        missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new MissionNotFoundException(
                                missionId));

        return sensorRepository
                .findByMissionIdAndSensorNumber(
                        missionId,
                        sensorNumber)
                .orElseThrow(() ->
                        new SensorNotFoundException(
                                missionId,
                                sensorNumber));
    }
    
    public String exportMissionTelemetry(Long missionId) {
		missionRepository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
		
		List<TelemetryByMission> readings =
	            telemetryByMissionRepository
	                    .findByKeyMissionId(missionId);

	    StringBuilder csv = new StringBuilder();

	    csv.append(
	            "sensor_number,read_at,reading_value\n");

	    for (TelemetryByMission reading : readings) {

	        csv.append(reading.getSensorNumber())
	                .append(",")

	                .append(reading.getKey().getReadAt())
	                .append(",")

	                .append(reading.getReadingValue())
	                .append("\n");
	    }

	    return csv.toString();
	}
    
    public String exportSensorTelemetry(Long missionId, Integer sensorNumber) {
		Sensor sensor = findSensor(missionId, sensorNumber);
		
		List<TelemetryBySensor> readings =
	            telemetryBySensorRepository
	                    .findByKeyMissionIdAndKeySensorId(missionId, sensor.getId());

	    StringBuilder csv = new StringBuilder();

	    csv.append(
	            "reading_value,read_at,received_at\n");

	    for (TelemetryBySensor reading : readings) {

	        csv.append(reading.getReadingValue())
	                .append(",")

	                .append(reading.getKey().getReadAt())
	                .append(",")

	                .append(reading.getReceivedAt())
	                .append("\n");
	    }

	    return csv.toString();
	}
}
