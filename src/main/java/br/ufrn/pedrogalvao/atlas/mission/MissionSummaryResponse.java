package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;

public record MissionSummaryResponse(Long id, String name, String description, MissionStatus status, long sensorCount, long telemetryCount, Instant createdAt, Instant startedAt,
		Instant lastTelemetryAt, Instant finishedAt) {
}
