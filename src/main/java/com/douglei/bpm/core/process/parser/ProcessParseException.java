package com.douglei.bpm.core.process.parser;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends RuntimeException{
	private static final long serialVersionUID = -76726380524981828L;

	public ProcessParseException(Throwable cause) {
		super(cause);
	}

	public ProcessParseException(String message) {
		super(message);
	}
}
