package br.ufrn.pedrogalvao.atlas.mission;

import java.time.LocalDateTime;

public class MissionSummaryResponse {
	private Long id;
	private String name;
	private MissionStatus status;
	private long sensorCount;
	private long telemetryCount;
	private LocalDateTime createdAt;
	private LocalDateTime startedAt;
	private LocalDateTime lastTelemetryAt;
	
	public MissionSummaryResponse(Long id, String name, MissionStatus status, long sensorCount, long telemetryCount,
			LocalDateTime createdAt, LocalDateTime startedAt, LocalDateTime lastTelemetryAt) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.sensorCount = sensorCount;
		this.telemetryCount = telemetryCount;
		this.createdAt = createdAt;
		this.startedAt = startedAt;
		this.lastTelemetryAt = lastTelemetryAt;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public MissionStatus getStatus() {
		return status;
	}

	public long getSensorCount() {
		return sensorCount;
	}

	public long getTelemetryCount() {
		return telemetryCount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public LocalDateTime getLastTelemetryAt() {
		return lastTelemetryAt;
	}
}
