package br.ufrn.pedrogalvao.atlas.exception;

public class TelemetryNotFoundException extends ResourceNotFoundException{
	private static final long serialVersionUID = 1L;

	public TelemetryNotFoundException(Long missionId, Long sensorId) {
		super("Nenhuma leitura de telemetria encontrada para a missão " + missionId + " e sensor " + sensorId);
	}
	
	public TelemetryNotFoundException(Long id) {
		super("Leitura de telemetria não encontrada: " + id);
	}
	
	public TelemetryNotFoundException() {
		super("Nenhuma leitura de telemetria encontrada");
	}
}
