package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.mission.MissionRepository;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final MissionRepository missionRepository;

    public SensorService(
            SensorRepository sensorRepository,
            MissionRepository missionRepository) {

        this.sensorRepository = sensorRepository;
        this.missionRepository = missionRepository;
    }

    public Sensor create(Long missionId, String name, SensorType type, String unit) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada: " + missionId));

        Sensor sensor = new Sensor();

        sensor.setMissionId(missionId);
        sensor.setName(name);
        sensor.setType(type);
        sensor.setUnit(unit);

        return sensorRepository.save(sensor);
    }

    public List<Sensor> listByMission(Long missionId) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada: " + missionId));

        return sensorRepository.findByMissionId(missionId);
    }

    public Sensor findById(Long sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor não encontrado " + sensorId));
    }

    public void delete(Long sensorId) {
    	sensorRepository.findById(sensorId)
        	.orElseThrow(() -> new RuntimeException("Sensor não encontrado: " + sensorId));
    	sensorRepository.deleteById(sensorId);
    }
}
