package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

public record TelemetryResponse(
		Long missionId,

        Long sensorId,

        Double readingValue,

        Instant readAt,

        Instant receivedAt
) {}
