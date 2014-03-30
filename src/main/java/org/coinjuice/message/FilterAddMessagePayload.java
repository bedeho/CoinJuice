package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

public class FilterAddMessagePayload extends MessagePayload {
	
	// The data element to add to the current filter.
	byte [] data;
	
	private static int MAX_ELEMENT_SIZE_FIELD = 520;
	
	// Constructor
	public FilterAddMessagePayload(byte [] data) throws DataFieldToBigException {
		
		if(data.length > MAX_ELEMENT_SIZE_FIELD)
			throw new DataFieldToBigException(data.length);
		
		this.data = data;
	}

	public FilterAddMessagePayload(LittleEndianDataInputStream input) throws IOException {

		//  How do we know how many filters
		
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {
		
		// Allocate
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);
		
		// Populate
		b.put(data);
		
		// Return
		return b;
	}
	
	// Compute length of raw representation
	public int rawLength() {
		return data.length;
	}
}
