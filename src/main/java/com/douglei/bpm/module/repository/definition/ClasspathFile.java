package com.douglei.bpm.module.repository.definition;

/**
 * 类路径下的资源文件
 * @author DougLei
 */
public class ClasspathFile {
	private String name; // 资源名称

	public ClasspathFile() {}
	public ClasspathFile(String name) {
		setName(name);
	}
	
	public String getName() {
		return name;
	}
	public ClasspathFile setName(String name) {
		this.name = name;
		return this;
	}
}
