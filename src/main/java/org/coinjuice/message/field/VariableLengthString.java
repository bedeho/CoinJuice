package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.coinjuice.message.field.VariableLengthInteger;

public class VariableLengthString {

	// Length of string
	VariableLengthInteger length;

	// String
	// java Strings and arrays are no longer than Integer.MAX, which is the signed max for 4 byte.
	// This is way less than variable length integer max value of unsigned max for 8 bytes,
	// hence this is a potential for a bug if someone sends a gigantic string
	String s;

	public VariableLengthString(ByteBuffer b) {

		// Load length of string
		length = new VariableLengthInteger(b);

		// Allocate space for string
		byte [] stringField = new byte[length.getValue()];

		// Load string data into buffer
		b.get(stringField);

		// Save string
		s = new String(stringField);
	}

	public VariableLengthString(String s) {
		
		// Save string
		this.s = s;

		// Pick most compact variable integer representation
		this.length = new VariableLengthInteger(s.length());
	}

	// Computes raw data from fields
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.put(length.raw());
		b.put(s.getBytes(Charset.forName("UTF-8")));

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of raw byte stream of field
	public int rawLength() {
		return length.rawLength() + s.length();
	}
}