package com.douglei.bpm.module.repository.definition.builder;

/**
 * 流程的基础属性
 * @author DougLei
 */
enum ProcessAttribute {
	NAME(false),
	CODE(true),
	VERSION(true);
	
	String name;
	boolean required;
	private ProcessAttribute(boolean required) {
		this.name = name().toLowerCase();
		this.required = required;
	}
}
