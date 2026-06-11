package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/missions/{missionId}/sensors")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Sensor> create(@PathVariable Long missionId, @RequestBody SensorCreateRequest request) {

        Sensor created = service.create(
                missionId,
                request.getName(),
                request.getType(),
                request.getUnit());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> listByMission(
            @PathVariable Long missionId) {

        return ResponseEntity.ok(service.listByMission(missionId));
    }
    
    @GetMapping("/{sensorId}")
    public ResponseEntity<Sensor> findById(
            @PathVariable Long missionId,
            @PathVariable Long sensorId) {

        return ResponseEntity.ok(
                service.findById(sensorId));
    }
    
    @DeleteMapping("/{sensorId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long missionId,
            @PathVariable Long sensorId) {

        service.delete(sensorId);

        return ResponseEntity.noContent().build();
    }
}
