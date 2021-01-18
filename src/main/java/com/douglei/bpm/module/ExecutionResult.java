package com.douglei.bpm.module;

/**
 * 执行结果
 * @author DougLei
 */
@SuppressWarnings("unchecked")
public class ExecutionResult {
	
	private String message; // 记录执行失败时的消息
	private String code; // 记录执行失败时的国际化编码
	private Object[] params; // 记录执行失败时的国际化编码需要的参数
	public ExecutionResult(String message, String code, Object... params) {
		this.message = message;
		this.code = code;
		this.params = params;
	}
	/**
	 * 获取执行失败时的消息
	 * @return
	 */
	public String getMessage() {
		if(params.length > 0)
			return String.format(message, params);
		return message;
	}
	/**
	 * 获取执行失败时的国际化编码
	 * @return
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 获取执行失败时的国际化编码参数
	 * @return
	 */
	public Object[] getParams() {
		return params;
	}
	
	// ------------------------------------------------------------------------------------------
	private Object object; // 执行成功时可以传递需要的实例
	public ExecutionResult(Object object) {
		this.success = true;
		this.object = object;
	}
	/**
	 * 获取执行成功时传递的实例
	 * @return
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * 获取执行成功时传递的实例
	 * @param clazz
	 * @return
	 */
	public <T> T getObject(Class<T> clazz) {
		return (T) object;
	}
	
	// ------------------------------------------------------------------------------------------
	private boolean success; // 是否成功
	public boolean isSuccess() {
		return success;
	}
	public boolean isFail() {
		return !success;
	}
	
	private static final ExecutionResult DEFAULT_SUCCESS_INSTANCE = new ExecutionResult(new Object());
	public static final ExecutionResult getDefaultSuccessInstance() {
		return DEFAULT_SUCCESS_INSTANCE;
	}
}
