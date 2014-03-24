package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.Tx;
import org.coinjuice.message.field.VariableLengthInteger;

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
	static final private int MAXIMUM_NUMBER_OF_BLOCKS = 2000;

	// Constructor
	public GetHeadersMessagePayload(int version, VariableLengthInteger hash_count, char[] block_locator_hashes, char[] hash_stop) {

		// Set payload fields
		this.version = version;
		this.hash_count = hash_count;
		this.block_locator_hashes = block_locator_hashes;
		this.hash_stop = hash_stop;
	}

	public GetHeadersMessagePayload(ByteBuffer b) {

		// version
		version = b.getInt();

		// hash_count
		hash_count = new VariableLengthInteger(b);

		// block_locator_hashes
		block_locator_hashes  = new char[32];		// Allocate buffer
		b.asCharBuffer().get(block_locator_hashes);	// Add through char view
		b.position(b.position() + 32);				// Advance buffer position

		// hash_stop
		hash_stop  = new char[32];			// Allocate buffer
		b.asCharBuffer().get(hash_stop);	// Read through char view
		b.position(b.position() + 32);		// Advance buffer position
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putInt(version);
		b.put(hash_count.raw());

		b.asCharBuffer().put(block_locator_hashes); // Write through char view
		b.position(b.position() + 32);			// Advance buffer position

		b.asCharBuffer().put(hash_stop);	 // Write through char view
		b.position(b.position() + 32);			// Advance buffer position

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public int rawLength() {
		return 4 + hash_count.rawLength() + 32 + 32;
	}

}