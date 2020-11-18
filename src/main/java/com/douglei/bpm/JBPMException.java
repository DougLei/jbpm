package com.douglei.bpm;

/**
 * 
 * @author DougLei
 */
public class JBPMException extends RuntimeException {

	public JBPMException(String message) {
		super(message);
	}
	
	public JBPMException(String message, Throwable cause) {
		super(message, cause);
	}
}
