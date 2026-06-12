package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
	public List<Sensor> findByMissionId(Long missionId);
}
