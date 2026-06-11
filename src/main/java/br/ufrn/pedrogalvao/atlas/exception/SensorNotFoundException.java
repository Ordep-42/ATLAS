package br.ufrn.pedrogalvao.atlas.exception;

public class SensorNotFoundException extends ResourceNotFoundException{
	private static final long serialVersionUID = 1L;

	public SensorNotFoundException(Long id) {
		super("Sensor não encontrado: " + id);
	}
}
