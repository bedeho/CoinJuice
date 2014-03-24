package org.coinjuice;

import org.coinjuice.CoinJuice;

public class Main { 

	public static void main(String [] args) {

		//  javac coinjuice/*.java coinjuice/exception/*.java coinjuice/message/*.java coinjuice/message/field/*.java 

		// Compile
		//  javac -Xlint:unchecked coinjuice/*.java coinjuice/exception/*.java coinjuice/message/*.java coinjuice/message/field/*.java 

		// Run
		// java coinjuice.Main

		// Seting up thin node
		ThinNode n = CoinJuice.createThinNode();

		// Connect node to network
		n.joinNetwork();

	}
}