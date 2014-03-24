package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.exception.IncorrectLengthOfHashObjectException;
import org.coinjuice.exception.UnknownObjectTypeException;

// Is called InventoryVector by bitcoin spesification and also standard client,
// but this name is more appropriate

public class InventoryItem {

	// Identifies the object type linked to this inventory
	private ObjectType type;

	public enum ObjectType {

		// VALUE 	NAME		DESCRIPTION
		// ----------------------------------------
		// 0	 	ERROR	 	Any data of with this number may be ignored
		// 1	 	MSG_TX	 	Hash is related to a transaction
 		// 2	 	MSG_BLOCK	Hash is related to a data block

		ERROR(1),
		MSG_TX(2), 
		MSG_BLOCK(3); 

		private final int value;

		private ObjectType(int value) {

			this.value = value;
		}

		int getValue() {
			return value;
		}

		static ObjectType getObjectType(int val) throws UnknownObjectTypeException {

			for (ObjectType o: values())
            	if (o.getValue() == val)
                	return o;

            throw new UnknownObjectTypeException("Unknown object type value: " + val);

		}
	}

	// Hash of the object: 32 chars
	private char[] hash;

	public InventoryItem(ObjectType type, char [] hash) throws IncorrectLengthOfHashObjectException {

		if(hash.length != 32)
			throw new IncorrectLengthOfHashObjectException("The length of the provided hash is " + hash.length + ", but it should be 32.");

		this.type = type;
		this.hash = hash;

	}

	public InventoryItem(ByteBuffer b) throws UnknownObjectTypeException {

		// Type field
		type = ObjectType.getObjectType(b.getInt());

		// Hash field
		hash = new char[32]; // Allocate buffer space
		b.asCharBuffer().get(hash); // Read from char buffer
		b.position(b.position() + 32); // Advance position in byte buffer

	}

	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate
		b.putInt(type.getValue());
		b.asCharBuffer().put(hash); // rembemr to manually advance position on b if you want to add more stuff to b from hereonin

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;

	}

	// Size of raw byte stream of field
	public static int rawLength() {
		return 4+32;
	}

}