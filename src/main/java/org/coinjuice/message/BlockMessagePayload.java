package org.coinjuice.message;

import java.nio.ByteBuffer;

import org.coinjuice.message.field.Tx;
import org.coinjuice.message.field.Block;
import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.IncorrectPreviousBlockHashLengthException;
import org.coinjuice.exception.IncorrectMerkleRootLengthException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;

/** \class BlockMessagePayload
*
* \brief Payload for Block message
*
* Briefly the message and the methods on the message
*
*/
public class BlockMessagePayload extends MessagePayload {

	// Block payload
	private Block block;

	// Constructors
	public BlockMessagePayload(int version, char[] prev_block, char [] merkle_root, int timestamp, int bits, int nonce, VariableLengthInteger txn_count, Tx[] txns)  throws IncorrectPreviousBlockHashLengthException, IncorrectMerkleRootLengthException, IncorrectNumberOfTransactionsException {
		this.block = new Block(version, prev_block, merkle_root, timestamp, bits, nonce, txn_count, txns);
	}

	public BlockMessagePayload(ByteBuffer b) throws ToManyEntriesException {
		block = new Block(b);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {
		return block.raw();
	}

	public int rawLength() {
		return block.rawLength();
	}

}