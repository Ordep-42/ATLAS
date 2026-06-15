package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
	List<Sensor> findByMissionId(Long missionId);
	long countByMissionId(Long missionId);
	
	long deleteByMissionId(Long missionId);
}
