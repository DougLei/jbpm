package com.douglei.bpm.module.repository.definition.entity.builder;

/**
 * 类路径下的资源文件
 * @author DougLei
 */
public class ClasspathFile {
	private String name; // 资源名称

	public ClasspathFile(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
