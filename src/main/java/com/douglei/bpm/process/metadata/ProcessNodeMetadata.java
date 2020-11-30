package com.douglei.bpm.process.metadata;

import java.io.Serializable;

import com.douglei.bpm.process.NodeType;
import com.douglei.tools.utils.StringUtil;

/**
 * 流程节点抽象父类
 * @author DougLei
 */
public abstract class ProcessNodeMetadata implements Serializable {
	protected String id;
	protected String name;
	
	protected ProcessNodeMetadata(String id, String name) {
		this.id = id;
		this.name = StringUtil.isEmpty(name)?id:name;
	}
	
	public final String getId() {
		return id;
	}
	public final String getName() {
		return name;
	}
	
	/**
	 * 获取节点类型
	 * @return
	 */
	public abstract NodeType getType();
}
