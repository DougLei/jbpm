package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

/**
 * 办理策略
 * @author DougLei
 */
public class HandlePolicy implements Serializable {
	private boolean suggest; // 是否需要强制输入意见
	private boolean attitude; // 是否需要强制表态
	private boolean multiHandle; // 是否多人办理
	private MultiHandlePolicy multiHandlePolicy; // 多人办理策略
	
	public HandlePolicy(boolean suggest, boolean attitude, MultiHandlePolicy multiHandlePolicy) {
		this.suggest = suggest;
		this.attitude = attitude;
		this.multiHandle = multiHandlePolicy!=null;
		this.multiHandlePolicy = multiHandlePolicy;
	}
}
