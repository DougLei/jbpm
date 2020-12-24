package com.douglei.bpm.process.handler;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class TaskHandleException extends ProcessEngineException {

	public TaskHandleException(String message, Throwable cause) {
		super(message, cause);
	}
	public TaskHandleException(String message) {
		super(message);
	}
}
