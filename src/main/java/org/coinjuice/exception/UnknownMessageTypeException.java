package org.coinjuice.exception;

// Used by Message getMessageType() constructor

public class UnknownMessageTypeException extends Exception {
	
	String val;

	public UnknownMessageTypeException(String val) {

		super("Unknown message type value: " + val);

		this.val = val;
		
	}
}