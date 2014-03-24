package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.TxIn;
import org.coinjuice.message.field.TxOut;

import org.coinjuice.exception.IncorrectNumberInputTransactionsException;
import org.coinjuice.exception.IncorrectNumberOutputTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;

public class Tx {

	// Transaction data format version
	private int version;

	// Number of Transaction inputs
	private VariableLengthInteger tx_in_count;

	// A list of 1 or more transaction inputs or sources for coins
	private TxIn[] tx_in;

	// Number of Transaction outputs
	private VariableLengthInteger tx_out_count;

	// A list of 1 or more transaction outputs or destinations for coins
	private TxOut[] tx_out;

	// The block number or timestamp at which this transaction is locked:
	// If all TxIn inputs have final (0xffffffff) sequence numbers then lock_time is irrelevant. Otherwise, the transaction may not be added to a block until after lock_time (see NLockTime).
	// 0 				Always locked
	// < 500 000 000	Block number at which this transaction is locked
	// >= 500 000 000 	UNIX timestamp at which this transaction is locked
	private int lock_time;

	// Constructor
	public Tx(int version, VariableLengthInteger tx_in_count, TxIn[] tx_in, VariableLengthInteger tx_out_count, TxOut[] tx_out, int lock_time) throws IncorrectNumberInputTransactionsException, IncorrectNumberOutputTransactionsException {

		// Set payload fields
		this.version = version;
		this.tx_in_count = tx_in_count;
		this.tx_in = tx_in;
		this.tx_out_count = tx_out_count;
		this.tx_out = tx_out;
		this.lock_time = lock_time;

		// Check that the number of input transactions in the counter matches the number provided
		if(tx_in_count.value != tx_in.length)
			throw new IncorrectNumberInputTransactionsException(tx_in_count.value, tx_in.length);

		// Check that the number of output transactions in the counter matches the number provided
		if(tx_out_count.value != tx_out.length)
			throw new IncorrectNumberInputTransactionsException(tx_out_count.value, tx_out.length);
	}

	public Tx(ByteBuffer b) throws ToManyEntriesException {

		// fields

		// version
		version = b.getInt();

		// tx_in_count
		tx_in_count = new VariableLengthInteger(b);

		// tx_in
		tx_in = new TxIn[(int)tx_in_count.value];

		for(int i = 0;i < tx_in_count.value;i++)
			tx_in[i] = new TxIn(b);

		// tx_out_count
		tx_out_count = new VariableLengthInteger(b);

		// tx_out
		tx_out = new TxOut[(int)tx_out_count.value];

		for(int i = 0;i < tx_out_count.value;i++)
			tx_out[i] = new TxOut(b);

		// lock_time
		lock_time = b.getInt();

	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.putInt(version);

		b.put(tx_in_count.raw());
		
		for(int i = 0;i < tx_in_count.value; i++)
			b.put(tx_in[i].raw());

		b.put(tx_out_count.raw());
		
		for(int i = 0;i < tx_out_count.value; i++)
			b.put(tx_out[i].raw());

		b.putInt(lock_time);

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;

	}

	public int rawLength() {

		int length = 4 + 4 + tx_in_count.rawLength() + tx_out_count.rawLength();

		// Add up size of inputs
		for(int i = 0;i < tx_in_count.value;i++)
			length += tx_in[i].rawLength();

		// Add up size of outputs
		for(int i = 0;i < tx_out_count.value;i++)
			length += tx_out[i].rawLength();

		return length;

	}

}