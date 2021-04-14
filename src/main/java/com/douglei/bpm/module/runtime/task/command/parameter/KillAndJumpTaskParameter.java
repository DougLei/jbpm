package com.douglei.bpm.module.runtime.task.command.parameter;

/**
 * 
 * @author DougLei
 */
public class KillAndJumpTaskParameter extends GeneralTaskParameter{
	private String targetTask; // 跳转的目标任务id
	private String reason; // KillAndJump原因
	private boolean strict; // 如果存在已经认领的指派信息, KillAndJump时是否强制删除相关数据; 建议使用默认值false
	
	public KillAndJumpTaskParameter(String targetTask, String reason) {
		this.targetTask = targetTask;
		this.reason = reason;
	}
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	/**
	 * 获取跳转的目标任务id
	 * @return
	 */
	public String getTargetTask() {
		return targetTask;
	}
	/**
	 * 获取KillAndJump原因
	 * @return
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 如果存在已经认领的指派信息, KillAndJump时是否强制删除相关数据
	 * @return
	 */
	public boolean isStrict() {
		return strict;
	}
}
