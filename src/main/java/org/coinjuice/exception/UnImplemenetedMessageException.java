package org.coinjuice.exception;

import org.coinjuice.message.MessageType;

@SuppressWarnings("serial")
public class UnImplemenetedMessageException extends Exception {

	MessageType type;
	
	public UnImplemenetedMessageException(MessageType type) {

		super("Received message of following type is not supported: " + type.getValue());
		
		this.type = type;
		
	}
}