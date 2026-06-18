package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;

public record MissionCreatedResponse(Long id, String name, String description, MissionStatus status, Instant createdAt) {}
