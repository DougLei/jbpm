package com.douglei.bpm.module.repository.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.douglei.bpm.module.common.entity.BasicEntity;
import com.douglei.tools.instances.file.resources.reader.ResourcesReader;

/**
 * 
 * @author DougLei
 */
public class ProcessDefinition extends BasicEntity{
	private int refTypeId;
	private String name;
	private String code;
	private String version;
	private int builtinVersion;
	private String content;
	private int enabled;
	private String description;
	
	/**
	 * 是否启用
	 * @return
	 */
	public boolean isEnabled() {
		return enabled == 1;
	}
	
	public ProcessDefinition setRefTypeId(int refTypeId) {
		this.refTypeId = refTypeId;
		return this;
	}
	public ProcessDefinition setDescription(String description) {
		this.description = description;
		return this;
	}
	public ProcessDefinition setEnabled(int enabled) {
		this.enabled = enabled;
		return this;
	}
	public ProcessDefinition setCode(String code) {
		this.code = code;
		return this;
	}
	public ProcessDefinition setVersion(String version) {
		this.version = version;
		return this;
	}
	public ProcessDefinition setBuiltinVersion(int builtinVersion) {
		this.builtinVersion = builtinVersion;
		return this;
	}
	public ProcessDefinition setName(String name) {
		this.name = name;
		return this;
	}
	public ProcessDefinition setContent(String content) {
		this.content = content;
		return this;
	}
	public ProcessDefinition setContent(File file) throws FileNotFoundException {
		this.content = new ResourcesReader(new FileInputStream(file)).readAll(500).toString();
		return this;
	}
	public int getRefTypeId() {
		return refTypeId;
	}
	public String getCode() {
		return code;
	}
	public String getVersion() {
		return version;
	}
	public int getBuiltinVersion() {
		return builtinVersion;
	}
	public String getName() {
		return name;
	}
	public int getEnabled() {
		return enabled;
	}
	public String getDescription() {
		return description;
	}
	public String getContent() {
		return content;
	}
}
