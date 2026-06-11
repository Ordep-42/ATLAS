package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTelemetryRepository implements TelemetryRepository {

    private final Map<Long, TelemetryReading> store = new HashMap<>();

    private Long nextId = 1L;

    @Override
    public TelemetryReading save(TelemetryReading reading) {

        if (reading.getId() == null) {
            reading.setId(nextId++);
        }

        store.put(reading.getId(), reading);

        return reading;
    }

    @Override
    public Optional<TelemetryReading> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<TelemetryReading> findByMissionId(Long missionId) {

        List<TelemetryReading> readings = new ArrayList<>();

        for (TelemetryReading reading : store.values()) {
            if (reading.getMissionId().equals(missionId)) {
                readings.add(reading);
            }
        }

        return readings;
    }

    @Override
    public List<TelemetryReading> findByMissionIdAndSensorId(Long missionId, Long sensorId) {
        List<TelemetryReading> readings = new ArrayList<>();

        for (TelemetryReading reading : store.values()) {
            if (reading.getMissionId().equals(missionId)
                    && reading.getSensorId().equals(sensorId)) {

                readings.add(reading);
            }
        }

        return readings;
    }

    @Override
    public List<TelemetryReading> listAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}