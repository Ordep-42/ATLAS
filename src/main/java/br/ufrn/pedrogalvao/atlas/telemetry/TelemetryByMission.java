package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("telemetry_by_mission")
public class TelemetryByMission {

	@PrimaryKey
	private TelemetryByMissionKey key;
	
	@Column("received_at")
	private Instant receivedAt;
	
	@Column("reading_value")
	private Double readingValue;

	public TelemetryByMission(TelemetryByMissionKey key, Instant receivedAt, Double readingValue) {
		this.key = key;
		this.receivedAt = receivedAt;
		this.readingValue = readingValue;
	}

	public TelemetryByMissionKey getKey() {
		return key;
	}

	public Instant getReceivedAt() {
		return receivedAt;
	}

	public Double getReadingValue() {
		return readingValue;
	}

	public void setKey(TelemetryByMissionKey key) {
		this.key = key;
	}

	public void setReceivedAt(Instant receivedAt) {
		this.receivedAt = receivedAt;
	}

	public void setReadingValue(Double readingValue) {
		this.readingValue = readingValue;
	}
}
