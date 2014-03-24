package org.coinjuice.exception;

// Is it appropriate to make an exception just for a constructor issue?
// Should this be put in name some how?

public class IncorrectNumberOfTransactionsException extends Exception {

	long count;

	public IncorrectNumberOfTransactionsException(long count) {

		super("Number of transaction entries field of block header must be 0, was: " + count);

		this.count = count;
	}
}