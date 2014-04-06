package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.NetworkAddress;
import org.coinjuice.exception.CountFieldToLargeException;

/** \class AddrMessagePayload
*
* \brief Payload for Addr messages. 
*
* Payload for Addr messages contains a list of time stamps and network addresses. Describe the version split at 31402
*
*/
public class AddrMessagePayload extends MessagePayload {

	// Identifies protocol version being used by the node
	// is required sine message has version dependency,
	// is not part of message payload
	private int version;

	// Number of address entries (max: 1000)
	private int count;

	final static int MAX_NUMBER_OF_ADDRESS_ENTRIES = 1000;
	
	// Address of other nodes on the network. version<209 will only read the first one. 
	// The uint32_t is a time stamp (see note below).
	private int [] addr_list_timestamp;
	private NetworkAddress [] addr_list_net_addr;

	// For version >= 31402, addresses are prefixed with timestamp
	final static int VERSION_SPLIT_NR = 31402;

	// Constructor
	public AddrMessagePayload(int version, int count, int [] addr_list_timestamp, NetworkAddress [] addr_list_net_addr) {

		this.version = version;
		this.count = count;
		this.addr_list_timestamp = addr_list_timestamp;
		this.addr_list_net_addr = addr_list_net_addr;
	}

	public AddrMessagePayload(ByteBuffer b) throws CountFieldToLargeException {

		// count
		count = b.getInt();

		if(count > MAX_NUMBER_OF_ADDRESS_ENTRIES)
			throw new CountFieldToLargeException(count);

		// Load address nodes
		addr_list_timestamp = new int[count];
		addr_list_net_addr = new NetworkAddress[count];

		for(int i = 0;i < count;i++) {

			if(version < VERSION_SPLIT_NR)
				addr_list_net_addr[i] = new NetworkAddress(version, b);
			else {
				
				addr_list_timestamp[i] = b.getInt();
				addr_list_net_addr[i] = new NetworkAddress(version, b);
			}
		}
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		for(int i = 0;i < count; i++) {

			// Timestamp
			if(version >= VERSION_SPLIT_NR)
			b.putInt(addr_list_timestamp[i]);

			// Network
			b.put(addr_list_net_addr[i].raw());
		}

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}
	
	// Compute length of raw representation
	public int rawLength() {

		if(version < VERSION_SPLIT_NR)
			return 4 + count*NetworkAddress.rawLength(version);
		else 
			return 4 + count*(4 + NetworkAddress.rawLength(version));
	}
}