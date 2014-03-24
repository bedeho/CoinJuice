package org.coinjuice.exception;

// Used to notify when a checksum does not match, primarly used for ByteBuffer based VersionMessage constructor.

public class IncorrectChecksumException extends Exception {

	byte [] computedChecksum;
	byte [] providedChecksum;
	
	public IncorrectChecksumException(byte [] computedChecksum, byte [] providedChecksum) {

		super("Provided checksum does not match hash of content.");
		//super("Provided checksum: " + + " does not match hash of content: " + );

		this.computedChecksum = computedChecksum;
		this.providedChecksum = providedChecksum;
		
	}
}