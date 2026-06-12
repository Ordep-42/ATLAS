package br.ufrn.pedrogalvao.atlas.exception;

public class SensorCreationNotAllowedException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public SensorCreationNotAllowedException() {
		super("Sensores só podem ser adicionados a missões no estágio de planejamento.");
	}

}
