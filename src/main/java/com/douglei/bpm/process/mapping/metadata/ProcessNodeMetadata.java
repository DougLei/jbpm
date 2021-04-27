package com.douglei.bpm.process.mapping.metadata;

import java.util.List;

import com.douglei.bpm.process.Type;
import com.douglei.orm.mapping.metadata.Metadata;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
public abstract class ProcessNodeMetadata implements Metadata {
	private String id;
	private String name;
	private List<String> listeners;
	
	public ProcessNodeMetadata(String id, String name) {
		this.id = id;
		this.name = StringUtil.isEmpty(name)?id:name;
	}
	public void setListeners(List<String> listeners) {
		this.listeners = listeners;
	}

	public final String getId() {
		return id;
	}
	public final String getName() {
		return name;
	}
	public final List<String> getListeners() {
		return listeners;
	}
	
	/**
	 * 获取节点类型
	 * @return
	 */
	public abstract Type getType();

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", type=" + getType() + "]";
	}
}
