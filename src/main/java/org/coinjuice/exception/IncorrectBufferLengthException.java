package org.coinjuice.exception;

// Used to notify when incorrect buffer sized variables are provided as input.

public class IncorrectBufferLengthException extends Exception {
	
	public IncorrectBufferLengthException(String message) {
		super(message);
	}
}