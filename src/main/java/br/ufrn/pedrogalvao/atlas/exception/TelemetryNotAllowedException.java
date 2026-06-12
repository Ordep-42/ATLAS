package br.ufrn.pedrogalvao.atlas.exception;

public class TelemetryNotAllowedException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public TelemetryNotAllowedException() {
		super("Não é possível receber telemetria de uma missão inativa.");
	}

}
