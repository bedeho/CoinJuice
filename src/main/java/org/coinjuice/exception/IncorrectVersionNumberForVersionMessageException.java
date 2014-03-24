package org.coinjuice.exception;

// Is it appropriate to make an exception just for a constructor issue?
// Should this be put in name some how?

public class IncorrectVersionNumberForVersionMessageException extends Exception {

	int version;

	public IncorrectVersionNumberForVersionMessageException(int version, String message) {

		super(message);
		this.version = version;
	}
}