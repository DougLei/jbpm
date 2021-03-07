package com.douglei.bpm.process.api.container;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.parser.ProcessParseException;
import com.douglei.bpm.process.parser.ProcessParser;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessContainerProxy {
	
	@Autowired
	private ProcessParser parser;
	
	@Autowired
	private ProcessContainer container;
	
	/**
	 * 清空存储容器
	 */
	public void clear() {
		container.clear();
	}
	
	/**
	 * 添加流程, 如果存在相同id的流程, 将其cover; 如果不存在相同id的流程, 将其add
	 * @param processDefinition
	 * @return 返回解析出的流程实例
	 * @throws ProcessParseException
	 */
	public ProcessMetadata addProcess(ProcessDefinition processDefinition) throws ProcessParseException{
		ProcessMetadata process = parser.parse(processDefinition.getId(), processDefinition.getContent());
		container.addProcess(process);
		return process;
	}
	
	
	/**
	 * 删除流程
	 * @param processDefinitionId
	 */
	public void deleteProcess(int processDefinitionId) {
		container.deleteProcess(processDefinitionId);
	}

	/**
	 * 获取流程
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public ProcessMetadata getProcess(int processDefinitionId) {
		ProcessMetadata process =  container.getProcess(processDefinitionId);
		if(process == null) {
			ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, content_ from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			if(processDefinition == null)
				throw new NullPointerException("从容器获取流程失败, 不存在id为["+processDefinitionId+"]的流程");
			process = addProcess(processDefinition);
		}
		return process;
	}
}
