package br.ufrn.pedrogalvao.atlas.exception;

public abstract class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}