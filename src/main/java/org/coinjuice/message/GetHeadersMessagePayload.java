package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.Util;

/** \class GetHeadersMessagePayload
*
* \brief Payload for GetHeaders message
*
* Briefly the message and the methods on the message
*
*/
public class GetHeadersMessagePayload extends MessagePayload {

	// The protocol version
	private int version;

	// Number of block locator hash entries
	private VariableLengthInteger hash_count;

	// Block locator object; newest back to genesis block (dense to start, but then sparse)
	private char[] block_locator_hashes;

	// Hash of the last desired block; set to zero to get as many blocks as possible (500)
	private char[] hash_stop;

	// The maximum number of blocks 
	//static final private int MAXIMUM_NUMBER_OF_BLOCKS = 2000;

	// Constructor
	public GetHeadersMessagePayload(int version, VariableLengthInteger hash_count, char[] block_locator_hashes, char[] hash_stop) {

		this.version = version;
		this.hash_count = hash_count;
		this.block_locator_hashes = block_locator_hashes;
		this.hash_stop = hash_stop;
	}

	public GetHeadersMessagePayload(ByteBuffer b) {

		version = b.getInt();
		hash_count = new VariableLengthInteger(b);
		
		block_locator_hashes = new char[32];
		Util.readChar(b, block_locator_hashes);
		
		hash_stop = new char[32];
		Util.readChar(b, hash_stop);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putInt(version);
		b.put(hash_count.raw());
		Util.writeChar(b, block_locator_hashes);
		Util.writeChar(b, hash_stop);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public int rawLength() {
		return 4 + hash_count.rawLength() + 32 + 32;
	}
}