package br.ufrn.pedrogalvao.atlas.mission;

import java.time.LocalDateTime;

public class MissionSensorLatestReading {
	 private Long sensorId;
    private String sensorName;
    private Double value;
    private LocalDateTime timestamp;
    
	public MissionSensorLatestReading(Long sensorId, String sensorName, Double value, LocalDateTime timestamp) {
		this.sensorId = sensorId;
		this.sensorName = sensorName;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public Long getSensorId() {
		return sensorId;
	}
	
	public String getSensorName() {
		return sensorName;
	}
	
	public Double getValue() {
		return value;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
    
}
