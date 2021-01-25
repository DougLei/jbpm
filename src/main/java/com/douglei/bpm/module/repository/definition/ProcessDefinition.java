package com.douglei.bpm.module.repository.definition;

/**
 * 流程定义
 * @author DougLei
 */
public class ProcessDefinition {
	private int id;
	private int typeId;
	private String name;
	private String code;
	private String version;
	private int isMajorVersion;
	private int subversion;
	private int isMajorSubversion;
	private String content;
	private String signature;
	private State state;
	private String description;
	private String tenantId;
	
	public ProcessDefinition() {}
	public ProcessDefinition(String name, String code, String version) {
		this.name = name;
		this.code = code;
		this.version = version;
		this.isMajorSubversion = 1;
		this.state = State.INITIAL;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getName() {
		if(name == null)
			name = code + ":" + version;
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isMajorVersion() {
		return isMajorVersion == 1;
	}
	public int getIsMajorVersion() {
		return isMajorVersion;
	}
	public void setIsMajorVersion(int isMajorVersion) {
		this.isMajorVersion = isMajorVersion;
	}
	public int getSubversion() {
		return subversion;
	}
	public void setSubversion(int subversion) {
		this.subversion = subversion;
	}
	public boolean isMajorSubversion() {
		return isMajorSubversion == 1;
	}
	public int getIsMajorSubversion() {
		return isMajorSubversion;
	}
	public void setIsMajorSubversion(int isMajorSubversion) {
		this.isMajorSubversion = isMajorSubversion;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public State getStateInstance() {
		return state;
	}
	public void setStateInstance(State state) {
		this.state = state;
	}
	public String getState() {
		return state.name();
	}
	public void setState(String state) {
		this.state = State.valueOf(state);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}