package com.douglei.bpm.module;

import com.douglei.bpm.ProcessEngineException;

/**
 * 
 * @author DougLei
 */
public class CommandExecuteException extends ProcessEngineException {

	public CommandExecuteException(String message, Throwable cause) {
		super(message, cause);
	}
	public CommandExecuteException(String message) {
		super(message);
	}
}
