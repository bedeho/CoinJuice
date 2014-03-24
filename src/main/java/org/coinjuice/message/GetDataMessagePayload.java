package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.InventoryItem;

import org.coinjuice.exception.NonMatchingIntenoryVectorLengthException;
import org.coinjuice.exception.ToManyEntriesException;
import org.coinjuice.exception.UnknownObjectTypeException;

public class GetDataMessagePayload extends MessagePayload {

	// Number of inventory entires
	private VariableLengthInteger count;

	// Inventory vectors
	private InventoryItem[] inventory;

	// The maximum number of entries per message
	static final int MAXIMUM_NUMBER_OF_ENTRIES = 50000;

	// Constructor
	public GetDataMessagePayload(VariableLengthInteger count, InventoryItem[] inventory) throws NonMatchingIntenoryVectorLengthException, ToManyEntriesException {

		// Set payload fields
		this.count = count;
		this.inventory = inventory;

		// Check that count matches inventory vector
		if(count.value != inventory.length)
			new NonMatchingIntenoryVectorLengthException(count.value, inventory.length);

		// Check that this does not exceed maximum number of entries
		if(count.value > MAXIMUM_NUMBER_OF_ENTRIES)
			new ToManyEntriesException(count.value);
	}

	public GetDataMessagePayload(ByteBuffer b) throws ToManyEntriesException, UnknownObjectTypeException {

		// count
		count = new VariableLengthInteger(b);

		// Check that this does not exceed maximum number of entries
		if(count.value > MAXIMUM_NUMBER_OF_ENTRIES)
			new ToManyEntriesException(count.value);

		// Load entries
		for(int i = 0;i < count.value;i++)
			inventory[i] = new InventoryItem(b);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		for(int i = 0;i < count.value; i++)
			b.put(inventory[i].raw());

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of raw stream of field
	public int rawLength() {
		return count.rawLength() + (int)count.value*InventoryItem.rawLength();
	}
}