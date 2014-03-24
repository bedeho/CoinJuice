package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class MessagePayload {

	// Computes raw payload from fields
	abstract public ByteBuffer raw();

	// Compute the raw length of the payload
	abstract public int rawLength();

	// Computes payload hash, used as checksum
	public byte [] computeChecksum() {

		byte [] payload = raw().array();

		// Allocate checksum buffer
		byte [] computedChecksum = new byte[4];

		// Compute payload hash
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// Do double hashing
			byte[] hash = md.digest(md.digest(payload));

			// Save in header
			computedChecksum = Arrays.copyOfRange(hash, 0, 3);

		} catch(NoSuchAlgorithmException e) {
			// can't happen
		}

		// Return checksum
		return computedChecksum;
	}

}