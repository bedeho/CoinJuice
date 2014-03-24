package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.exception.UnknownMagicValueException;
import org.coinjuice.exception.IncorrectPayloadLengthException;

public class MempoolMessagePayload extends MessagePayload {

	public ByteBuffer raw() {
		return ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);
	}

	public int rawLength() {
		return 0;
	}
}