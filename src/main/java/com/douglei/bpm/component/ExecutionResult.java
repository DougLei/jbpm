package com.douglei.bpm.component;

/**
 * 执行结果
 * @author DougLei
 */
public class ExecutionResult<T> {
	private boolean success;
	
	// 记录执行失败时的消息
	private String failMessage;
	public ExecutionResult(String failMessage) {
		this.failMessage = failMessage;
	}
	public boolean isFail() {
		return !success;
	}
	public String getFailMessage() {
		return failMessage;
	}
	
	// 记录执行成功时返回的实例
	private T result;
	public ExecutionResult(T result) {
		this.success = true;
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public T getResult() {
		return result;
	}
}
