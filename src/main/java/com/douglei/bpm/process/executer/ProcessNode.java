package com.douglei.bpm.process.executer;

import java.io.Serializable;

import com.douglei.tools.utils.StringUtil;

/**
 * 流程节点抽象父类
 * @author DougLei
 */
public abstract class ProcessNode implements Serializable {
	protected String id;
	protected String name;
	
	protected ProcessNode(String id, String name) {
		this.id = id;
		this.name = StringUtil.isEmpty(name)?id:name;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
