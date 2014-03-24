package org.coinjuice.exception;

// Used to notify when BlockMessage constructor receives merkle root hash of incorrect length
@SuppressWarnings("serial")
public class IncorrectMerkleRootLengthException extends Exception {
	
	int merkle_root_hash_length;

	public IncorrectMerkleRootLengthException(int merkle_root_hash_length) {

		super("The length of a merkle root must be 32 bytes, but was: " + merkle_root_hash_length);

		this.merkle_root_hash_length = merkle_root_hash_length;
	}
}