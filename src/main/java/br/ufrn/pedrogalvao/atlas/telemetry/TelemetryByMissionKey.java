package br.ufrn.pedrogalvao.atlas.telemetry;

import java.time.Instant;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class TelemetryByMissionKey {

	@PrimaryKeyColumn(
			name = "mission_id",
			ordinal = 0,
			type = PrimaryKeyType.PARTITIONED
			)
	private Long missionId;
	
	@PrimaryKeyColumn(
			name = "read_at",
			ordinal = 1,
			type = PrimaryKeyType.CLUSTERED
			)
	private Instant readAt;
	
	@PrimaryKeyColumn(
			name = "sensor_id",
			ordinal = 2,
			type = PrimaryKeyType.CLUSTERED
			)
	private Long sensorId;

	public TelemetryByMissionKey(Long missionId, Instant readAt, Long sensorId) {
		this.missionId = missionId;
		this.readAt = readAt;
		this.sensorId = sensorId;
	}

	public Long getMissionId() {
		return missionId;
	}

	public Instant getReadAt() {
		return readAt;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setMissionId(Long missionId) {
		this.missionId = missionId;
	}

	public void setReadAt(Instant readAt) {
		this.readAt = readAt;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}
}
