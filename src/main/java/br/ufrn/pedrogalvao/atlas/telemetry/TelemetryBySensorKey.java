package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TelemetryBySensorKey {

	@PrimaryKeyColumn(
			name= "mission_id",
			ordinal = 0,
			type = PrimaryKeyType.PARTITIONED
			)
	private Long missionId;

	@PrimaryKeyColumn(
			name= "sensor_id",
			ordinal = 1,
			type = PrimaryKeyType.PARTITIONED
			)
	private Long sensorId;
	
	@PrimaryKeyColumn(
			name= "read_at",
			ordinal = 2,
			type = PrimaryKeyType.CLUSTERED
			)
	private Instant readAt;
	
	public TelemetryBySensorKey(Long missionId, Long sensorId, Instant readAt) {
		this.missionId = missionId;
		this.sensorId = sensorId;
		this.readAt = readAt;
	}

	public Long getMissionId() {
		return missionId;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public Instant getReadAt() {
		return readAt;
	}

	public void setMissionId(Long missionId) {
		this.missionId = missionId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	public void setReadAt(Instant readAt) {
		this.readAt = readAt;
	}
}
