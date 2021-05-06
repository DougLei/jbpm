package com.douglei.bpm.module.execution.task.command.parameter;

/**
 * 
 * @author DougLei
 */
public class KillAndJumpTaskParameter extends GeneralTaskParameter{
	private String target; // 跳转的目标任务id
	private String reason; // KillAndJump原因
	
	public KillAndJumpTaskParameter(String userId, String target, String reason) {
		setUserId(userId);
		this.target = target;
		this.reason = reason;
	}
	
	/**
	 * 获取跳转的目标任务id
	 * @return
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 获取KillAndJump原因
	 * @return
	 */
	public String getReason() {
		return reason;
	}
}
