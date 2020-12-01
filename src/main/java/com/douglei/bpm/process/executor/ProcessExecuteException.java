package com.douglei.bpm.process.executor;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class ProcessExecuteException extends ProcessEngineException{
	
	public ProcessExecuteException(String message) {
		super(message);
	}
	public ProcessExecuteException(String message, Throwable cause) {
		super(message, cause);
	}
}
