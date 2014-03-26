package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** \class MempoolMessagePayload
*
* \brief Payload for Mempool message
*
* Briefly the message and the methods on the message
*
*/
public class MempoolMessagePayload extends MessagePayload {

	public ByteBuffer raw() {
		return ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);
	}

	public int rawLength() {
		return 0;
	}
}