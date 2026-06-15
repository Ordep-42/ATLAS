package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryRepository extends JpaRepository<TelemetryReading, Long>{
    List<TelemetryReading> findByMissionId(Long missionId);
    List<TelemetryReading> findByMissionIdAndSensorId(Long missionId, Long sensorId);
    Optional<TelemetryReading> findFirstByMissionIdAndSensorIdOrderByReceivedAtDesc(
            Long missionId,
            Long sensorId);
    Optional<TelemetryReading> findFirstByMissionIdOrderByReceivedAtDesc(
            Long missionId);
    long countByMissionId(Long missionId);
    long countByMissionIdAndSensorId(Long missionId, Long sensorId);
    long deleteByMissionId(Long missionId);
}