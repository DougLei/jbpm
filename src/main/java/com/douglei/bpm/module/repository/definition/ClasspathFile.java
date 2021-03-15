package com.douglei.bpm.module.repository.definition;

/**
 * 
 * @author DougLei
 */
public class ClasspathFile {
	private String path; // 基于java resource路径的文件

	/**
	 * 基于java resource路径的文件
	 * @param path
	 */
	public ClasspathFile(String path) {
		this.path = path;
	}
	
	/**
	 * 获取基于java resource路径的文件
	 * @return
	 */
	public String getPath() {
		return path;
	}
}
