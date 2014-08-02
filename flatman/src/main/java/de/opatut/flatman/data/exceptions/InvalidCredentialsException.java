package de.opatut.flatman.data.exceptions;

public class InvalidCredentialsException extends LoadDataException {
	private static final long serialVersionUID = 1038579494853366851L;
	public static final String IDENTIFIER = "INVALID_CREDENTIALS";

	public InvalidCredentialsException() {
		super(IDENTIFIER);
	}
}
