package org.coinjuice.exception;

// Is it appropriate to make an exception just for a constructor issue?
// Should this be put in name some how?

public class UnknownMagicValueException extends Exception {
	
	int val;

	public UnknownMagicValueException(int val) {

		super("Unknown magic value: " + val);

		this.val = val;
		
	}
}