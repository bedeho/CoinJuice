package org.coinjuice.message.field;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.exception.IncorrectHashLengthException;
import org.coinjuice.Util;

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

	public OutPoint(LittleEndianDataInputStream input) throws IOException {

		// hash
		hash = Util.readChar(input, 32);
		// index
		index = input.readInt();
	}

	// Computes raw data from fields
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		Util.writeChar(b, hash);
		b.putInt(index);

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of field
	public static int rawLength() {
		return 32 + 4;
	}
}