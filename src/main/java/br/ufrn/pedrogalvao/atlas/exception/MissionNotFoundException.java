package br.ufrn.pedrogalvao.atlas.exception;

public class MissionNotFoundException extends ResourceNotFoundException {
	private static final long serialVersionUID = 1L;

	public MissionNotFoundException(Long id) {
		super("Missão não encontrada: " + id);
	}
}
