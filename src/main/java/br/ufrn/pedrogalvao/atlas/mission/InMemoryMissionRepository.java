package br.ufrn.pedrogalvao.atlas.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryMissionRepository implements MissionRepository {

	private final Map<Long, Mission> store = new HashMap<>();
	private Long nextId = 1L;
	
	@Override
	public Mission save(Mission mission) {
		if (mission.getId() == null) {
			mission.setId(nextId++);
		}
		store.put(mission.getId(), mission);
		return mission;
	}

	@Override
	public Optional<Mission> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public List<Mission> listAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public void deleteById(Long id) {
		store.remove(id);
	}
}
