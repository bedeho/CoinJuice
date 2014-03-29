package org.coinjuice.message.field;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.Tx;
import org.coinjuice.exception.IncorrectPreviousBlockHashLengthException;
import org.coinjuice.exception.IncorrectMerkleRootLengthException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;
import org.coinjuice.Util;

public class MerkleBlock {

	// Block version information, based upon the software version creating this block
	private int version;

	// The hash value of the previous block this particular block references
	private char[] prev_block;

	// The reference to a Merkle tree collection which is a hash of all transactions related to this block
	private char [] merkle_root;

	// A unix timestamp recording when this block was created (Currently limited to dates before the year 2106!)
	private int timestamp;

	// The calculated difficulty target being used for this block
	private int bits;

	// The nonce used to generate this block… to allow variations of the header and compute different hashes
	private int nonce;

	// Number of transaction entries
	private VariableLengthInteger txn_count;

	// Block transactions, in format of tx command
	private Tx[] txns;

	// Constructors
	public MerkleBlock(int version, char[] prev_block, char [] merkle_root, int timestamp, int bits, int nonce, VariableLengthInteger txn_count, Tx[] txns)  throws IncorrectPreviousBlockHashLengthException, IncorrectMerkleRootLengthException, IncorrectNumberOfTransactionsException {

		// Set message payload fields
		this.version = version;
		this.prev_block = prev_block;
		this.merkle_root = merkle_root;
		this.timestamp = timestamp;
		this.bits = bits;
		this.nonce = nonce;
		this.txn_count = txn_count;
		this.txns = txns;

		// Check if previous block hash is of correct length
		if(prev_block.length != 32)
			throw new IncorrectPreviousBlockHashLengthException(prev_block.length);

		// Check if merkle root hash is of correct length
		if(merkle_root.length != 32)
			throw new IncorrectMerkleRootLengthException(merkle_root.length);

		// Check if the stated number of transactions matches the actual number of transactions
		if(txn_count.getValue() == txns.length)
			throw new IncorrectNumberOfTransactionsException(txn_count.getValue());

	}

	public MerkleBlock(LittleEndianDataInputStream input) throws ToManyEntriesException, IOException {

		// version
		version = input.readInt();

		// prev_block
		prev_block = Util.readChar(input, 32);

		// merkle_root
		merkle_root = Util.readChar(input, 32);

		// timestamp
		timestamp = input.readInt();

		// bits
		bits = input.readInt();

		// nonce
		nonce = input.readInt();

		// txn_count
		txn_count = new VariableLengthInteger(input);

		// Check that txn_count > 0 ?

		// txns
		for(int i = 0;i < txn_count.getValue();i++)
			txns[i] = new Tx(input);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.putInt(version);
		Util.writeChar(b, prev_block);
		Util.writeChar(b, merkle_root);
		b.putInt(timestamp);
		b.putInt(nonce);
		b.put(txn_count.raw());

		for(int i = 0;i < txn_count.getValue();i++)
			b.put(txns[i].raw());

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public int rawLength() {

		int length = 4 + 32 + 32 + 4 + 4 + txn_count.rawLength();

		for(int i = 0;i < txn_count.getValue();i++)
			length += txns[i].rawLength();

		return length;
	}
}