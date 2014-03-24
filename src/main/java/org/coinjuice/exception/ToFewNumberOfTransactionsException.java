package org.coinjuice.exception;

// Used to notify when number of trasnactions field in block header is lower than 1.

public class ToFewNumberOfTransactionsException extends Exception {
	
	long count;

	public ToFewNumberOfTransactionsException(long count) {
		super("Number of transaction entries field of block header must be 0, was " + count);

		this.count = count;
	}
}