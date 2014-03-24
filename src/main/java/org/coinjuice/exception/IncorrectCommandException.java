package org.coinjuice.exception;

// Used to notify when a command does not match, primarly used for ByteBuffer based VersionMessage constructor.

public class IncorrectCommandException extends Exception {

	String incorrectCommand;
	String expectedCommand;
	
	public IncorrectCommandException(String incorrectCommand, String expectedCommand) {

		super(expectedCommand + " command expected, instead found " + incorrectCommand + " character command");
		
		this.incorrectCommand = incorrectCommand;
		this.expectedCommand = expectedCommand;

	}
}