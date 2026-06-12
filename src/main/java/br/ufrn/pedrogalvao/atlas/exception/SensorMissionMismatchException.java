package br.ufrn.pedrogalvao.atlas.exception;

public class SensorMissionMismatchException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public SensorMissionMismatchException(Long missionId, Long sensorId) {
		super("Sensor " + sensorId + " não pertence a missão " + missionId);
	}

}
