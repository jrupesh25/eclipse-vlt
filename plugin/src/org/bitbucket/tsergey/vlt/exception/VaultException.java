package org.bitbucket.tsergey.vlt.exception;

public class VaultException extends RuntimeException {

	private static final long serialVersionUID = -6267592065599196640L;

	private Type type;

	public VaultException(Type type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public VaultException(Type type, String message) {
		super(message);
		this.type = type;
	}

	public VaultException(Type type) {
		super();
		this.type = type;
	}

	public Type getType() {
		return type;
	}




	public enum Type {

		COMMAND_CONSTRUCT_ERROR, JCR_ROOT_CONFIG_ERROR, CREDENTIALS_CONFIG_ERROR, NO_FILE_SELECTION_ERROR

	}

}
