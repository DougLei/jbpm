package com.douglei.bpm.core.process.executer;

import java.io.Serializable;

import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
public abstract class Node implements Serializable {
	protected String id;
	protected String name;
	
	protected Node(String id, String name) {
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
