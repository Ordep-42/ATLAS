package br.ufrn.pedrogalvao.atlas.mission;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/missions")
public class MissionController {

	private final MissionService service;
	
	public MissionController(MissionService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<Mission> create(@RequestBody Mission mission) {
		Mission created = service.create(mission.getName(), mission.getDescription());
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@GetMapping
	public ResponseEntity<List<Mission>> listAll() {
		return ResponseEntity.ok(service.listAll());
	}

	@GetMapping("/{id}")
    public ResponseEntity<Mission> findById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Mission> updateStatus(@PathVariable Long id, @RequestBody MissionStatus status) {
        Mission updated = service.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/summary")
    public ResponseEntity<MissionSummaryResponse> getSummary(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSummary(id));
    }
    
    @GetMapping("/{id}/latest")
    public ResponseEntity<List<MissionLatestReadingResponse>> getLatestReadings(@PathVariable Long id) {
        return ResponseEntity.ok(service.getLatestReadings(id));
    }
}
