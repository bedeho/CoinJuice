package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.IncorrectScriptLengthException;

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
		if(script_length.value != signature_script.length)
			throw new IncorrectScriptLengthException(script_length.value, signature_script.length);

		// Do we need to validate the script some how? 
	}

	public TxIn(ByteBuffer b) {

		// outpoint
		outpoint = new OutPoint(b);

		// script_length
		script_length = new VariableLengthInteger(b);

		// signature_script
		signature_script = new char[(int)script_length.value];
		b.asCharBuffer().get(signature_script); // Read through char view
		b.position(b.position() + (int)script_length.value); // Advance position in byte buffer

	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.put(outpoint.raw());
		b.put(script_length.raw());

		b.asCharBuffer().put(signature_script); // Write to buffer through view
		b.position(b.position() + (int)signature_script.length); // Advance underlying buffer position

		b.putInt(sequence);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;

	}

	// Size of field
	public int rawLength() {
		return OutPoint.rawLength() + script_length.rawLength() + (int)script_length.value*1 + 4;
	}

}