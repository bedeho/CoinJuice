package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.Tx;

import org.coinjuice.exception.IncorrectPreviousBlockHashLengthException;
import org.coinjuice.exception.IncorrectMerkleRootLengthException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;
/*
import org.coinjuice.exception.IncorrectChecksumException;
import org.coinjuice.exception.IncorrectPayloadLengthException;
import org.coinjuice.exception.IncorrectCommandException;
import org.coinjuice.exception.UnknownMagicValueException;
*/

public class Block {

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

	// Block transactions, in format of "tx" command
	private Tx[] txns;

	// Constructors
	public Block(int version, char[] prev_block, char [] merkle_root, int timestamp, int bits, int nonce, VariableLengthInteger txn_count, Tx[] txns)  throws IncorrectPreviousBlockHashLengthException, IncorrectMerkleRootLengthException, IncorrectNumberOfTransactionsException {

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
		if(txn_count.value == txns.length)
			throw new IncorrectNumberOfTransactionsException(txn_count.value);

	}

	public Block(ByteBuffer b) throws ToManyEntriesException {

		// version
		version = b.getInt();

		// prev_block
		prev_block = new char [32];
		b.asCharBuffer().get(prev_block);
		b.position(b.position() + 32);

		// merkle_root
		merkle_root = new char [32];
		b.asCharBuffer().get(merkle_root);
		b.position(b.position() + 32); 

		// timestamp
		timestamp = b.getInt();

		// bits
		bits = b.getInt();

		// nonce
		nonce = b.getInt();

		// txn_count
		txn_count = new VariableLengthInteger(b);

		// Check that txn_count > 0 ?

		// txns
		for(int i = 0;i < txn_count.value;i++)
			txns[i] = new Tx(b);

	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.putInt(version);

		b.asCharBuffer().put(prev_block);
		b.position(b.position() + prev_block.length);

		b.asCharBuffer().put(merkle_root);
		b.position(b.position() + merkle_root.length);

		b.putInt(timestamp);
		b.putInt(nonce);
		b.put(txn_count.raw());

		for(int i = 0;i < txn_count.value;i++)
			b.put(txns[i].raw());

		// Return rewinded buffer
		b.rewind();

		// Return bufffer
		return b;
	}

	public int rawLength() {

		int length = 4 + 32 + 32 + 4 + 4 + txn_count.rawLength();

		for(int i = 0;i < txn_count.value;i++)
			length += txns[i].rawLength();

		return length;

	}

}