package de.opatut.flatman.data.exceptions;

public class AuthorizationRequiredException extends LoadDataException {
	private static final long serialVersionUID = -2735207705233973947L;
	public static final String IDENTIFIER = "AUTHORIZATION_REQUIRED";

	public AuthorizationRequiredException() {
		super(IDENTIFIER);
	}
}
