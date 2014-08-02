package de.opatut.flatman.data.exceptions;

import java.io.IOException;

public class LoadDataException extends IOException {
	private static final long serialVersionUID = -2986202207956855137L;

	public LoadDataException(String msg) {
		super(msg);
	}
}
