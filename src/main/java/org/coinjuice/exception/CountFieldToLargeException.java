package org.coinjuice.exception;

// Excetion thrown by Addr constructor

public class CountFieldToLargeException extends Exception {
	
	int count;

	public CountFieldToLargeException(int count) {

		super("Count value cannot be any larger than 1000, but it was: " + count);

		this.count = count;
		
	}
}