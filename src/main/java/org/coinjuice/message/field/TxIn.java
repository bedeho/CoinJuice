package org.coinjuice.message.field;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.IncorrectScriptLengthException;
import org.coinjuice.Util;

public class TxIn {

	// The previous output transaction reference, as an OutPoint structure
	private OutPoint outpoint;
	
	// The length of the signature script
	private VariableLengthInteger script_length;

	// Computational Script for confirming transaction authorization
	private char[] signature_script;

	// Transaction version as defined by the sender. Intended for "replacement" of transactions when information is updated before inclusion into a block.
	// Didn't quite get this http://bitcoin.stackexchange.com/questions/2025/what-is-txins-sequence
	private int sequence;

	// Constructor
	public TxIn(OutPoint outpoint, VariableLengthInteger script_length, char[] signature_script, int sequence) throws IncorrectScriptLengthException {

		this.outpoint = outpoint;
		this.script_length = script_length;
		this.signature_script = signature_script;
		this.sequence = sequence;

		// Check that count matches inventory vector
		if(script_length.getValue() != signature_script.length)
			throw new IncorrectScriptLengthException(script_length.getValue(), signature_script.length);

		// Do we need to validate the script some how? 
	}

	public TxIn(LittleEndianDataInputStream input) throws IOException {

		// outpoint
		outpoint = new OutPoint(input);

		// script_length
		script_length = new VariableLengthInteger(input);

		// signature_script
		signature_script = Util.readChar(input, script_length.getValue());
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.put(outpoint.raw());
		b.put(script_length.raw());
		Util.writeChar(b, signature_script);
		b.putInt(sequence);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of field
	public int rawLength() {
		return OutPoint.rawLength() + script_length.rawLength() + script_length.getValue()*1 + 4;
	}
}