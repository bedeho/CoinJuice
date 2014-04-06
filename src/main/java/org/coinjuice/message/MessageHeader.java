package org.coinjuice.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.coinjuice.message.MessageType;
import org.coinjuice.exception.UnknownMagicValueException;
import org.coinjuice.exception.UnknownMessageTypeException;
import org.coinjuice.Util;

/** \class MessageHeader
*
* \brief Represents the header of messages
*
* A more detailed class description...
*
*/
public class MessageHeader {

	// Magic value indicating message origin network, and used to seek to next message when stream state is unknown
	public enum Magic {

		// NETWORK 		VALUE (hex)		JAVA 4 byte twos compelement representation
		// ----------------------------------------
		// main	 		0xD9B4BEF9 		-642466055
		// testnet		0xDAB5BFFA 		-625623046
		// testnet3 	0x0709110B 		118034699
		// namecoin 	0xFEB4BEF9 		-21709063

		// The four byte data to be sent are larger than max JAVA signed int,
		// and is represented by negative numbers due to twos complement representation used.
		// Values were converted using http://www.binaryconvert.com/result_signed_int.html

		MAIN(-642466055),
		TESTNET(-625623046), 
		TESTNET3(118034699), 
		NAMECOIN(-21709063); 

		private final int value;

		private Magic(int value) {
			this.value = value;
		}

		int getValue() {
			return value;
		}

		static Magic getMagic(int val) throws UnknownMagicValueException {

			for (Magic m: values())
            	if (m.getValue() == val)
                	return m;

            throw new UnknownMagicValueException(val);
		}
	}

	protected Magic magic;

	// Command field encoding message type
	protected MessageType command;

	// Length of payload in number of bytes
	protected int length;

	// First 4 bytes of sha256(sha256(payload))
	protected byte[] checksum;

	// Constructor
	public MessageHeader(Magic magic, MessageType command, int length, byte [] checksum) {

		this.magic = magic;
		this.command = command;
		this.length = length;
		this.checksum = checksum;
	}

	public MessageHeader(ByteBuffer b) throws UnknownMagicValueException, UnknownMessageTypeException {

		// magic
		magic = Magic.getMagic(b.getInt());

		// command
		byte [] commandBuffer = new byte[12];
		b.get(commandBuffer);

		try {
			command = MessageType.getMessageType(new String(commandBuffer, "UTF-8"));
		} catch(UnsupportedEncodingException e) {
			// can't happen, we are manually providing it
		}

		// length
		length = b.getInt();

		// checksum
		checksum = new byte[4];
		b.get(checksum);
	}

	// Computes raw byte stream for entire message
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate with raw header
		b.putInt(magic.getValue());
		Util.writeChar(b, command.getValue().toCharArray());
		b.putInt(length);
		b.put(checksum);

		// Return rewinded buffer
		b.rewind();

		// Return buffer
		return b;
	}

	public static int rawLength() {
		return 4+12+4+4;
	}

	public byte[] getChecksumField() {
		return checksum;
	}

	public int getLengthField() {
		return length;
	}

	public MessageType getCommandField() {
		return command;
	}
}