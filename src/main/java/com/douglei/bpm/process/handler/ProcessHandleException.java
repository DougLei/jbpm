package com.douglei.bpm.process.handler;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class ProcessHandleException extends ProcessEngineException {

	public ProcessHandleException(String message, Throwable cause) {
		super(message, cause);
	}
	public ProcessHandleException(String message) {
		super(message);
	}
}
