package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

public record TelemetryCreateRequest(Long missionId, Long sensorId, Double readingValue, Instant readAt) {}