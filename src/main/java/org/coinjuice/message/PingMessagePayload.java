package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

/** \class PingMessagePayload
*
* \brief Represents payload for ping message
*
* A more detailed class description...
*
*/
public class PingMessagePayload extends MessagePayload {

	// Random nonce
	private long nonce;

	// Constructor
	public PingMessagePayload(long nonce) {
		this.nonce = nonce;
	}

	public PingMessagePayload(LittleEndianDataInputStream input) throws IOException {
		nonce = input.readLong();
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