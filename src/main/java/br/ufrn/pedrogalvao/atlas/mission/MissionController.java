package br.ufrn.pedrogalvao.atlas.mission;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Missões",
    description = "Gerenciamento e monitoramento de missões"
)
@RestController
@RequestMapping("/missions")
public class MissionController {

	private final MissionService service;
	
	public MissionController(MissionService service) {
		this.service = service;
	}
	
	@Operation(
	    summary = "Criar missão",
	    description = "Cria uma nova missão no estado PLANNED."
    )
	@PostMapping
	public ResponseEntity<Mission> create(@RequestBody Mission mission) {
		Mission created = service.create(mission.getName(), mission.getDescription());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@Operation(
	    summary = "Listar missões",
	    description = "Retorna todas as missões cadastradas."
	)
	@GetMapping
	public ResponseEntity<List<Mission>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@Operation(
	    summary = "Buscar missão por ID",
	    description = "Retorna os detalhes de uma missão específica."
	)
	@GetMapping("/{id}")
    public ResponseEntity<Mission> findById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

	@Operation(
	    summary = "Atualizar status da missão",
	    description = "Atualiza o estado operacional da missão respeitando as regras de transição definidas pelo sistema."
	)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Mission> updateStatus(@PathVariable Long id, @RequestBody MissionStatus status) {
        Mission updated = service.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @Operation(
	    summary = "Obter resumo da missão",
	    description = "Retorna informações consolidadas da missão, incluindo quantidade de sensores, leituras de telemetria e última atividade."
	)
    @GetMapping("/{id}/summary")
    public ResponseEntity<MissionSummaryResponse> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSummary(id));
    }
    
    @Operation(
	    summary = "Obter últimas leituras da missão",
	    description = "Retorna a leitura mais recente de cada sensor associado à missão."
	)
    @GetMapping("/{id}/latest")
    public ResponseEntity<List<MissionLatestReadingResponse>> getLatestReadings(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLatestReadings(id));
    }
    
    @Operation(
	    summary = "Excluir missão",
	    description = "Remove uma missão do sistema."
	)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	service.delete(id);
    	return ResponseEntity.noContent().build();
    }
}
