package com.douglei.bpm;

/**
 * 
 * @author DougLei
 */
public class ProcessEngineException extends RuntimeException {

	public ProcessEngineException() {}
	public ProcessEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public ProcessEngineException(String message, Throwable cause) {
		super(message, cause);
	}
	public ProcessEngineException(String message) {
		super(message);
	}
	public ProcessEngineException(Throwable cause) {
		super(cause);
	}
}
