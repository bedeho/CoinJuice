package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.exception.ToFewNumberOfTransactionsException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;

public class BlockHeader {

	// Block version information, based upon the software version creating this block
	int version;

	// The hash value of the previous block this particular block references
	char [] prev_block;

	// The reference to a Merkle tree collection which is a hash of all transactions related to this block
	char [] merkle_root;

	// A timestamp recording when this block was created (Will overflow in 2106[2])
	int timestamp;

	// The calculated difficulty target being used for this block
	int bits;

	// The nonce used to generate this block… to allow variations of the header and compute different hashes
	int nonce;

	// Number of transaction entries, this value is always 0
	VariableLengthInteger txn_count;

	public BlockHeader(int version, char [] prev_block, char [] merkle_root, int timestamp, int bits, int nonce, VariableLengthInteger txn_count) throws ToFewNumberOfTransactionsException {

		this.version = version;
		this.prev_block = prev_block;
		this.merkle_root = merkle_root;
		this.timestamp = timestamp;
		this.bits = bits;
		this.nonce = nonce;
		this.txn_count = txn_count;

		// Check that there is atleast 1 transaction
		if(txn_count.value > 0)
			throw new ToFewNumberOfTransactionsException(txn_count.value);

	}

	public BlockHeader(ByteBuffer b) throws IncorrectNumberOfTransactionsException {

		// Version field
		version = b.getInt();

		// Previous block hash field
		prev_block = new char[32]; // Allocate buffer
		b.asCharBuffer().get(prev_block); // Read through char array
		b.position(b.position() + 32); // Advance buffer position

		// Merkle tree hash
		merkle_root = new char[32]; // Allocate buffer
		b.asCharBuffer().get(merkle_root); // Read through char array
		b.position(b.position() + 32); // Advance buffer position

		// UNIX timestamp
		timestamp = b.getInt();

		// Difficulty of block
		bits = b.getInt();

		// Nonce of block
		nonce = b.getInt();

		// Number of transaction entries
		txn_count = new VariableLengthInteger(b);

		// Confirm number of transaction entries count being 0
		if(txn_count.value != 0)
			throw new IncorrectNumberOfTransactionsException(txn_count.value);

	}

	public ByteBuffer raw() {

		// Allocate byte buffer for fixed part of header, that is excluding txn_count
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Version field
		b.putInt(version);

		// Previous block hash field
		b.asCharBuffer().put(prev_block); // Write through char array
		b.position(b.position() + 32); // Advance buffer position

		// Merkle tree hash
		b.asCharBuffer().put(merkle_root); // Write through char array
		b.position(b.position() + 32); // Advance buffer position

		// UNIX timestamp
		b.putInt(timestamp);

		// Difficulty of block
		b.putInt(bits);

		// Nonce of block
		b.putInt(nonce);

		// Number of transaction entries
		b.put(txn_count.raw());

		// Rewind buffer
		b.rewind();

		// Return bufffer
		return b;
	}

	// Header size
	public static int rawLength() {
		return 4+32+32+4+4+4+1;
	}

}