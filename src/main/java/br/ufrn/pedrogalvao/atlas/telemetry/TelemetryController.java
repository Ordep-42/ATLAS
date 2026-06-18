package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
    name = "Telemetria",
    description = "Recepção e consulta de dados de telemetria"
)
@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @Operation(
	    summary = "Registrar telemetria",
	    description = "Armazena uma nova leitura de telemetria proveniente de um sensor."
	)
    @PostMapping
    public ResponseEntity<TelemetryResponse> create(
    		@Valid @RequestBody TelemetryCreateRequest request) {

        TelemetryResponse created = service.create(
                request.missionId(),
                request.sensorId(),
                request.readingValue(),
                request.readAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
	    summary = "Listar telemetria da missão",
	    description = "Retorna todas as leituras associadas à missão."
	)
    @GetMapping("/{missionId}")
    public ResponseEntity<List<TelemetryResponse>> listByMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(service.listByMission(missionId));
    }

    @Operation(
	    summary = "Listar telemetria do sensor",
	    description = "Retorna todas as leituras registradas para um sensor específico."
	)
    @GetMapping("/{missionId}/{sensorId}")
    public ResponseEntity<List<TelemetryResponse>> listByMissionAndSensor(@PathVariable Long missionId, @PathVariable Long sensorId) {
        return ResponseEntity.ok(service.listByMissionAndSensor(missionId, sensorId));
    }
    
    @Operation(
	    summary = "Obter última leitura",
	    description = "Retorna a leitura mais recente registrada para o sensor."
	)
    @GetMapping("/{missionId}/{sensorId}/latest")
    public ResponseEntity<TelemetryResponse> latest(@PathVariable Long missionId, @PathVariable Long sensorId) {
        return ResponseEntity.ok(service.getLatestReading(missionId, sensorId));
    }
    
    @Operation(
	    summary = "Obter estatísticas de telemetria",
	    description = "Retorna métricas estatísticas da telemetria do sensor, incluindo quantidade de leituras, valor mínimo, valor máximo e média."
	)
    @GetMapping("/{missionId}/{sensorId}/stats")
    public ResponseEntity<TelemetryStatsResponse> stats(@PathVariable Long missionId, @PathVariable Long sensorId) {
    	
        return ResponseEntity.ok(service.getStats(missionId, sensorId));
    }
}