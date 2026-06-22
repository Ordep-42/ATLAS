package br.ufrn.pedrogalvao.atlas.mission;

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
    name = "Missões",
    description = "Gerenciamento e monitoramento de missões"
)
@RestController
@RequestMapping("/missions")
public class MissionController {

	private final MissionService service;
	private final ExportService exportService;
	
	public MissionController(MissionService service, ExportService exportService) {
		this.service = service;
		this.exportService = exportService;
	}
	
	@Operation(
	    summary = "Criar missão",
	    description = "Cria uma nova missão no estado PLANNED."
    )
	@PostMapping
	public ResponseEntity<MissionCreatedResponse> create(@Valid @RequestBody MissionCreateRequest request) {
		MissionCreatedResponse created = service.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@Operation(
	    summary = "Listar missões",
	    description = "Retorna todas as missões cadastradas."
	)
	@GetMapping
	public ResponseEntity<List<MissionCreatedResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@Operation(
	    summary = "Obter resumo da missão",
	    description = "Retorna os detalhes de uma missão específica."
	)
	@GetMapping("/{id}")
    public ResponseEntity<MissionSummaryResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(service.findById(id));
    }

	@Operation(
	    summary = "Atualizar status da missão",
	    description = "Atualiza o estado operacional da missão respeitando as regras de transição definidas pelo sistema."
	)
    @PatchMapping("/{id}/status")
    public ResponseEntity<MissionStatusUpdateResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody MissionStatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
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
    	    summary = "Exporta os dados da missão como um arquivo CSV",
    	    description = "Retorna um arquivo dos dados dos sensores da missão."
	)
    @GetMapping("/{id}/export")
    public ResponseEntity<byte[]> exportReadings(@PathVariable Long id) {
    	 String csv =
    			 exportService.exportMissionTelemetry(id);

    	    return ResponseEntity.ok()
    	            .header(
    	                    HttpHeaders.CONTENT_DISPOSITION,
    	                    "attachment; filename=mission-"
    	                            + id
    	                            + "-telemetry.csv")
    	            .contentType(
    	                    MediaType.parseMediaType(
    	                            "text/csv"))
    	            .body(csv.getBytes());
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
