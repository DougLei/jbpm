package com.douglei.bpm.module.repository.definition;

/**
 * 流程定义状态
 * @author DougLei
 */
enum State {
	UNPUBLISHED(0), // 未发布
	PUBLISHED(1), // 已发布
	DELETE(2); // 被删除
	
	private int value;
	private State(int value) {
		this.value = value;
	}
	
	/**
	 * 获取状态值
	 * @return
	 */
	public int value() {
		return value;
	}
}
