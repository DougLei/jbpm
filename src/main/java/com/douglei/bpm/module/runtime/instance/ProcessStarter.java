package com.douglei.bpm.module.runtime.instance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.module.components.variable.Variable;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinitionStateConstants;
import com.douglei.orm.sessionfactory.sessions.sqlsession.SqlSession;

/**
 * 流程启动器
 * @author DougLei
 */
public class ProcessStarter {
	private int processDefinitionId; // 流程定义的id
	
	private String code; // 流程code
	private String version; // 流程版本
	
	private String busiId; // (主)业务标识
	private String startUser; // 启动人
	private Map<String, Variable> variables; // 流程变量map集合
	
	public ProcessStarter(int processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	public ProcessStarter(String code, String version) {
		this.code = code;
		this.version = version;
	}
	
	public ProcessStarter setBusiId(String busiId) {
		this.busiId = busiId;
		return this;
	}
	public ProcessStarter setStartUser(String startUser) {
		this.startUser = startUser;
		return this;
	}
	
	public ProcessStarter addVariable(String name, Object value) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		this.variables.put(name, new Variable(name, value));
		return this;
	}
	public ProcessStarter addVariables(Map<String, Object> variables) {
		if(this.variables == null)
			this.variables = new HashMap<String, Variable>();
		
		variables.entrySet().forEach(entry -> this.variables.put(entry.getKey(), new Variable(entry.getKey(), entry.getValue())));
		return this;
	}
	
	public String getBusiId() {
		return busiId;
	}
	public String getStartUser() {
		return startUser;
	}
	public Map<String, Variable> getVariables() {
		return variables;
	}
	
	/**
	 * 获取验证后的流程定义id值
	 * @param session
	 * @return
	 */
	int getProcessDefinitionIdAfterValidate(SqlSession session) {
		if(processDefinitionId > 0) {
			Object[] processDefinition = session.uniqueQuery_("select state from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
				throw new NullPointerException("启动失败, 不存在id为["+processDefinitionId+"]的流程定义");
			if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
				throw new IllegalArgumentException("启动失败, id为["+processDefinitionId+"]的流程定义还未发布");
			return this.processDefinitionId;
		}
		
		Object[] processDefinition = session.queryFirst_("select state, id from bpm_re_procdef where code=? and version=? order by subversion desc", Arrays.asList(code, version));
		if(processDefinition == null || Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.DELETE)
			throw new NullPointerException("启动失败, 不存在code为["+code+"], version为["+version+"]的流程定义");
		if(Byte.parseByte(processDefinition[0].toString()) == ProcessDefinitionStateConstants.UNPUBLISHED)
			throw new IllegalArgumentException("启动失败, code为["+code+"], version为["+version+"]的流程定义还未发布");
		return Integer.parseInt(processDefinition[1].toString());
	}
}