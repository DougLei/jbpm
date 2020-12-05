package com.douglei.bpm.process.executor;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class ExecuteException extends ProcessEngineException{
	
	public ExecuteException(String message) {
		super(message);
	}
	public ExecuteException(String message, Throwable cause) {
		super(message, cause);
	}
}
