package org.coinjuice;

import java.nio.ByteBuffer;
import java.io.IOException;
import com.google.common.io.LittleEndianDataInputStream;

/** \class Util
*
* \brief Utility class with static methods, filling the holes in java api. 
*
* Briefly describe the groups of functions that exist?
*
*/
public class Util {
	
	// Switch endianness
	static public short swapEndian(short v) {
		
		int b1 = 0x000000ff & v; // only keep first byte
		int b2 = 0x0000ff00 & v; // only keep second byte
		
		return (short) ((b1 << 8) | (b2 >> 8)); // swap two bytes inside short
	}
	
	static public char [] readChar(LittleEndianDataInputStream input, int numberOfChars) throws IOException {
		
		// Allocate buffer
		byte [] tmp = new byte[numberOfChars];
		
		// Read bytes into buffer
		input.read(tmp);
		
		// Wrap into a ByteBuffer, turn that into a CharBuffer, and return underlying array
		return ByteBuffer.wrap(tmp).asCharBuffer().array();
	}
	
	static public void writeChar(ByteBuffer b, char v []) {
		b.asCharBuffer().put(v); // Write through char buffer
		b.position(b.position() + v.length); // advance position in underlying buffer
	}
}