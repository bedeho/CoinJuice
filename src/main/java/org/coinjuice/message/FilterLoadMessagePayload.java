package org.coinjuice.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.io.LittleEndianDataInputStream;

public class FilterLoadMessagePayload extends MessagePayload {
	
	ADD VARINT< SIPA SAYS!!!>
	
	// The filter itself is simply a bit field of arbitrary byte-aligned size. The maximum size is 36,000 bytes
	byte [] filter;
	
	final static int MAX_FILTER_SIZE = 36000;
	
	// The number of hash functions to use in this filter. The maximum value allowed in this field is 50
	int nHashFuncs;
	
	final static int MAX_NUMBER_OF_HASH = 50;
	
	// A random value to add to the seed value in the hash function used by the bloom filter
	int nTweak;
	
	// A set of flags that control how matched items are added to the filter
	byte nFlags;
	
	// Constructor
	public FilterLoadMessagePayload(byte [] filter, int nHashFuncs, int nTweak, byte nFlags)  throws FilterToBigException, ToManyHashFunctionsException {
		
		if(filter.length > MAX_FILTER_SIZE)
			throw new FilterToBigException(filter.length);
			
		if(nHashFuncs > MAX_NUMBER_OF_HASH)
			throw new ToManyHashFunctionsException(nHashFuncs);
		
		this.filter = filter;
		this.nHashFuncs = nHashFuncs;
		this.nTweak = nTweak;
		this.nFlags = nFlags;
	}

	public FilterLoadMessagePayload(LittleEndianDataInputStream input) throws IOException {

		//  How do we know how many filters
		
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {
		
		// Allocate
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);
		
		// Populate
		b.put(filter);
		b.putInt(nHashFuncs);
		b.putInt(nTweak);
		b.put(nFlags);
		
		// Return
		return b;
	}
	
	// Compute length of raw representation
	public int rawLength() {
		return filter.length + 4 + 4 + 1;
	}
}
