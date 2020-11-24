package com.douglei.bpm.module.components.command;

/**
 * 命令执行异常
 * @author DougLei
 */
@SuppressWarnings("rawtypes")
public class CommandExecuteException extends RuntimeException {
	private static final long serialVersionUID = -9159804824680165033L;
	
	public CommandExecuteException(Command command, Throwable cause) {
		super("执行 ["+command.getClass().getName()+"] 命令时出现异常", cause);
	}
	public CommandExecuteException(String message, Throwable cause) {
		super(message, cause);
	}
	public CommandExecuteException(String message) {
		super(message);
	}
	public CommandExecuteException(Throwable cause) {
		super(cause);
	}
}
