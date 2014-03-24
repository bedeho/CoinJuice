package org.coinjuice.exception;

// Used to notify when HeadersMessage class receives a stated number of block headers different from the actual number of block headers

public class IncorrectNumberOfBlockHeadersException extends Exception {
	
	long statedNumberOfBlockHeaders;
	int actualNumberOfBlockHeaders;

	public IncorrectNumberOfBlockHeadersException(long statedNumberOfBlockHeaders, int actualNumberOfBlockHeaders) {

		super("The stated number of block headers was " + statedNumberOfBlockHeaders + ", but the actual number of block headers was: " + actualNumberOfBlockHeaders);

		this.statedNumberOfBlockHeaders = statedNumberOfBlockHeaders;
		this.actualNumberOfBlockHeaders = actualNumberOfBlockHeaders;
	}
}