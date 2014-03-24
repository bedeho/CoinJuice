package org.coinjuice.exception;

// Excetion thrown by Inv constructor
import org.coinjuice.message.InvMessagePayload;

public class ToManyEntriesException extends Exception {
	
	long count;

	public ToManyEntriesException(long count) {

		super("The maximum number of entries " + InvMessagePayload.MAXIMUM_NUMBER_OF_ENTRIES + ", however then number of entries found was: " + count);

		this.count = count;
		
	}
}