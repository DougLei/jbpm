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
	protected NodeType type;
	
	protected ProcessNodeMetadata(String id, String name, NodeType type) {
		this.id = id;
		this.name = StringUtil.isEmpty(name)?id:name;
		this.type = type;
	}
	
	public final String getId() {
		return id;
	}
	public final String getName() {
		return name;
	}
	public final NodeType getType() {
		return type;
	}
}
