package org.coinjuice.message;

import java.nio.ByteBuffer;

import org.coinjuice.message.field.Tx;
import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.TxIn;
import org.coinjuice.message.field.TxOut;
import org.coinjuice.exception.IncorrectNumberInputTransactionsException;
import org.coinjuice.exception.IncorrectNumberOutputTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;

/** \class TxMessagePayload
*
* \brief Represents payload for tx message
*
* A more detailed class description...
*
*/
public class TxMessagePayload extends MessagePayload {

	// Transaction data format version
	private Tx transaction;

	// Constructor
	public TxMessagePayload(int version, VariableLengthInteger tx_in_count, TxIn[] tx_in, VariableLengthInteger tx_out_count, TxOut[] tx_out, int lock_time) throws IncorrectNumberInputTransactionsException, IncorrectNumberOutputTransactionsException {
		transaction = new Tx(version, tx_in_count, tx_in, tx_out_count, tx_out, lock_time);
	}

	public TxMessagePayload(ByteBuffer b) throws ToManyEntriesException {
		transaction = new Tx(b);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {
		return transaction.raw();
	}

	// Size of raw version of message payload
	public int rawLength() {
		return transaction.rawLength();
	}

}