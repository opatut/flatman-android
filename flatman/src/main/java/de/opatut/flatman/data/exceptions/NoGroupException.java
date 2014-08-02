package de.opatut.flatman.data.exceptions;

public class NoGroupException extends LoadDataException {
	private static final long serialVersionUID = 8501980256826887555L;
	public static final String IDENTIFIER = "NO_GROUP";

	public NoGroupException() {
		super(IDENTIFIER);
	}
}