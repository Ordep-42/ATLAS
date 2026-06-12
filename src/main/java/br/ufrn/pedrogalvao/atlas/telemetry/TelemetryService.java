package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.exception.SensorMissionMismatchException;
import br.ufrn.pedrogalvao.atlas.exception.SensorNotFoundException;
import br.ufrn.pedrogalvao.atlas.exception.TelemetryNotAllowedException;
import br.ufrn.pedrogalvao.atlas.exception.TelemetryNotFoundException;
import br.ufrn.pedrogalvao.atlas.mission.Mission;
import br.ufrn.pedrogalvao.atlas.mission.MissionRepository;
import br.ufrn.pedrogalvao.atlas.mission.MissionStatus;
import br.ufrn.pedrogalvao.atlas.sensor.Sensor;
import br.ufrn.pedrogalvao.atlas.sensor.SensorRepository;

@Service
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final MissionRepository missionRepository;
    private final SensorRepository sensorRepository;

    public TelemetryService(
            TelemetryRepository telemetryRepository,
            MissionRepository missionRepository,
            SensorRepository sensorRepository) {

        this.telemetryRepository = telemetryRepository;
        this.missionRepository = missionRepository;
        this.sensorRepository = sensorRepository;
    }

    public TelemetryReading create(Long missionId, Long sensorId, Double value) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));
        
        if (mission.getStatus() == MissionStatus.PLANNED || mission.getStatus() == MissionStatus.COMPLETED || mission.getStatus() == MissionStatus.ABORTED) {
        	throw new TelemetryNotAllowedException();
        }
        
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException(sensorId));

        if (!sensor.getMissionId().equals(missionId)) {
            throw new SensorMissionMismatchException(missionId, sensorId);
        }

        TelemetryReading reading = new TelemetryReading();

        reading.setMissionId(missionId);
        reading.setSensorId(sensorId);
        reading.setValue(value);

        return telemetryRepository.save(reading);
    }

    public List<TelemetryReading> listByMission(Long missionId) {

        missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        return telemetryRepository.findByMissionId(missionId);
    }

    public List<TelemetryReading> listByMissionAndSensor(Long missionId, Long sensorId) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException(sensorId));

        return telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);
    }
    
    public TelemetryReading getLatestReading(Long missionId, Long sensorId) {
    	List<TelemetryReading> readings = telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);
    	
    	if (readings.isEmpty()) {
    		throw new TelemetryNotFoundException(missionId, sensorId);
    	}	
    	
    	return readings.stream().max((a, b) -> a.getTimestamp().compareTo(b.getTimestamp())).orElseThrow();
    }
    
    public TelemetryStatsResponse getStats(Long missionId, Long sensorId) {
    	missionRepository.findById(missionId)
    	.orElseThrow(() -> new MissionNotFoundException(missionId));
    	
    	Sensor sensor = sensorRepository.findById(sensorId)
    			.orElseThrow(() -> new SensorNotFoundException(sensorId));
    	
    	if (!sensor.getMissionId().equals(missionId)) {
    		throw new SensorMissionMismatchException(missionId, sensorId);
    	}
    	
		List<TelemetryReading> readings = telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);    	
		
    	if (readings.isEmpty()) {
    		throw new TelemetryNotFoundException(missionId, sensorId);
    	}
    	
    	DoubleSummaryStatistics stats = readings.stream().mapToDouble(TelemetryReading::getValue).summaryStatistics();
    	
    	return new TelemetryStatsResponse(stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage());
    }
}