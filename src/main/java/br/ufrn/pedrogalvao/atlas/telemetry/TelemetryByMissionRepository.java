package br.ufrn.pedrogalvao.atlas.telemetry;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface TelemetryByMissionRepository extends CassandraRepository<TelemetryByMission, TelemetryByMissionKey>{
	List<TelemetryByMission> findByKeyMissionId(
	        Long missionId);
}
