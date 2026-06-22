package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
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
    
    private Mission findMission(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() ->
                        new MissionNotFoundException(
                                missionId));
    }
    
    private TelemetryResponse toResponse(
            TelemetryBySensor telemetry) {

        return new TelemetryResponse(
                telemetry.getKey().getMissionId(),
                telemetry.getSensorNumber(),
                telemetry.getReadingValue(),
                telemetry.getKey().getReadAt(),
                telemetry.getReceivedAt());
    }
    
    private TelemetryResponse toResponse(
            TelemetryByMission telemetry) {

        return new TelemetryResponse(
                telemetry.getKey().getMissionId(),
                telemetry.getSensorNumber(),
                telemetry.getReadingValue(),
                telemetry.getKey().getReadAt(),
                telemetry.getReceivedAt());
    }
    
    public TelemetryResponse create(Long missionId, Integer sensorNumber, Double value, Instant readAt) {

        Mission mission = findMission(missionId);
        
        if (mission.getStatus() == MissionStatus.PLANNED || mission.getStatus() == MissionStatus.COMPLETED || mission.getStatus() == MissionStatus.ABORTED) {
        	throw new TelemetryNotAllowedException();
        }
        
        Sensor sensor = findSensor(missionId, sensorNumber);

        Instant receivedAt = Instant.now();
        
        TelemetryBySensorKey sensorKey = new TelemetryBySensorKey(
        		missionId, sensor.getId(), readAt); 
        
        TelemetryBySensor telemetryBySensor = new TelemetryBySensor(sensorKey, sensorNumber, receivedAt, value);
        
        telemetryBySensorRepository.save(telemetryBySensor);
        
        TelemetryByMissionKey missionKey = new TelemetryByMissionKey(
        		missionId, readAt, sensor.getId()); 
        
        TelemetryByMission telemetryByMission = new TelemetryByMission(missionKey, sensorNumber, receivedAt, value);
        
        telemetryByMissionRepository.save(telemetryByMission);
        
        return new TelemetryResponse(
                missionId,
                sensorNumber,
                value,
                readAt,
                receivedAt);
    }

    public List<TelemetryResponse> listByMission(Long missionId) {

    	findMission(missionId);

        return telemetryByMissionRepository
                .findByKeyMissionId(
                        missionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TelemetryResponse> listByMissionAndSensor(Long missionId, Integer sensorNumber) {
    	Sensor sensor = findSensor(
                missionId,
                sensorNumber);

        return telemetryBySensorRepository
                .findByKeyMissionIdAndKeySensorId(
                        missionId,
                        sensor.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    public TelemetryResponse getLatestReading(Long missionId, Integer sensorNumber) {
    	Sensor sensor = findSensor(missionId, sensorNumber);
    	
    	List<TelemetryBySensor> readings =
                telemetryBySensorRepository
                        .findByKeyMissionIdAndKeySensorId(
                                missionId,
                                sensor.getId());

        return readings.stream()
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() ->
                        new TelemetryNotFoundException(
                                missionId,
                                sensorNumber));
    }
    
    public TelemetryStatsResponse getStats(Long missionId, Integer sensorNumber) {
		List<TelemetryResponse> readings = listByMissionAndSensor(missionId, sensorNumber);    	
		
    	if (readings.isEmpty()) {
    		throw new TelemetryNotFoundException(missionId, sensorNumber);
    	}
    	
    	DoubleSummaryStatistics stats = readings.stream().filter(Objects::nonNull).mapToDouble(TelemetryResponse::readingValue).summaryStatistics();
    	
    	if (stats.getCount() == 0) {
    	    throw new TelemetryNotFoundException(
    	            missionId,
    	            sensorNumber);
    	}
    	
    	return new TelemetryStatsResponse(stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage());
    }
}