package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(
	name = "missions",
    indexes = {
        @Index(
            name = "idx_mission_status",
            columnList = "status"
        )
    }
)
public class Mission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(length = 300)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MissionStatus status;
	
	@Column(nullable = false)
	@CreationTimestamp
	private Instant createdAt;
	
	private Instant startedAt;
	private Instant finishedAt;
	
	public Mission() {
		this.status = MissionStatus.PLANNED;
	}
	
	public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MissionStatus getStatus() { return status; }
    public void setStatus(MissionStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getFinishedAt() { return finishedAt; }
    public void setFinishedAt(Instant finishedAt) { this.finishedAt = finishedAt; }
}
