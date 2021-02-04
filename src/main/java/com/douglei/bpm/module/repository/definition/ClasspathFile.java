package com.douglei.bpm.module.repository.definition;

/**
 * 
 * @author DougLei
 */
public class ClasspathFile {
	private String file; // 基于java resource路径的文件

	/**
	 * 基于java resource路径的文件
	 * @param file
	 */
	public ClasspathFile(String file) {
		this.file = file;
	}
	
	/**
	 * 获取基于java resource路径的文件
	 * @return
	 */
	public String getFile() {
		return file;
	}
}
