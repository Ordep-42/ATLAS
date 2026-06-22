package br.ufrn.pedrogalvao.atlas.mission;

import java.time.Instant;

public record MissionLatestReadingResponse(Integer sensorNumber, String sensorName, Double readingValue, Instant readAt, Instant receivedAt) {}
