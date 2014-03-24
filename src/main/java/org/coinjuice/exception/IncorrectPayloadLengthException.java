package org.coinjuice.exception;

// Used to notify when a command does not match, primarly used for ByteBuffer based VersionMessage constructor.

// Used by validateChecksumAndLengthFields method in Message class
public class IncorrectPayloadLengthException extends Exception {

	int statedPayloadLength;
	int actualPayloadLength;
	
	public IncorrectPayloadLengthException(int statedPayloadLength, int actualPayloadLength) {

		super("The stated payload length was " + statedPayloadLength + ", while the actual payload length was " + actualPayloadLength);

		this.statedPayloadLength = statedPayloadLength;
		this.actualPayloadLength = actualPayloadLength;

	}
}