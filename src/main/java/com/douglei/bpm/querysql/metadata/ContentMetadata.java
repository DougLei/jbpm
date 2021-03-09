package com.douglei.bpm.querysql.metadata;

import java.io.Serializable;

/**
 * 
 * @author DougLei
 */
public class ContentMetadata implements Serializable{
	private boolean package_ = true;
	private boolean append;
	private String content;

	public ContentMetadata(boolean package_, boolean append, String content) {
		this.package_ = package_;
		this.append = append;
		this.content = content;
	}
	
	public boolean isPackage() {
		return package_;
	}
	public boolean isAppend() {
		return append;
	}
	public String getContent() {
		return content;
	}
}
