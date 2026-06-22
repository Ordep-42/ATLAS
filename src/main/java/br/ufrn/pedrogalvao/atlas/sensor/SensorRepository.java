package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
	List<Sensor> findByMissionId(Long missionId);
	Optional<Sensor> findByMissionIdAndId(Long missionId, Long id);
	Optional<Sensor> findByMissionIdAndSensorNumber(
	        Long missionId,
	        Integer sensorNumber);
	Optional<Sensor> findTopByMissionIdOrderBySensorNumberDesc(
			Long missionId);
	long countByMissionId(Long missionId);
	
	long deleteByMissionId(Long missionId);
}
