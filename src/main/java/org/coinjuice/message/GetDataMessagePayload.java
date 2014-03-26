package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.message.field.VariableLengthInteger;
import org.coinjuice.message.field.InventoryItem;
import org.coinjuice.exception.NonMatchingIntenoryVectorLengthException;
import org.coinjuice.exception.ToManyEntriesException;
import org.coinjuice.exception.UnknownObjectTypeException;

/** \class GetDataMessagePayload
*
* \brief Payload for GetData message
*
* Briefly the message and the methods on the message
*
*/
public class GetDataMessagePayload extends MessagePayload {

	// Number of inventory entries
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
		if(count.getValue() != inventory.length)
			new NonMatchingIntenoryVectorLengthException(count.getValue(), inventory.length);

		// Check that this does not exceed maximum number of entries
		if(count.getValue() > MAXIMUM_NUMBER_OF_ENTRIES)
			new ToManyEntriesException(count.getValue());
	}

	public GetDataMessagePayload(LittleEndianDataInputStream input) throws ToManyEntriesException, UnknownObjectTypeException, IOException {

		// count
		count = new VariableLengthInteger(input);

		// Check that this does not exceed maximum number of entries
		if(count.getValue() > MAXIMUM_NUMBER_OF_ENTRIES)
			new ToManyEntriesException(count.getValue());

		// Load entries
		for(int i = 0;i < count.getValue();i++)
			inventory[i] = new InventoryItem(input);
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		for(int i = 0;i < count.getValue(); i++)
			b.put(inventory[i].raw());

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Size of raw stream of field
	public int rawLength() {
		return count.rawLength() + (int)count.getValue()*InventoryItem.rawLength();
	}
}