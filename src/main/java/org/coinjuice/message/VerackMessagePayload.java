package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** \class VerackMessagePayload
*
* \brief Represents payload for verack message
*
* A more detailed class description...
*
*/
public class VerackMessagePayload extends MessagePayload {

	public ByteBuffer raw() {
		return ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);
	}

	public int rawLength() {
		return 0;
	}
}