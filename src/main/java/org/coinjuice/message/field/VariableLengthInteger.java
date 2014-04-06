package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VariableLengthInteger {

	// Size of this variable integer in bytes
	private int size;

	// Actual long value
	private long value;

	// Table of prefixes in variable length integer field, and what it means

	// VALUES 			LENGTH (byte)	code (hex)	JAVA 2 byte twos compelement representation
	// ----------------------------------------------------------------------------------------
	// < 0xfd	 		1	 			none
	// <= 0xffff	 	3	 			0xfd 		-3
	// <= 0xffffffff	5	 			0xfe 		-2
	// -	 			9	 			0xff 		-1
	
	// The one byte data prefix is larger than max JAVA signed byte/char,
	// and is represented by negative numbers due to twos complement representation used.
	// Values were converted using http://www.binaryconvert.com/result_signed_char.html

	private final static byte PREFIX_TWO_BYTE = -3;
	private final static byte PREFIX_FOUR_BYTE = -2;
	private final static byte PREFIX_EIGHT_BYTE = -1;

	public VariableLengthInteger(ByteBuffer b) {

		byte firstByte = b.get();

		if(firstByte == PREFIX_TWO_BYTE) {

			size = 2;
			value = b.getShort();

		} else if(firstByte == PREFIX_FOUR_BYTE) {

			size = 4;
			value = b.getInt();

		} else if(firstByte == PREFIX_EIGHT_BYTE) {

			size = 8;
			value = b.getLong();

		}
		else {
			
			// ONE BYTE
			size = 1;
			value = b.get();

		}

	}

	// Automatically set correct size
	public VariableLengthInteger(long value) {

		// Save value
		this.value = value;
		
		// Figure out correct size
		// Java only has unsigned, so we must transform max vals
		if(value <= 2*Byte.MAX_VALUE + 1) // 2^8 - 1 = 254
			this.size = 1;
		else if(value <= 2*Short.MAX_VALUE + 1) // 2^16 - 1 = 65535
			this.size = 2;
		else if(value <= 2*Short.MAX_VALUE + 1) // 2^32 - 1 = 4294967295
			this.size = 4;
		else // 2^64 - 1 = 1.8446744e+19
			this.size = 8;

	}

	// Computes raw data from fields
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		if(size == 1)
			b.put((byte)value);
		else {

			if(size == 2)
				b.put(PREFIX_TWO_BYTE).putShort((short)value);
			else if(size == 4)
				b.put(PREFIX_FOUR_BYTE).putInt((int)value);
			else // (size == 8)
				b.put(PREFIX_EIGHT_BYTE).putLong(value);
		}

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of raw data stream of field
	public int rawLength() {

		if(size == 1) 
			return 1;
		else
			return size+1;
	}
	
	// Return int version of variable
	public int getValue() {
		return (int)value;
	}
}