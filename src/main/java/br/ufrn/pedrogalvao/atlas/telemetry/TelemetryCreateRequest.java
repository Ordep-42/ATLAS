package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public record TelemetryCreateRequest(
		@NotNull(message = "Mission ID é obrigatório")
		Long missionId, 
		
		@NotNull(message = "Numero do sensor é obrigatório")
		Integer sensorNumber, 
		
		@NotNull(message = "Valor da leitura é obrigatório")
		Double readingValue, 
		
		@NotNull(message = "Timestamp da leitura é obrigatório")
		Instant readAt
) {}