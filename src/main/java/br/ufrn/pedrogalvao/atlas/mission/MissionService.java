package br.ufrn.pedrogalvao.atlas.mission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MissionService {
	
	private final MissionRepository repository;
	
	public MissionService(InMemoryMissionRepository repository) {
		this.repository = repository;
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
				.orElseThrow(() -> new RuntimeException("Missão Não encontrada: " + id));
		
		if (newStatus == MissionStatus.ACTIVE && mission.getStartedAt() == null) {
			mission.setStartedAt(LocalDateTime.now());
		}
		
		if (newStatus == MissionStatus.COMPLETED || newStatus == MissionStatus.ABORTED) {
			mission.setFinishedAt(LocalDateTime.now());
		}
		
		mission.setStatus(newStatus);
		return repository.save(mission);
	}
}
