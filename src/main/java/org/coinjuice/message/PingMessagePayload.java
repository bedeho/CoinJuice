package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PingMessagePayload extends MessagePayload {

	// Random nonce
	private long nonce;

	// Constructor
	public PingMessagePayload(long nonce) {
		this.nonce = nonce;
	}

	public PingMessagePayload(ByteBuffer b) {
		nonce = b.getLong();
	}

	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.putLong(nonce);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public int rawLength() {
		return 8;
	}
}