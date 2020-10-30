package com.douglei.bpm.process.parser;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends RuntimeException{
	private static final long serialVersionUID = 2004504868846663317L;
	
	public ProcessParseException(String message) {
		super(message);
	}
	public ProcessParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
