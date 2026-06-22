package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufrn.pedrogalvao.atlas.export.ExportService;
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
    private final ExportService exportService;

    public TelemetryController(TelemetryService service, ExportService exportService) {
        this.service = service;
		this.exportService = exportService;
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
                request.sensorNumber(),
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
    @GetMapping("/{missionId}/{sensorNumber}")
    public ResponseEntity<List<TelemetryResponse>> listByMissionAndSensor(@PathVariable Long missionId, @PathVariable Integer sensorNumber) {
        return ResponseEntity.ok(service.listByMissionAndSensor(missionId, sensorNumber));
    }
    
    @Operation(
	    summary = "Obter última leitura",
	    description = "Retorna a leitura mais recente registrada para o sensor."
	)
    @GetMapping("/{missionId}/{sensorNumber}/latest")
    public ResponseEntity<TelemetryResponse> latest(@PathVariable Long missionId, @PathVariable Integer sensorNumber) {
        return ResponseEntity.ok(service.getLatestReading(missionId, sensorNumber));
    }
    
    @Operation(
	    summary = "Obter estatísticas de telemetria",
	    description = "Retorna métricas estatísticas da telemetria do sensor, incluindo quantidade de leituras, valor mínimo, valor máximo e média."
	)
    @GetMapping("/{missionId}/{sensorNumber}/stats")
    public ResponseEntity<TelemetryStatsResponse> stats(@PathVariable Long missionId, @PathVariable Integer sensorNumber) {
    	
        return ResponseEntity.ok(service.getStats(missionId, sensorNumber));
    }
    
    @Operation(
		summary = "Exporta os dados de um sensor específico como um arquivo CSV",
 	    description = "Retorna um arquivo com todas as leituras de um sensor de uma missão."
	)
    @GetMapping("/{missionId}/{sensorNumber}/export")
    public ResponseEntity<byte[]> exportReadings(@PathVariable Long missionId, @PathVariable Integer sensorNumber) {
		String csv =
   			 exportService.exportSensorTelemetry(missionId, sensorNumber);

   	    return ResponseEntity.ok()
   	            .header(
   	                    HttpHeaders.CONTENT_DISPOSITION,
   	                    "attachment; filename=mission-"
   	                            + missionId + "-sensor-" + sensorNumber
   	                            + "-telemetry.csv")
   	            .contentType(
   	                    MediaType.parseMediaType(
   	                            "text/csv"))
   	            .body(csv.getBytes());
    }
}