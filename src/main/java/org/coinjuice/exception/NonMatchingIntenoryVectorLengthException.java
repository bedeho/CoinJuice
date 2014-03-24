package org.coinjuice.exception;

// Excetion thrown by Inv constructor

public class NonMatchingIntenoryVectorLengthException extends Exception {
	
	long value;
	int actualInventoryLength;

	public NonMatchingIntenoryVectorLengthException(long value, int actualInventoryLength) {

		super("Count value cannot be any larger than 1000, but it was: " + value);

		this.value = value;
		this.actualInventoryLength = actualInventoryLength;
		
	}
}