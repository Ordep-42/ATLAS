package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufrn.pedrogalvao.atlas.exception.MissionNotFoundException;
import br.ufrn.pedrogalvao.atlas.exception.SensorCreationNotAllowedException;
import br.ufrn.pedrogalvao.atlas.exception.SensorDeletionNotAllowedException;
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
        	throw new SensorCreationNotAllowedException();
        }
        
        Sensor sensor = new Sensor();

        sensor.setMissionId(missionId);
        sensor.setName(name);
        sensor.setType(type);
        sensor.setUnit(unit);

        return sensorRepository.save(sensor);
    }

    public List<Sensor> findByMission(Long missionId) {
        missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));

        return sensorRepository.findByMissionId(missionId);
    }

    public Sensor findById(Long sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(() -> new SensorNotFoundException(sensorId));
    }

    @Transactional
    public void delete(Long missionId, Long sensorId) {
    	Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new MissionNotFoundException(missionId));
    	
    	sensorRepository.findById(sensorId)
        		.orElseThrow(() -> new SensorNotFoundException(sensorId));
    	if (mission.getStatus() != MissionStatus.PLANNED) {
        	throw new SensorDeletionNotAllowedException();
        }
    	sensorRepository.deleteById(sensorId);
    }
}
