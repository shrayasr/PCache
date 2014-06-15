package main.com.pcache.exceptions;

public class PCacheException extends Exception {

	public PCacheException(String message) {
		super(message);
	}

	public PCacheException(String message, Exception ex) {
		super(message, ex);
	}

}
