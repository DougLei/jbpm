package com.douglei.bpm.process.metadata.task.user.candidate.handle;

import java.io.Serializable;

/**
 * 多人办理策略
 * @author DougLei
 */
public class MultiHandlePolicy implements Serializable{
	private HandleNumberExpression handleNumberExpression; // 可办理的人数的表达式
	private String handleEnd; // 办理是否可以结束的策略名称
	
	// 串行/并行办理配置
	private boolean serial; // 是否串行办理
	private String sort; // 串行办理时的办理顺序策略名称
}
