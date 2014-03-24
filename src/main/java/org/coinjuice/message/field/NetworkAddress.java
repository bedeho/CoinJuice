package org.coinjuice.message.field;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.field.Services;
import org.coinjuice.exception.VersionIncompatibleActionException;

public class NetworkAddress {

	// uint32	 the Time (version >= 31402)
	private int time;

	private static final int VERSION_SPLIT_NR = 31402;

	// uint64_t	 same service(s) listed in version
	private Services services;

	// IPv6/4	 char[16]	 IPv6 address. Network byte order. The original client only supports IPv4 and only reads the last 4 bytes to get the IPv4 address. However, the IPv4 address is written into the message as a 16 byte IPv4-mapped IPv6 address
	// 12 bytes 00 00 00 00 00 00 00 00 00 00 FF FF, followed by the 4 bytes of the IPv4 address).
	private char[] IPv6; // = new char[16];

	// port number, network byte order
	private short port;

	// Identifies protocol version being used by the node, gives
	// raw() function different behaviour.
	private int version;

	public NetworkAddress(int version, Services services, char[] IPv6, short port) throws VersionIncompatibleActionException {

		// Check that version < VERSION_SPLIT_NR
		if(version >= VERSION_SPLIT_NR)
			throw new VersionIncompatibleActionException("Wrong constructor called, version " + version + " is above " + VERSION_SPLIT_NR + ", therefor time field is required.");
		
		this.version = version;
		this.services = services;
		this.IPv6 = IPv6;
		this.port = port;
		
	}

	public NetworkAddress(int version, int time, Services services, char[] IPv6, short port) throws VersionIncompatibleActionException {

		// Check that version >= VERSION_SPLIT_NR
		if(version < VERSION_SPLIT_NR)
			throw new VersionIncompatibleActionException("Wrong constructor called, version " + version + " is below " + VERSION_SPLIT_NR + ", therefor time field should be absent.");
		
		this.version = version;
		this.time = time;
		this.services = services;
		this.IPv6 = IPv6;
		this.port = port;

	}

	public NetworkAddress(int version, ByteBuffer b) {

		// Save version
		this.version = version;

		// Load time field if the version number is high enough
		if(version >= VERSION_SPLIT_NR)
			time = b.getInt();

		// Services
		services = new Services(b);

		// IPv6/4 field
		IPv6 = new char[16]; // allocate buffer
		b.asCharBuffer().get(IPv6); // read into buffer through view
		b.position(b.position() + 16); // advance original buffer position

		// Port field, in big endian
		b.order(ByteOrder.BIG_ENDIAN);
		port = b.getShort();
		b.order(ByteOrder.LITTLE_ENDIAN);

	}

	public ByteBuffer raw() {

		// Allocate buffer size based on version
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Save time field in this case
		if(version >= VERSION_SPLIT_NR)
			b.putInt(time);

		// Services field
		b.put(services.raw());
		
		// IPv6/4
		b.asCharBuffer().put(this.IPv6); // write address
		b.position(b.position() + 16); // advance original buffer position

		//  Port field in big endian
		b.order(ByteOrder.BIG_ENDIAN).putShort(this.port).order(ByteOrder.LITTLE_ENDIAN);
		
		// Return rewinded buffer
		b.rewind();

		// Return bufffer
		return b;

	}

	public static int rawLength(int version) {

		if(version < VERSION_SPLIT_NR)
			return 8+16+2; // NETWORK_ADDRESS_SIZE_NO_TIME_FIELD
		else 
			return 4+8+16+2; // NETWORK_ADDRESS_SIZE
	}

	public int rawLength() {
		return rawLength(version);
	}
	
}