package org.coinjuice.message;

import org.coinjuice.exception.UnknownMessageTypeException;

/** \class MessageType
*
* \brief Represents all the message types
*
* A more detailed class description...
*
*/
// Enum covering all messages types in protocol
public enum MessageType {

	VERSION("version     "),
	VERACK("verack      "),
	ADDR("addr        "),
	INV("inv         "),
	GETDATA("getdata     "),
	NOTFOUND("notfound    "),
	GETBLOCKS("getblocks   "),
	GETHEADERS("getheaders  "),
	TX("tx          "),
	BLOCK("block       "),
	HEADERS("headers     "),
	GETADDR("getaddr     "),
	MEMPOOL("mempool     "),
	CHECKORDER("checkorder  "),
	SUBMITORDER("submitorder "),
	REPLY("reply       "),
	PING("ping        "),
	PONG("pong        "),
	FILTERLOAD("filterload  "),
	FILTERADD("filteradd   "),
	FILTERCLEAR("filterclear "),
	MERKLEBLOCK("merkleblock "),
	ALERT("alert       ");

	private final String command;

	MessageType(String command) {
		this.command = command;
	}

	// Get value of this enum instance
	public String getValue() {
		return command;
	}

	// Recover enum from value
	public static MessageType getMessageType(String command) throws UnknownMessageTypeException {

		for (MessageType m: values())
        	if (m.getValue().equals(command))
            	return m;

        throw new UnknownMessageTypeException(command);
	}

/*
	// Get encoding of some enum instance :zero padding to 12 chars
	public static String getGetMessageTypeEncoding(MessageType m) {
		//return (m.getValue() + (new String(new char[12 - m.getValue().length()]))).toCharArray();
		return (m.getValue() + new String(new char[12 - m.getValue().length()]));
	}
	*/
}