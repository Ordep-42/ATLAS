package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface TelemetryBySensorRepository extends CassandraRepository<TelemetryBySensor, TelemetryBySensorKey> {
	List<TelemetryBySensor> findByKeyMissionIdAndKeySensorId(
	        Long missionId,
	        Long sensorId);
}
