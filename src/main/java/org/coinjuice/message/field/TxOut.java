package org.coinjuice.message.field;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.IncorrectScriptLengthException;
import org.coinjuice.Util;

public class TxOut {

	// Transaction Value
	private long value;
	
	// Length of the pk_script
	private VariableLengthInteger pk_script_length;

	// Usually contains the public key as a BitCoin script setting up conditions to claim this output.
	private char[] pk_script;

	// Constructor
	public TxOut(long value, VariableLengthInteger pk_script_length, char[] pk_script) throws IncorrectScriptLengthException {

		this.value = value;
		this.pk_script_length = pk_script_length;
		this.pk_script = pk_script;

		// Check that count matches inventory vector
		if(pk_script_length.getValue() != pk_script.length)
			throw new IncorrectScriptLengthException(pk_script_length.getValue(), pk_script.length);

		// Do we need to validate the script some how? 
	}

	public TxOut(LittleEndianDataInputStream input) throws IOException {

		// outpoint
		value = input.readLong();

		// pk_script
		pk_script_length = new VariableLengthInteger(input);

		// pk_script
		pk_script = Util.readChar(input, pk_script_length.getValue());
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putLong(value);
		b.put(pk_script_length.raw());
		Util.writeChar(b, pk_script);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of raw byte stream of field
	public int rawLength() {
		return 8 + pk_script_length.rawLength() + pk_script_length.getValue()*1;
	}
}