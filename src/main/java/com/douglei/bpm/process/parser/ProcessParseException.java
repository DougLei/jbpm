package com.douglei.bpm.process.parser;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends RuntimeException{
	private static final long serialVersionUID = -1006301697731190021L;
	
	public ProcessParseException(String message) {
		super(message);
	}
	public ProcessParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
