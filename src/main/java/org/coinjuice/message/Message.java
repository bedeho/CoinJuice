package org.coinjuice.message;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.google.common.io.LittleEndianDataInputStream;

import org.coinjuice.exception.CountFieldToLargeException;
import org.coinjuice.exception.IncorrectNumberOfTransactionsException;
import org.coinjuice.exception.ToManyEntriesException;
import org.coinjuice.exception.UnknownMagicValueException;
import org.coinjuice.exception.UnknownMessageTypeException;
import org.coinjuice.exception.IncorrectPayloadLengthException;
import org.coinjuice.exception.IncorrectChecksumException;
import org.coinjuice.exception.UnImplemenetedMessageException;
import org.coinjuice.exception.UnknownObjectTypeException;

// Import all payloads
//import org.coinjuice.message.*;

/** \class Message
*
* \brief Represents the header and payload of a message
*
* A more detailed class description...
*
*/
public class Message {

	// Message components
	MessageHeader header;
	MessagePayload payload;

	// Constructor
	public Message(MessageHeader header, MessagePayload payload) throws IncorrectChecksumException, IncorrectPayloadLengthException {

		// Set fields
		this.header = header;
		this.payload = payload;

		// Check that header checksum and length fields match payload
		validateChecksumAndLengthFields();
	}

	public Message(LittleEndianDataInputStream input) throws UnknownMagicValueException, UnknownMessageTypeException, IncorrectChecksumException, IncorrectPayloadLengthException, IOException, UnImplemenetedMessageException {

		// Load header
		header = new MessageHeader(input);

		// Load payload
		payload = createMessagePayload(input, header.command);

		// Check that header checksum and length fields match payload
		validateChecksumAndLengthFields();
	}

	public static MessagePayload createMessagePayload(LittleEndianDataInputStream input, MessageType type) throws UnImplemenetedMessageException, CountFieldToLargeException, IOException, ToManyEntriesException, UnknownObjectTypeException, IncorrectNumberOfTransactionsException {
		
		MessagePayload p;
		
        switch(type) {

            case VERSION: p = new VersionMessagePayload(input); break;
            case VERACK: p = new EmptyMessagePayload(); break;
            case ADDR: p = new AddrMessagePayload(input); break;
            case INV: p = new InvMessagePayload(input); break;
            case GETDATA: p = new GetDataMessagePayload(input); break;
            case NOTFOUND: p = new NotFoundMessagePayload(input); break;
            case GETBLOCKS: p = new GetBlocksMessagePayload(input); break;
            case GETHEADERS: p = new GetHeadersMessagePayload(input); break;
            case TX: p = new TxMessagePayload(input); break;
            case BLOCK: p = new BlockMessagePayload(input); break;
            case HEADERS: p = new HeadersMessagePayload(input); break;
            case GETADDR: p = new EmptyMessagePayload(); break;
            case MEMPOOL: throw new UnImplemenetedMessageException(type);
            case CHECKORDER: throw new UnImplemenetedMessageException(type);
            case SUBMITORDER: throw new UnImplemenetedMessageException(type);
            case REPLY: throw new UnImplemenetedMessageException(type);
            case PING: p = new PingMessagePayload(input); break;
            case PONG: p = new PongMessagePayload(input); break;
            case FILTERLOAD: throw new UnImplemenetedMessageException(type);
            case FILTERADD: throw new UnImplemenetedMessageException(type);
            case FILTERCLEAR: p = new EmptyMessagePayload(); break;
            case MERKLEBLOCK: p = new MerkleBlockMessagePayload(type);
            case ALERT: throw new UnImplemenetedMessageException(type);
            
            default: // Unrecognized message, must be due to MessageType expansion, but not implemented as case in this switch.
            	throw new UnImplemenetedMessageException(type);
        }
        
        return p;
	}

	// Check that present checksum and length fields match payload
	protected void validateChecksumAndLengthFields() throws IncorrectChecksumException, IncorrectPayloadLengthException {

		// Payload
		byte [] p = payload.raw().array();

		// Compute checksum based on payload fields
		byte [] computedChecksum = payload.computeChecksum();

		// Checksum in payload
		byte [] checksumInHeader = header.getChecksumField();

		// Check computed checksum with checksum field
		if(checksumInHeader[0] != computedChecksum[0] || 
			checksumInHeader[1] != computedChecksum[1] ||
			checksumInHeader[2] != computedChecksum[2] ||
			checksumInHeader[3] != computedChecksum[3])
			throw new IncorrectChecksumException(checksumInHeader, computedChecksum);

		// Check length of payload with length field
		if(header.getLengthField() != p.length)
			throw new IncorrectPayloadLengthException(header.getLengthField(), p.length);	
	}

	// Computes raw byte stream for entire message
	public ByteBuffer raw() {

		// Allocate buffer
		ByteBuffer b = ByteBuffer.allocate(rawLength()).order(ByteOrder.LITTLE_ENDIAN);

		// Populate buffer
		b.put(header.raw());
		b.put(payload.raw());

		// Rewind buffer
		b.rewind();

		// Return buffer
		return b;
	}

	// Total raw length of message
	public int rawLength() {
		return MessageHeader.rawLength() + payload.rawLength();
	}

	// Get message header
	public MessageHeader getMessageHeader() {
		return header;
	}

	// Get message payload
	public MessagePayload getMessagePayload() {
		return payload;
	}
}