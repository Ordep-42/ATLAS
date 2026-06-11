package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class InMemorySensorRepository implements SensorRepository {

	private final Map<Long, Sensor> store = new HashMap<>();

    private Long nextId = 1L;

    @Override
    public Sensor save(Sensor sensor) {

        if (sensor.getId() == null) {
            sensor.setId(nextId++);
        }

        store.put(sensor.getId(), sensor);

        return sensor;
    }

    @Override
    public Optional<Sensor> findById(Long sensorId) {
        return Optional.ofNullable(store.get(sensorId));
    }

    @Override
    public List<Sensor> findByMissionId(Long missionId) {

        List<Sensor> sensors = new ArrayList<>();

        for (Sensor sensor : store.values()) {

            if (sensor.getMissionId().equals(missionId)) {
                sensors.add(sensor);
            }

        }

        return sensors;
    }

    @Override
    public List<Sensor> listAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(Long sensorId) {
        store.remove(sensorId);
    }
}
