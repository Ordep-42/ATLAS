package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;

public record MissionStatusUpdateResponse(Long id, String name, MissionStatus status, Instant createdAt, Instant startedAt, Instant finishedAt) {

}
