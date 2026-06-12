package br.ufrn.pedrogalvao.atlas.sensor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Sensores",
    description = "Gerenciamento de sensores associados às missões"
)
@RestController
@RequestMapping("/missions/{missionId}/sensors")
public class SensorController {

    private final SensorService service;

    public SensorController(SensorService service) {
        this.service = service;
    }

    @Operation(
	    summary = "Cadastrar sensor",
	    description = "Adiciona um novo sensor à missão informada."
	)
    @PostMapping
    public ResponseEntity<Sensor> create(@PathVariable Long missionId, @RequestBody SensorCreateRequest request) {

        Sensor created = service.create(
                missionId,
                request.getName(),
                request.getType(),
                request.getUnit());

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(
	    summary = "Listar sensores da missão",
	    description = "Retorna todos os sensores cadastrados para uma missão."
	)
    @GetMapping
    public ResponseEntity<List<Sensor>> listByMission(
            @PathVariable Long missionId) {

        return ResponseEntity.ok(service.findByMission(missionId));
    }
    
    @Operation(
	    summary = "Buscar sensor",
	    description = "Retorna os detalhes de um sensor específico."
	)
    @GetMapping("/{sensorId}")
    public ResponseEntity<Sensor> findById(
            @PathVariable Long missionId,
            @PathVariable Long sensorId) {

        return ResponseEntity.ok(
                service.findById(sensorId));
    }
    
    @Operation(
	    summary = "Excluir sensor",
	    description = "Remove um sensor da missão."
	)
    @DeleteMapping("/{sensorId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long missionId,
            @PathVariable Long sensorId) {

        service.delete(sensorId);

        return ResponseEntity.noContent().build();
    }
}
