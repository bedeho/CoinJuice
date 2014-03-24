package org.coinjuice;

import java.util.ArrayList;
import java.net.InetSocketAddress;

import org.coinjuice.ThinNode;
import org.coinjuice.FullNode;

public class CoinJuice {

	static ThinNode createThinNode() {

		// From https://blockchain.info/hub-nodes
		// https://blockchain.info/hub-nodes

		ArrayList<InetSocketAddress> peers = new ArrayList<InetSocketAddress>();
		
		peers.add(new InetSocketAddress("176.9.104.178", 8883));

		return new ThinNode(peers);
	}

	static FullNode createFullNode() {
		return new FullNode();
	}

}