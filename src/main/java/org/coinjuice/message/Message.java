package org.coinjuice.message;

import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.Arrays;

import org.coinjuice.exception.UnknownMagicValueException;
import org.coinjuice.exception.UnknownMessageTypeException;
import org.coinjuice.exception.IncorrectPayloadLengthException;
import org.coinjuice.exception.IncorrectChecksumException;

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

	public Message(LittleEndianDataInputStream input) throws UnknownMagicValueException, UnknownMessageTypeException, IncorrectChecksumException, IncorrectPayloadLengthException, IOException {

		// Load header
		header = new MessageHeader(input);

		// Load payload
		payload = createMessagePayload(input, header.command) new MessagePayload(input);

		

		// Add check that packet actually has the size it claims?
		//if(b.remaining() != length)
		//	throw new IncorrectPayloadLengthException(b.remaining(), length);

		

		// Check that header checksum and length fields match payload
		validateChecksumAndLengthFields();

		
	}

	public static MessagePayload createMessagePayload(LittleEndianDataInputStream input, MessageType type) {
		
		MessagePayload p;
		
        switch(type) {

            case VERSION: break;
            case VERACK: break;
            case ADDR: break;
            case INV: break;
            case GETDATA: break;
            case NOTFOUND: break;
            case GETBLOCKS: break;
            case GETHEADERS: break;
            case TX: break;
            case BLOCK: break;
            case HEADERS: break;
            case GETADDR: break;
            case MEMPOOL: break;
            case CHECKORDER: break;
            case SUBMITORDER: break;
            case REPLY: break;
            case PING: break;
            case PONG: break;
            case FILTERLOAD: break;
            case FILTERADD: break;
            case FILTERCLEAR: break;
            case MERKLEBLOCK: break;
            case ALERT: break;
        }
        
        // Return payload
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
		return header.rawLength() + payload.rawLength();
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