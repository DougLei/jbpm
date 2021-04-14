package com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch;

/**
 * 
 * @author DougLei
 */
public class BackOption extends AbstractDispatchOption {
	private int steps;
	
	public BackOption(String type, String name, int order, int steps, boolean suggestIsRequired, boolean attitudeIsRequired) {
		super(type, name, order, suggestIsRequired, attitudeIsRequired);
		this.steps = steps;
	}
	
	/**
	 * 获取要回退的步数
	 * @return
	 */
	public int getSteps() {
		return steps;
	}
}
