package org.coinjuice.message.field;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

// Services field in network adresses and in version message
public class Services {

	// Value	 Name	 			Description
	// ----------------------------------------
	// 1	 	NODE_NETWORK	 	This node can be asked for full blocks instead of just headers.

	// uint64_t	 same service(s) listed in version
	long bitfield;

	public Services() {
		this.bitfield = 1;
	}
	
	public Services(long bitfield) {
		this.bitfield = bitfield;
	}

	public Services(LittleEndianDataInputStream input) throws IOException {
		this.bitfield = input.readLong();
	}

	public Services(ByteBuffer raw) {
		bitfield = raw.getLong();
	}

	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putLong(bitfield);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public static int rawLength() {
		return 8;
	}
}