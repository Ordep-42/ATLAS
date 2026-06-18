package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.pedrogalvao.atlas.exception.InvalidTransitionException;
import br.ufrn.pedrogalvao.atlas.exception.MissionDeletionNotAllowedException;
import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.sensor.Sensor;
import br.ufrn.pedrogalvao.atlas.sensor.SensorRepository;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryByMission;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryByMissionRepository;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryBySensor;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryBySensorRepository;

@Service
public class MissionService {
	
	private final MissionRepository repository;
	private final SensorRepository sensorRepository;
	private final TelemetryBySensorRepository telemetryBySensorRepository;
	private final TelemetryByMissionRepository telemetryByMissionRepository;
	
	public MissionService(MissionRepository repository, SensorRepository sensorRepository, TelemetryBySensorRepository telemetryBySensorRepository, TelemetryByMissionRepository telemetryByMissionRepository) {
		this.repository = repository;
		this.sensorRepository = sensorRepository;
		this.telemetryBySensorRepository = telemetryBySensorRepository;
		this.telemetryByMissionRepository = telemetryByMissionRepository;
	}
	
	public MissionCreatedResponse create(MissionCreateRequest request) {
		Mission mission = new Mission();
		mission.setName(request.name());
		mission.setDescription(request.description());
		repository.save(mission);
		
		return new MissionCreatedResponse(mission.getId(), mission.getName(), mission.getDescription(), mission.getStatus(), mission.getCreatedAt());
	}
	
	public List<MissionCreatedResponse> findAll() {
		List<Mission> missions = repository.findAll();
		
		List<MissionCreatedResponse> result = new ArrayList<>();
		
		for (Mission mission : missions) {
			result.add(new MissionCreatedResponse(mission.getId(), mission.getName(), mission.getDescription(), mission.getStatus(), mission.getCreatedAt()));
		}
		
		return result;
	}
	
	public MissionSummaryResponse findById(Long missionId) {
		Mission mission = repository.findById(missionId)
				.orElseThrow(() -> new MissionNotFoundException(missionId));
		
		long sensorCount = sensorRepository.countByMissionId(missionId);

		List<TelemetryByMission> readings = telemetryByMissionRepository.findByKeyMissionId(missionId);
		
	    long telemetryCount = readings.size();
	    
	    Instant lastTelemetryAt = readings.isEmpty() ? null : readings.get(0).getReceivedAt();
	    
	    return new MissionSummaryResponse(mission.getId(), mission.getName(), mission.getDescription(), mission.getStatus(), sensorCount, telemetryCount, mission.getCreatedAt(), mission.getStartedAt(), lastTelemetryAt, mission.getFinishedAt());
	}
	    
	public MissionStatusUpdateResponse updateStatus(Long id, MissionStatusUpdateRequest request) {
		Mission mission = repository.findById(id)
				.orElseThrow(() -> new MissionNotFoundException(id));
		
		MissionStatus newStatus = request.status();
		
		
		
		if (mission.getStatus() == newStatus || !isValidTransition(mission.getStatus(), newStatus))
		    throw new InvalidTransitionException(mission.getStatus(), newStatus);
		
		if (newStatus == MissionStatus.ACTIVE && mission.getStartedAt() == null) {
			mission.setStartedAt(Instant.now());
		} else if (newStatus == MissionStatus.COMPLETED || newStatus == MissionStatus.ABORTED) {
			mission.setFinishedAt(Instant.now());
		}
		
		
		mission.setStatus(newStatus);
		repository.save(mission);
		
		return new MissionStatusUpdateResponse(mission.getId(), mission.getName(), mission.getStatus(), mission.getCreatedAt(), mission.getStartedAt(), mission.getFinishedAt());
	}
	
	@Transactional
	public void delete(Long id) {
		Mission mission = repository.findById(id)
				.orElseThrow(() -> new MissionNotFoundException(id));
		
		if (mission.getStatus() == MissionStatus.ACTIVE || mission.getStatus() == MissionStatus.SAFE_MODE) {
			throw new MissionDeletionNotAllowedException(mission.getStatus());
		}
		
		//telemetryRepository.deleteByMissionId(id);
		sensorRepository.deleteByMissionId(id);
	    repository.deleteById(id);
	}
	
	private boolean isValidTransition(MissionStatus current, MissionStatus next) {
		switch (current) {
		case PLANNED:
			return next == MissionStatus.ACTIVE || next == MissionStatus.ABORTED;
			
		case ACTIVE:
			return next == MissionStatus.SAFE_MODE || next == MissionStatus.COMPLETED || next == MissionStatus.ABORTED; 
		
		case SAFE_MODE:
			return next == MissionStatus.ACTIVE || next == MissionStatus.COMPLETED || next == MissionStatus.ABORTED;
			
		case COMPLETED:
		case ABORTED:
			return false;
			
		default:
			return false;
		}
	}
	
	public List<MissionLatestReadingResponse> getLatestReadings(Long missionId) {
		repository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
		List<Sensor> sensors = sensorRepository.findByMissionId(missionId);
		
		List<MissionLatestReadingResponse> result = new ArrayList<>();
		
		for (Sensor sensor : sensors) {
		    List<TelemetryBySensor> readings = telemetryBySensorRepository.findByKeyMissionIdAndKeySensorId(missionId, sensor.getId());
		    
		    if (!readings.isEmpty()) {
		    	TelemetryBySensor latest =
	                    readings.get(0);
		    
		    	result.add(new MissionLatestReadingResponse(
	    					sensor.getId(),
                            sensor.getName(),
                            latest.getReadingValue(),
                            latest.getKey().getReadAt(),
                            latest.getReceivedAt()));
		    }
		}
		
		return result;
	}
}
