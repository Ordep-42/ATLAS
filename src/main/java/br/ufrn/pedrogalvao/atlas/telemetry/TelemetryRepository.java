package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;
import java.util.Optional;

public interface TelemetryRepository {
    TelemetryReading save(TelemetryReading reading);
    Optional<TelemetryReading> findById(Long id);
    List<TelemetryReading> findByMissionId(Long missionId);
    List<TelemetryReading> findByMissionIdAndSensorId(Long missionId, Long sensorId);
    List<TelemetryReading> listAll();
    void deleteById(Long id);
}