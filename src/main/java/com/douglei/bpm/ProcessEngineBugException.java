package com.douglei.bpm;

/**
 * 
 * @author DougLei
 */
public class ProcessEngineBugException extends ProcessEngineException{

	public ProcessEngineBugException(String message) {
		super("BUG: " + message);
	}
}
