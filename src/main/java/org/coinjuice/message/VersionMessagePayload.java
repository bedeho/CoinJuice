package org.coinjuice.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.Services;
import org.coinjuice.message.field.NetworkAddress;
import org.coinjuice.message.field.VariableLengthString;
import org.coinjuice.exception.IncorrectVersionNumberForVersionMessageException;

/** \class VerackMessagePayload
*
* \brief Represents payload for verack message
*
* A more detailed class description...
*
*/
public class VersionMessagePayload extends MessagePayload {

	// Identifies protocol version being used by the node
	private int version;

	// bitfield of features to be enabled for this connection
	private Services services;

	// standard UNIX timestamp in seconds
	private long timestamp;

	// The network address of the node receiving this message
	private NetworkAddress addr_recv;

	// For version >= 106, then next five fields are also used.
	final int VERSION_SPLIT_NR = 106;
	final int VERSION_SPLIT_NR_RELAY = 70001;

	// The network address of the node emitting this message
	private NetworkAddress addr_from;

	// Node random nonce, randomly generated every time a version packet is sent. This nonce is used to detect connections to self.
	private long nonce;

	// User Agent (0x00 if string is 0 bytes long)
	private VariableLengthString user_agent;

	// The last block received by the emitting node
	private int start_height;

	// Whether the remote peer should announce relayed transactions or not, see BIP 0037, since version >= 70001
	private boolean relay;

	// Constructors
	public VersionMessagePayload(int version, Services services, long timestamp, NetworkAddress addr_recv) throws IncorrectVersionNumberForVersionMessageException  {

		// Check if version agrees with constructor
		if(version >= VERSION_SPLIT_NR)
			throw new IncorrectVersionNumberForVersionMessageException(version, "Incorrect version number, must be < " + VERSION_SPLIT_NR + " to create shortest version message.");

		// Set message payload fields
		this.version = version;
		this.services = services;
		this.timestamp = timestamp;
		this.addr_recv = addr_recv;
	}

	public VersionMessagePayload(int version, Services services, long timestamp, NetworkAddress addr_recv, NetworkAddress addr_from, long nonce, VariableLengthString user_agent, int start_height) throws IncorrectVersionNumberForVersionMessageException {

		// Check if version agrees with constructor
		if(version < VERSION_SPLIT_NR)
			throw new IncorrectVersionNumberForVersionMessageException(version, "Incorrect version number, must be >= " + VERSION_SPLIT_NR + " to create long version message.");
		else if(version >= VERSION_SPLIT_NR_RELAY)
			throw new IncorrectVersionNumberForVersionMessageException(version, "Incorrect version number, must be < " + VERSION_SPLIT_NR_RELAY + " to not create longest version message.");

		// Set message payload fields
		this.version = version;
		this.services = services;
		this.timestamp = timestamp;
		this.addr_recv = addr_recv;
		this.addr_from = addr_from;
		this.nonce = nonce;
		this.user_agent = user_agent;
		this.start_height = start_height;
	}

	public VersionMessagePayload(int version, Services services, long timestamp, NetworkAddress addr_recv, NetworkAddress addr_from, long nonce, VariableLengthString user_agent, int start_height, boolean relay) throws IncorrectVersionNumberForVersionMessageException {

		// Check if version agrees with constructor
		if(version < VERSION_SPLIT_NR_RELAY)
			throw new IncorrectVersionNumberForVersionMessageException(version, "Incorrect version number, must be >= " + VERSION_SPLIT_NR_RELAY + " to create long version message.");

		// Set message payload fields
		this.version = version;
		this.services = services;
		this.timestamp = timestamp;
		this.addr_recv = addr_recv;
		this.addr_from = addr_from;
		this.nonce = nonce;
		this.user_agent = user_agent;
		this.start_height = start_height;
		this.relay = relay;
	}

	public VersionMessagePayload(ByteBuffer b) {

		// version
		version = b.getInt();

		// services
		services = new Services(b);

		// timestamp
		timestamp = b.getLong();

		// addr_recv
		addr_recv = new NetworkAddress(version, b);

		if(version >= VERSION_SPLIT_NR) {

			// The network address of the node emitting this message
			addr_from = new NetworkAddress(version, b);

			// Node random nonce, randomly generated every time a version packet is sent. This nonce is used to detect connections to self.
			nonce = b.getLong();

			// User Agent (0x00 if string is 0 bytes long)
			user_agent = new VariableLengthString(b);

			// The last block received by the emitting node
			start_height = b.getInt();

			// Whether the remote peer should announce relayed transactions or not, see BIP 0037, since version >= 70001
			if(version >= VERSION_SPLIT_NR_RELAY)
				relay = (b.get() != 0);
		}
	}

	// Produce raw version of message payload
	public ByteBuffer raw() {

		// Build  message which was the full message for version < 106
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN); // version, services, timestamp, addr_recv

		// Populate
		b.putInt(version);
		b.put(services.raw());
		b.putLong(timestamp);
		b.put(addr_recv.raw());

		if(version >= VERSION_SPLIT_NR) {

			b.put(addr_from.raw());
			b.putLong(nonce);
			b.put(user_agent.raw());
			b.putInt(start_height);

			// Return if message is of middle range
			if(version >= VERSION_SPLIT_NR_RELAY) {

				if(relay)
					b.put((byte)1);
				else
					b.put((byte)0);

			}
		}

		// Rewind buffer
		b.rewind();
		
		// Return buffer
		return b;
	}

	public int rawLength() {

		if(version < VERSION_SPLIT_NR)
			return (4 + 8 + 8 + addr_recv.rawLength());
		else if(version < VERSION_SPLIT_NR_RELAY)
			return (4 + 8 + 8 + addr_recv.rawLength()) + (addr_from.rawLength() + 8 + user_agent.rawLength() + 4);
		else // version >= VERSION_SPLIT_NR_RELAY
			return (4 + 8 + 8 + addr_recv.rawLength()) + (addr_from.rawLength() + 8 + user_agent.rawLength() + 4) + 1;
	}
}