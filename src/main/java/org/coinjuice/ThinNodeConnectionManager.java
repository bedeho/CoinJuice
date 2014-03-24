package org.coinjuice;

import java.net.Socket;
import java.net.InetSocketAddress;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;

import org.coinjuice.ThinNode;
import org.coinjuice.message.Message;

import org.coinjuice.message.MessageType;

/*
import org.coinjuice.message.MessageHeader;
import org.coinjuice.message.MessagePayload;
import org.coinjuice.message.field.Services;
import org.coinjuice.message.field.NetworkAddress;
*/

public class ThinNodeConnectionManager extends Thread {

    // Thin Node this manager maintains connection for
	private ThinNode node;

    // Address of peer
	private InetSocketAddress peer;

    // Socket used for this connection
    private Socket connection;

    // Version of protocol to be used
    private int version;

    // Data streams used for communication
    private DataInputStream input;
    private DataOutputStream output;

    // Processing state of connection


	// Constructors
	ThinNodeConnectionManager(ThinNode node, InetSocketAddress peer, int version) {

		this.node = node;
		this.peer = peer;
        this.version = version;
	}

	// Thread start point
	public void run()  {

        // Create socket and connect to peer
        try {
        
            connection = new Socket(peer.getAddress(), peer.getPort());

        } catch (IOException e) {
            // - if an I/O error occurs when creating the socket.
        }

        // Grab input and output streams
        try {

            input = new DataInputStream(connection.getInputStream());
            output = new DataOutputStream(connection.getOutputStream());

        } catch (IOException e) {
            // - if an I/O error occurs when opening data stream
        } 

        /*
        // Create version message
        VersionMessagePayload p = new MessagePayload(version, new Services(), timestamp, new NetworkAddress()); 
        MessageHeader h = new MessageHeader(MAIN, VERSION, p.rawLength(), p.getChecksumField());
        Message versionMessage = new Message(h,p);

        // Send version message
        output.write(versionMessage.raw());

        // Message processing loop
        while(true) { // <-- how to stop thread in good way

            // Read message
            Message m = new Message(input);

            // Process message
            processMessage(m);
        }
        */
	}

    // Processing logic
    private void processMessage(Message m) {

        // Check what message we received
        MessageType t = m.getMessageHeader().getCommandField();

        switch(t) {

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
    }

	// End connection: Not perfectly thread safe, but it doesnt matter
	public void endConnection() {
        // read this on stopping thread: http://docs.oracle.com/javase/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
	}

}
