package org.coinjuice.exception;

public class CommandToLongException extends Exception {

	int commandLength;
	
	public CommandToLongException(int commandLength) {

		super("Command message cannot be longer than a maximum of 12 bytes, however it was: " + commandLength);
		
		this.commandLength = commandLength;
		
	}
}