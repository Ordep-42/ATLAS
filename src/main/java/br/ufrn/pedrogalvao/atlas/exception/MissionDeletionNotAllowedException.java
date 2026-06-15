package br.ufrn.pedrogalvao.atlas.exception;

import br.ufrn.pedrogalvao.atlas.mission.MissionStatus;

public class MissionDeletionNotAllowedException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public MissionDeletionNotAllowedException(MissionStatus status) {
		super("Missões só podem ser removidas no estágio de planejamento ou após completas. Status atual da missão: " + status);
	}
}
