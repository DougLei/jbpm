package com.douglei.bpm.core.process.executer;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public abstract class Node implements Serializable {
	protected String id;
	protected String name;
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
