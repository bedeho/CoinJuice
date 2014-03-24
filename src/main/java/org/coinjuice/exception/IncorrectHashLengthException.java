package org.coinjuice.exception;

// Is it appropriate to make an exception just for a constructor issue?
// Should this be put in name some how?

// For constructor of OutPoint when it is bassed a hash char array of incorrect length

public class IncorrectHashLengthException extends Exception {

	int length;

	public IncorrectHashLengthException(int length) {
		super("Hash length must be 32 bytes, was instead: " + length);

		this.length = length;
	}
}