package br.ufrn.pedrogalvao.atlas.exception;

public class SensorDeletionNotAllowedException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public SensorDeletionNotAllowedException() {
		super("Sensores só podem ser removidos de missões no estágio de planejamento ou completadas.");
	}
}
