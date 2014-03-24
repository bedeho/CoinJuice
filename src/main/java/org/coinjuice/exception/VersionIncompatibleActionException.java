package org.coinjuice.exception;

// Exception for when actions are not compatible with protocol version
public class VersionIncompatibleActionException extends Exception {

	public VersionIncompatibleActionException(String message) {
		super(message);
	}
}