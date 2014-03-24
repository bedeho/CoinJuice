package org.coinjuice.exception;

// Used to notify when TxIn constructor gets transaction counter and number of transactions

public class IncorrectNumberInputTransactionsException extends Exception {
	
	long statedNumberOfTransactions;
	int actualNumberOfTransactions;

	public IncorrectNumberInputTransactionsException(long statedNumberOfTransactions, int actualNumberOfTransactions) {

		super("The stated number of input trasnsactions was " + statedNumberOfTransactions + ", but the actual number of input transactions was: " + actualNumberOfTransactions);

		this.statedNumberOfTransactions = statedNumberOfTransactions;
		this.actualNumberOfTransactions = actualNumberOfTransactions;
	}
}