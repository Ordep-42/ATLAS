package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("telemetry_by_sensor")
public class TelemetryBySensor {
	
	@PrimaryKey
	private TelemetryBySensorKey key;
	
	@Column("received_at")
	private Instant receivedAt;
	
	@Column("reading_value")
	private Double readingValue;

	public TelemetryBySensor(TelemetryBySensorKey key, Instant receivedAt, Double readingValue) {
		this.key = key;
		this.receivedAt = receivedAt;
		this.readingValue = readingValue;
	}

	public TelemetryBySensorKey getKey() {
		return key;
	}

	public Instant getReceivedAt() {
		return receivedAt;
	}

	public Double getReadingValue() {
		return readingValue;
	}

	public void setKey(TelemetryBySensorKey key) {
		this.key = key;
	}

	public void setReceivedAt(Instant receivedAt) {
		this.receivedAt = receivedAt;
	}

	public void setReadingValue(Double readingValue) {
		this.readingValue = readingValue;
	}
}
