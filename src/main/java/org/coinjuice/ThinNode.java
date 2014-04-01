package org.coinjuice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.net.InetSocketAddress;

import org.coinjuice.message.field.Block;
import org.coinjuice.message.field.Tx;

import org.coinjuice.ThinNodeConnectionManager;

public class ThinNode {

	// Block chain presently accepted by this node
	private LinkedHashMap<String, Block> blockchain;

	// All unspent transactions in my version of block chain
	private HashMap<String, Tx> unspentTransactions;

	// Transactions this node is aware of which are not part of block chain it is aware of
	// use this as mempool
	private HashMap<String, Tx> unconfirmedTransactions;

	// relay set: messages recently inved by this node?
	
	// both mempool and relay set must be
	
	// List of peern nodes
	private ArrayList<InetSocketAddress> peers;

	// List of connection managers
	private ArrayList<ThinNodeConnectionManager> managers;

	// Constructors
	ThinNode(ArrayList<InetSocketAddress> peers) {
		this.peers = peers;
	}

	// Join BitCoin network
	void joinNetwork() {

		// Create array list for connection managers
		managers = new ArrayList<ThinNodeConnectionManager>();

		// Start new thin node for each peer
		for(int i = 0;i < peers.size();i++) {
			
			/*
			// Make new connection manager for this peer
			ThinNodeConnectionManager m = new ThinNodeConnectionManager(this, peers.get(i));

			// Add to manager pool
			managers.add(m);

			// Start manager
			m.start();
			*/
		}
	}

	void leaveNetwork() {

		// Close connections
		for(int i = 0;i < peers.size();i++)
			managers.get(i).endConnection();

		// Clear managers
		managers.clear();

		// Clear data
		blockchain.clear();
		unspentTransactions.clear();
		unconfirmedTransactions.clear();
	}
	
	// Connection managers deposit errors here
	void error() {
		
	}

	// Send money you control
	// REcv: Check that a transaction exists, its balance, and how deep it is in the blockchain

}
