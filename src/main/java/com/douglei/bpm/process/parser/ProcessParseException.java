package com.douglei.bpm.process.parser;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class ProcessParseException extends ProcessEngineException{
	
	public ProcessParseException(String message) {
		super(message);
	}
	public ProcessParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
