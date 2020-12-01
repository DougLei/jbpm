package com.douglei.bpm.module.components;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class ProcessObjectException extends ProcessEngineException {

	public ProcessObjectException() {
		super();
	}
	public ProcessObjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public ProcessObjectException(String message, Throwable cause) {
		super(message, cause);
	}
	public ProcessObjectException(String message) {
		super(message);
	}
	public ProcessObjectException(Throwable cause) {
		super(cause);
	}
}
