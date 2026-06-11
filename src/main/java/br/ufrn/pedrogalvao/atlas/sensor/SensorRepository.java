package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;
import java.util.Optional;

public interface SensorRepository {
	Sensor save(Sensor sensor);
    Optional<Sensor> findById(Long sensorId);
    List<Sensor> findByMissionId(Long missionId);
    List<Sensor> listAll();
    void deleteById(Long sensorId);
}
