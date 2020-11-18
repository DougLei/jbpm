package com.douglei.bpm.process.parser;

import com.douglei.bpm.JBPMException;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends JBPMException{
	
	public ProcessParseException(String message) {
		super(message);
	}
	public ProcessParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
