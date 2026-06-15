package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
	name = "telemetry_readings",
    indexes = {
        @Index(
            name = "idx_telemetry_mission",
            columnList = "missionId"
        ),
        @Index(
            name = "idx_telemetry_sensor",
            columnList = "sensorId"
        )
    }
)
public class TelemetryReading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
    private Long missionId;
	
	@Column(nullable = false)
    private Long sensorId;
	
    private Double readingValue;
    private LocalDateTime receivedAt;

    public TelemetryReading() {
        this.receivedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public Double getReadingValue() {
        return readingValue;
    }

    public void setReadingValue(Double value) {
        this.readingValue = value;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime timestamp) {
        this.receivedAt = timestamp;
    }
}