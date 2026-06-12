package br.ufrn.pedrogalvao.atlas.exception;

import br.ufrn.pedrogalvao.atlas.mission.MissionStatus;

public class InvalidTransitionException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public InvalidTransitionException(MissionStatus current, MissionStatus next) {
		super("Transição de status inválida: " + current + " -> " + next);
	}
}
