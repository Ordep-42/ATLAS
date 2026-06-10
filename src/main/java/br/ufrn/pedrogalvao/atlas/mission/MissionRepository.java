package br.ufrn.pedrogalvao.atlas.mission;

import java.util.List;
import java.util.Optional;

public interface MissionRepository {
	Mission save(Mission mission);
	Optional<Mission> findById(Long id);
	List<Mission> listAll();
	void deleteById(Long id);
}
