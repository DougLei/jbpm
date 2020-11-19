package com.douglei.bpm.module.runtime.instance.start;

import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.components.variable.Variable;

/**
 * 流程的启动参数
 * @author DougLei
 */
public class StartParameter {
	private StartingMode startingMode;
	private int processDefinitionId; // 流程定义的id
	
	private String code; // 流程code
	private String version; // 流程版本
	
	private String businessId; // (主)业务标识
	private String startUser; // 启动人
	private Map<String, Variable> variables; // 流程变量map集合
	
	public StartParameter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
		this.startingMode = StartingMode.BY_PROCESS_DEFINITION_ID;
	}
	public StartParameter(String code, String version) {
		this.code = code;
		this.version = version;
		this.startingMode = StartingMode.BY_PROCESS_DEFINITION_CODE_VERSION;
	}
	
	public StartParameter setBusinessId(String businessId) {
		this.businessId = businessId;
		return this;
	}
	public StartParameter setStartUser(String startUser) {
		this.startUser = startUser;
		return this;
	}
	
	public StartParameter addVariable(String name, Object value) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		this.variables.put(name, new Variable(name, value));
		return this;
	}
	public StartParameter addVariables(Map<String, Object> variables) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		
		variables.entrySet().forEach(entry -> this.variables.put(entry.getKey(), new Variable(entry.getKey(), entry.getValue())));
		return this;
	}
	
	public int getProcessDefinitionId() {
		return processDefinitionId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public String getStartUser() {
		return startUser;
	}
	public Map<String, Variable> getVariables() {
		return variables;
	}
	
	/**
	 * 获取在数据库验证后的流程定义id值
	 * @param session
	 * @return
	 */
//	int getProcessDefinitionIdAfterDBValidate(SqlSession session) {
//		if(processDefinitionId > 0) {
//			Object[] processDefinition = session.uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
//			if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
//				throw new NullPointerException("启动失败, 不存在id为["+processDefinitionId+"]的流程定义");
//			if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
//				throw new IllegalArgumentException("启动失败, id为["+processDefinitionId+"]的流程定义还未发布");
//		}else {
//			Object[] processDefinition = session.queryFirst_("select state, id from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(code, version));
//			if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
//				throw new NullPointerException("启动失败, 不存在code为["+code+"], version为["+version+"]的流程定义");
//			if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
//				throw new IllegalArgumentException("启动失败, code为["+code+"], version为["+version+"]的流程定义还未发布");
//			this.processDefinitionId = Integer.parseInt(processDefinition[1].toString());
//		}
//		return this.processDefinitionId;
//	}
}