package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;

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

    private final MissionRepository missionRepository;
    private final SensorRepository sensorRepository;
    
    private final TelemetryBySensorRepository telemetryBySensorRepository;
    private final TelemetryByMissionRepository telemetryByMissionRepository;

    public TelemetryService(
            MissionRepository missionRepository,
            SensorRepository sensorRepository,
            TelemetryBySensorRepository telemetryBySensorRepository,
            TelemetryByMissionRepository telemetryByMissionRepository) {

        this.missionRepository = missionRepository;
        this.sensorRepository = sensorRepository;
		this.telemetryBySensorRepository = telemetryBySensorRepository;
		this.telemetryByMissionRepository = telemetryByMissionRepository;
    }

    private Sensor validateMissionAndSensor(Long missionId, Long sensorId) {
    	missionRepository.findById(missionId)
    	.orElseThrow(() -> new MissionNotFoundException(missionId));
    	
    	Sensor sensor = sensorRepository.findById(sensorId)
    			.orElseThrow(() -> new SensorNotFoundException(sensorId));
    	
    	if (!sensor.getMissionId().equals(missionId)) {
    		throw new SensorMissionMismatchException(missionId, sensorId);
    	}
    	
    	return sensor;
    }
    
    private TelemetryResponse toResponse(
            TelemetryBySensor telemetry) {

        return new TelemetryResponse(
                telemetry.getKey().getMissionId(),
                telemetry.getKey().getSensorId(),
                telemetry.getReadingValue(),
                telemetry.getKey().getReadAt(),
                telemetry.getReceivedAt());
    }
    
    private TelemetryResponse toResponse(
            TelemetryByMission telemetry) {

        return new TelemetryResponse(
                telemetry.getKey().getMissionId(),
                telemetry.getKey().getSensorId(),
                telemetry.getReadingValue(),
                telemetry.getKey().getReadAt(),
                telemetry.getReceivedAt());
    }
    
    public TelemetryResponse create(Long missionId, Long sensorId, Double value, Instant readAt) {

        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));
        
        if (mission.getStatus() == MissionStatus.PLANNED || mission.getStatus() == MissionStatus.COMPLETED || mission.getStatus() == MissionStatus.ABORTED) {
        	throw new TelemetryNotAllowedException();
        }
        
        validateMissionAndSensor(missionId, sensorId);

        Instant receivedAt = Instant.now();
        
        TelemetryBySensorKey sensorKey = new TelemetryBySensorKey(
        		missionId, sensorId, readAt); 
        
        TelemetryBySensor telemetryBySensor = new TelemetryBySensor(sensorKey, receivedAt, value);
        
        telemetryBySensorRepository.save(telemetryBySensor);
        
        TelemetryByMissionKey missionKey = new TelemetryByMissionKey(
        		missionId, readAt, sensorId); 
        
        TelemetryByMission telemetryByMission = new TelemetryByMission(missionKey, receivedAt, value);
        
        telemetryByMissionRepository.save(telemetryByMission);
        
        return new TelemetryResponse(
                missionId,
                sensorId,
                value,
                readAt,
                receivedAt);
    }

    public List<TelemetryResponse> listByMission(Long missionId) {

        missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        return telemetryByMissionRepository
                .findByKeyMissionId(
                        missionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TelemetryResponse> listByMissionAndSensor(Long missionId, Long sensorId) {
    	validateMissionAndSensor(
                missionId,
                sensorId);

        return telemetryBySensorRepository
                .findByKeyMissionIdAndKeySensorId(
                        missionId,
                        sensorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    public TelemetryResponse getLatestReading(Long missionId, Long sensorId) {
    	validateMissionAndSensor(missionId, sensorId);
    	
    	List<TelemetryBySensor> readings =
                telemetryBySensorRepository
                        .findByKeyMissionIdAndKeySensorId(
                                missionId,
                                sensorId);

        return readings.stream()
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() ->
                        new TelemetryNotFoundException(
                                missionId,
                                sensorId));
    }
    
    public TelemetryStatsResponse getStats(Long missionId, Long sensorId) {
		List<TelemetryResponse> readings = listByMissionAndSensor(missionId, sensorId);    	
		
    	if (readings.isEmpty()) {
    		throw new TelemetryNotFoundException(missionId, sensorId);
    	}
    	
    	DoubleSummaryStatistics stats = readings.stream().filter(Objects::nonNull).mapToDouble(TelemetryResponse::readingValue).summaryStatistics();
    	
    	if (stats.getCount() == 0) {
    	    throw new TelemetryNotFoundException(
    	            missionId,
    	            sensorId);
    	}
    	
    	return new TelemetryStatsResponse(stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage());
    }
}