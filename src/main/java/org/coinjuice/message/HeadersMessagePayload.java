package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.BlockHeader;
import org.coinjuice.exception.IncorrectNumberOfBlockHeadersException;
import org.coinjuice.exception.UnknownMagicValueException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;

/** \class HeadersMessagePayload
*
* \brief Payload for Headers message
*
* Briefly the message and the methods on the message
*
*/
public class HeadersMessagePayload extends MessagePayload {

	// Number of block headers
	private VariableLengthInteger count;

	// Block headers
	private BlockHeader[] block_header;

	// Constructors
	public HeadersMessagePayload(VariableLengthInteger count, BlockHeader[] block_header)  throws IncorrectNumberOfBlockHeadersException, UnknownMagicValueException {

		this.count = count;
		this.block_header = block_header;

		// Check if the stated number of block headers matches the actual number of block headers
		if(count.getValue() == block_header.length)
			throw new IncorrectNumberOfBlockHeadersException(count.getValue(), block_header.length);
	}

	public HeadersMessagePayload(LittleEndianDataInputStream input) throws IncorrectNumberOfTransactionsException, IOException {

		// count
		count = new VariableLengthInteger(input); 

		// block_header
		for(int i = 0;i < count.getValue();i++)
			block_header[i] = new BlockHeader(input);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.put(count.raw());

		for(int i = 0;i < count.getValue();i++)
			b.put(block_header[i].raw());

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public int rawLength() {
		return count.rawLength() + (int)count.getValue()*BlockHeader.rawLength();
	}
}