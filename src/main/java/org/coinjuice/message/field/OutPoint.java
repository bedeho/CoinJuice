package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.exception.IncorrectHashLengthException;

public class OutPoint {

	// The hash of the referenced transaction
	public char[] hash;

	// The index of the specific output in the transaction. The first output is 0, etc.
	public int index;

	// Constructor
	public OutPoint(char [] hash, int index)  throws IncorrectHashLengthException {

		if(hash.length != 32)
			throw new IncorrectHashLengthException(hash.length);

		this.hash = hash;
		this.index = index;

	}

	public OutPoint(ByteBuffer b) {

		// hash
		hash = new char[32];
		b.asCharBuffer().get(hash); // Read from buffer through view
		b.position(b.position() + 32); 	 // Advance position

		// index
		b.getInt(index);
	}

	// Computes raw data from fields
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.asCharBuffer().put(hash);  // Add to buffer through view
		b.position(b.position() + 32); // Advance position in original buffer
		b.putInt(index);

		// Return rewinded buffer
		b.rewind();

		// Return bufffer
		return b;
	}

	// Size of field
	public static int rawLength() {
		return 32 + 4;
	}
}