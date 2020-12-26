package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

/**
 * 办理策略
 * @author DougLei
 */
public class HandlePolicy implements Serializable {
	private boolean suggest; // 是否需要强制输入意见
	private boolean attitude; // 是否需要强制表态
	private HandleMode mode; // 办理模式
	private MultiHandlePolicy multiHandlePolicy; // 多人办理策略
	
}
