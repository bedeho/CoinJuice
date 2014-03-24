package org.coinjuice.exception;

// Used to notify when BlockMessage constructor receives previous bloch hash of incorrect length
public class IncorrectPreviousBlockHashLengthException extends Exception {
	
	int prev_block_hash_length;

	public IncorrectPreviousBlockHashLengthException(int prev_block_hash_length) {

		super("The length of a hash of previous block must be 32 bytes, but was: " + prev_block_hash_length);

		this.prev_block_hash_length = prev_block_hash_length;
	}
}