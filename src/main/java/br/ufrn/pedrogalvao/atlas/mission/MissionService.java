package br.ufrn.pedrogalvao.atlas.mission;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.sensor.Sensor;
import br.ufrn.pedrogalvao.atlas.sensor.SensorRepository;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryReading;
import br.ufrn.pedrogalvao.atlas.telemetry.TelemetryRepository;

@Service
public class MissionService {
	
	private final MissionRepository repository;
	private final SensorRepository sensorRepository;
	private final TelemetryRepository telemetryRepository;
	
	public MissionService(MissionRepository repository, SensorRepository sensorRepository, TelemetryRepository telemetryRepository) {
		this.repository = repository;
		this.sensorRepository = sensorRepository;
		this.telemetryRepository = telemetryRepository;
	}
	
	public Mission create(String name, String description) {
		Mission mission = new Mission();
		mission.setName(name);
		mission.setDescription(description);
		return repository.save(mission);
	}
	
	public List<Mission> listAll() {
		return repository.listAll();
	}
	
	public Optional<Mission> findById(Long id) {
		return repository.findById(id);
	}
	
	public Mission updateStatus(Long id, MissionStatus newStatus) {
		Mission mission = repository.findById(id)
				.orElseThrow(() -> new MissionNotFoundException(id));
		
		if (mission.getStatus() == newStatus) {
		    return mission;
		}
		
		if (newStatus == MissionStatus.ACTIVE && mission.getStartedAt() == null) {
			mission.setStartedAt(LocalDateTime.now());
		}
		
		if (newStatus == MissionStatus.COMPLETED || newStatus == MissionStatus.ABORTED) {
			mission.setFinishedAt(LocalDateTime.now());
		}
		
		if (!isValidTransition(mission.getStatus(), newStatus)) {
			throw new RuntimeException("Transição de status inválida: " + mission.getStatus() + " -> " + newStatus);
		}
		
		mission.setStatus(newStatus);
		return repository.save(mission);
	}
	
	public void delete(Long id) {
	    repository.deleteById(id);
	}
	
	private boolean isValidTransition(MissionStatus current, MissionStatus next) {
		switch (current) {
		case PLANNED:
			return next == MissionStatus.ACTIVE || next == MissionStatus.ABORTED;
			
		case ACTIVE:
			return next == MissionStatus.SAFE_MODE || next == MissionStatus.COMPLETED || next == MissionStatus.ABORTED; 
		
		case SAFE_MODE:
			return next == MissionStatus.COMPLETED || next == MissionStatus.COMPLETED || next == MissionStatus.ABORTED;
			
		case COMPLETED:
		case ABORTED:
			return false;
			
		default:
			return false;
		}
	}
	
	public MissionSummaryResponse getSummary(Long missionId) {
		Mission mission = repository.findById(missionId)
				.orElseThrow(() -> new MissionNotFoundException(missionId));
		
		long sensorCount = sensorRepository.findByMissionId(missionId).size();

		List<TelemetryReading> readings = telemetryRepository.findByMissionId(missionId);
		LocalDateTime lastTelemetryAt = readings.stream().map(TelemetryReading::getTimestamp).max(LocalDateTime::compareTo).orElse(null);
	    long telemetryCount = readings.size();
	    
	    return new MissionSummaryResponse(mission.getId(), mission.getName(), mission.getStatus(), sensorCount, telemetryCount, mission.getCreatedAt(), mission.getStartedAt(), lastTelemetryAt);
	}
	
	public List<MissionLatestReadingResponse> getLatestReadings(Long missionId) {
		repository.findById(missionId).orElseThrow(() -> new MissionNotFoundException(missionId));
		List<Sensor> sensors = sensorRepository.findByMissionId(missionId);
		
		List<MissionLatestReadingResponse> result = new ArrayList<>();
		
		for (Sensor sensor : sensors) {
		    List<TelemetryReading> readings = telemetryRepository.findByMissionIdAndSensorId(missionId, sensor.getId());
		    readings.stream().max(Comparator.comparing(TelemetryReading::getTimestamp)).ifPresent(latest -> result.add(
                    new MissionLatestReadingResponse(sensor.getId(), sensor.getName(), latest.getValue(), latest.getTimestamp())));
		}
		
		return result;
	}
}
