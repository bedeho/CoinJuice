package org.coinjuice.exception;

// Used to notify when TxIn and TxOut constructor gets script and script lenth indicator of contradictoyr length

public class IncorrectScriptLengthException extends Exception {
	
	long script_length_counter;
	int actual_signature_script_length;

	public IncorrectScriptLengthException(long script_length_counter, int actual_signature_script_length) {

		super("The script length counter was " + script_length_counter + ", but the actual script length was: " + actual_signature_script_length);

		this.script_length_counter = script_length_counter;
		this.actual_signature_script_length = actual_signature_script_length;
	}
}