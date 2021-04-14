package com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch;

import com.douglei.bpm.process.mapping.metadata.task.user.candidate.Candidate;

/**
 * 
 * @author DougLei
 */
public class JumpOption extends AbstractDispatchOption {
	private String target;
	private Candidate candidate;
	
	public JumpOption(String type, String name, int order, String target, boolean suggestIsRequired, boolean attitudeIsRequired, Candidate candidate) {
		super(type, name, order, suggestIsRequired, attitudeIsRequired);
		this.target = target;
		this.candidate = candidate;
	}
	
	/**
	 * 获取要跳转的目标id
	 * @return
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * 获取候选人配置
	 * @return
	 */
	public Candidate getCandidate() {
		return candidate;
	}
}
