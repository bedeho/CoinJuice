package org.coinjuice.exception;

// Used to notify when TxOut constructor gets transaction counter and number of transactions

public class IncorrectNumberOutputTransactionsException extends Exception {
	
	long statedNumberOfTransactions;
	int actualNumberOfTransactions;

	public IncorrectNumberOutputTransactionsException(long statedNumberOfTransactions, int actualNumberOfTransactions) {

		super("The stated number of output transactions was " + statedNumberOfTransactions + ", but the actual number of input transactions was: " + actualNumberOfTransactions);

		this.statedNumberOfTransactions = statedNumberOfTransactions;
		this.actualNumberOfTransactions = actualNumberOfTransactions;
	}
}