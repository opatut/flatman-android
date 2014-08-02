package de.opatut.flatman.data.exceptions;

public class FormErrorsException extends LoadDataException {
	private static final long serialVersionUID = 8604575673832056877L;
	public static final String IDENTIFIER = "FORM_ERROS";

	public FormErrorsException() {
		super(IDENTIFIER);
	}
}
