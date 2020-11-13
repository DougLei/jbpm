package com.douglei.bpm.process.parser;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends RuntimeException{
	
	public ProcessParseException(String message) {
		super(message);
	}
	public ProcessParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
