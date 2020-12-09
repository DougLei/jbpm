package com.douglei.bpm.module;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult {
	private boolean success; // 是否成功
	
	private String failMessage; // 记录执行失败时的消息
	public ExecutionResult(String failMessage) {
		this.failMessage = failMessage;
	}
	public String getFailMessage() {
		return failMessage;
	}
	public boolean isSuccess() {
		return success;
	}
	public boolean isFail() {
		return !success;
	}
	
	private ExecutionResult() {
		this.success = true;
	}
	private static final ExecutionResult SUCCESS_INSTANCE = new ExecutionResult();
	public static final ExecutionResult getSuccessInstance() {
		return SUCCESS_INSTANCE;
	}
	
	private static final ExecutionResult DEFAULT_FAIL_INSTANCE = new ExecutionResult("Fail");
	public static final ExecutionResult getDefaultFailInstance() {
		return DEFAULT_FAIL_INSTANCE;
	}
}
