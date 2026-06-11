package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.exception.SensorNotFoundException;
import br.ufrn.pedrogalvao.atlas.mission.Mission;
import br.ufrn.pedrogalvao.atlas.mission.MissionRepository;
import br.ufrn.pedrogalvao.atlas.mission.MissionStatus;

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
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        if (mission.getStatus() != MissionStatus.PLANNED) {
        	throw new RuntimeException ("Só é possível adicionar sensores em uma missão planejada.");
        }
        
        Sensor sensor = new Sensor();

        sensor.setMissionId(missionId);
        sensor.setName(name);
        sensor.setType(type);
        sensor.setUnit(unit);

        return sensorRepository.save(sensor);
    }

    public List<Sensor> listByMission(Long missionId) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        return sensorRepository.findByMissionId(missionId);
    }

    public Sensor findById(Long sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException(sensorId));
    }

    public void delete(Long sensorId) {
    	sensorRepository.findById(sensorId)
        	.orElseThrow(() -> new SensorNotFoundException(sensorId));
    	sensorRepository.deleteById(sensorId);
    }
}
