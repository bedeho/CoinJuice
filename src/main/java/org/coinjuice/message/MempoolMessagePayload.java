package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** \class MempoolMessagePayload
*
* \brief Payload for Mempool message
*
* The mempool message sends a request to a node asking for information about transactions it has verified but which have not yet confirmed. The response to receiving this message is an inv message containing the transaction hashes for all the transactions in the node's mempool.
No additional data is transmitted with this message.
It is specified in BIP 35. Since BIP 37, only transactions matching the filter are replied.
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