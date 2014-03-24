package org.coinjuice.exception;

// Is it appropriate to make an exception just for a constructor issue?
// Should this be put in name some how?

// Used CoinJuice constructor when it receives a nodemode value which does not match

public class UnknownNodeModeException extends Exception {
	
	int val;

	public UnknownNodeModeException(int val) {

		super("Unknown NodeMode value: " + val);

		this.val = val;
		
	}
}