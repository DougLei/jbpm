package com.douglei.bpm.module;

/**
 * 执行结果
 * @author DougLei
 */
@SuppressWarnings("unchecked")
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
	
	private Object object; // 执行成功时可以传递需要的实例
	public ExecutionResult(Object object) {
		this.success = true;
		this.object = object;
	}
	public Object getObject() {
		return object;
	}
	public <T> T getObject(Class<T> clazz) {
		return (T) object;
	}
	
	private static final ExecutionResult DEFAULT_SUCCESS_INSTANCE = new ExecutionResult(new Object());
	public static final ExecutionResult getDefaultSuccessInstance() {
		return DEFAULT_SUCCESS_INSTANCE;
	}
	
	private static final ExecutionResult DEFAULT_FAIL_INSTANCE = new ExecutionResult("Fail");
	public static final ExecutionResult getDefaultFailInstance() {
		return DEFAULT_FAIL_INSTANCE;
	}
}
