package com.douglei.bpm.process;

import com.douglei.bpm.bean.Autowired;
import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.process.container.ProcessContainer;
import com.douglei.bpm.process.node.Process;
import com.douglei.bpm.process.parser.ProcessParser;

/**
 * 
 * @author DougLei
 */
@Bean
public class ProcessHandler {
	
	@Autowired
	private ProcessParser parser;
	
	@Autowired
	private ProcessContainer container;
	
	/**
	 * 添加流程
	 * @param processDefinition
	 * @return 返回解析出的流程实例
	 */
	public Process addProcess(ProcessDefinition processDefinition) {
		return addProcess(processDefinition.getId(), processDefinition.getContent());
	}
	
	/**
	 * 添加流程
	 * @param processDefinitionId
	 * @param content
	 * @return 返回解析出的流程实例
	 */
	public Process addProcess(int processDefinitionId, String content) {
		Process process = parser.parse(processDefinitionId, content);
		container.addProcess(process);
		return process;
	}
	
	/**
	 * 删除流程
	 * @param id
	 */
	public void deleteProcess(int id) {
		container.deleteProcess(id);
	}

	/**
	 * 根据id获取流程
	 * @param id
	 * @return
	 */
	public Process getProcess(int id) {
		Process process =  container.getProcess(id);
		if(process == null) {
//			Object[] processDefinition = SessionContext.getSqlSession().uniqueQuery_("select content_ from bpm_re_procdef where id=?", Arrays.asList(id));
//			if(processDefinition == null)
//				throw new NullPointerException("不存在id为["+id+"]的流程");
//			process = addProcess(id, processDefinition[0].toString());
		}
		return process;
	}
}
