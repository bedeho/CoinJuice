package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

/** \class PongMessagePayload
*
* \brief Represents payload for pong message
*
* A more detailed class description...
*
*/
public class PongMessagePayload extends MessagePayload {

	// Nonce from ping
	private long nonce;

	// Constructor
	public PongMessagePayload(long nonce) {
		this.nonce = nonce;
	}

	public PongMessagePayload(LittleEndianDataInputStream input) throws IOException {
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