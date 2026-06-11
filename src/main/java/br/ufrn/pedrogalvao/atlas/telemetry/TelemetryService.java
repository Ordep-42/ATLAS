package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new RuntimeException("Missão não encontrada:: " + missionId));
        
        if (mission.getStatus() == MissionStatus.PLANNED || mission.getStatus() == MissionStatus.COMPLETED || mission.getStatus() == MissionStatus.ABORTED) {
        	throw new RuntimeException("Não é possível receber telemetria de uma missão inativa.");
        }
        
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor não encontrado " + sensorId));

        if (!sensor.getMissionId().equals(missionId)) {
            throw new RuntimeException("Sensor não pertence a missão");
        }

        TelemetryReading reading = new TelemetryReading();

        reading.setMissionId(missionId);
        reading.setSensorId(sensorId);
        reading.setValue(value);

        return telemetryRepository.save(reading);
    }

    public List<TelemetryReading> listByMission(Long missionId) {

        missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada:: " + missionId));

        return telemetryRepository.findByMissionId(missionId);
    }

    public List<TelemetryReading> listByMissionAndSensor(Long missionId, Long sensorId) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada:: " + missionId));

        sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor não encontrado: " + sensorId));

        return telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);
    }
    
    public TelemetryReading getLatestReading(Long missionId, Long sensorId) {
    	List<TelemetryReading> readings = telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);
    	
    	if (readings.isEmpty()) {
    		throw new RuntimeException("Nenhuma leitura de telemetria encontrada");
    	}	
    	
    	return readings.stream().max((a, b) -> a.getTimestamp().compareTo(b.getTimestamp())).orElseThrow();
    }
    
    public TelemetryStatsResponse getStats(Long missionId, Long sensorId) {
		List<TelemetryReading> readings = telemetryRepository.findByMissionIdAndSensorId(missionId, sensorId);
    	
    	if (readings.isEmpty()) {
    		throw new RuntimeException("Nenhuma leitura de telemetria encontrada");
    	}
    	
    	DoubleSummaryStatistics stats = readings.stream().mapToDouble(TelemetryReading::getValue).summaryStatistics();
    	
    	return new TelemetryStatsResponse(stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage());
    }
}