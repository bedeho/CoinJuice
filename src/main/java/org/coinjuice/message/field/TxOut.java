package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.IncorrectScriptLengthException;

public class TxOut {

	// Transaction Value
	private long value;
	
	// Length of the pk_script
	private VariableLengthInteger pk_script_length;

	// Usually contains the public key as a Bitcoin script setting up conditions to claim this output.
	private char[] pk_script;

	// Constructor
	public TxOut(long value, VariableLengthInteger pk_script_length, char[] pk_script) throws IncorrectScriptLengthException {

		this.value = value;
		this.pk_script_length = pk_script_length;
		this.pk_script = pk_script;

		// Check that count matches inventory vector
		if(pk_script_length.value != pk_script.length)
			throw new IncorrectScriptLengthException(pk_script_length.value, pk_script.length);

		// Do we need to validate the script some how? 
	}

	public TxOut(ByteBuffer b) {

		// outpoint
		value = b.getLong();

		// pk_script
		pk_script_length = new VariableLengthInteger(b);

		// pk_script
		pk_script = new char[(int)pk_script_length.value];
		b.asCharBuffer().get(pk_script); // Read through char view
		b.position(b.position() + (int)pk_script_length.value); // Advance position in byte buffer

	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putLong(value);
		b.put(pk_script_length.raw());

		b.asCharBuffer().put(pk_script); // Write to buffer through view
		b.position(b.position() + pk_script.length); // Advance underlying buffer position

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;

	}

	// Size of raw byte stream of field
	public int rawLength() {
		return 8 + pk_script_length.rawLength() + (int)pk_script_length.value*1;
	}

}