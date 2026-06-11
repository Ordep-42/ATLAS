package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TelemetryReading> create(
            @RequestBody TelemetryCreateRequest request) {

        TelemetryReading created = service.create(
                request.getMissionId(),
                request.getSensorId(),
                request.getValue());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{missionId}")
    public ResponseEntity<List<TelemetryReading>> listByMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(service.listByMission(missionId));
    }

    @GetMapping("/{missionId}/{sensorId}")
    public ResponseEntity<List<TelemetryReading>> listByMissionAndSensor(@PathVariable Long missionId, @PathVariable Long sensorId) {
        return ResponseEntity.ok(service.listByMissionAndSensor(missionId, sensorId));
    }
    
    @GetMapping("/{missionId}/{sensorId}/latest")
    public ResponseEntity<TelemetryReading> latest(@PathVariable Long missionId, @PathVariable Long sensorId) {
        return ResponseEntity.ok(service.getLatestReading(missionId, sensorId));
    }
    
    @GetMapping("/{missionId}/{sensorId}/stats")
    public ResponseEntity<TelemetryStats> stats(@PathVariable Long missionId, @PathVariable Long sensorId) {
    	
        return ResponseEntity.ok(service.getStats(missionId, sensorId));
    }
}