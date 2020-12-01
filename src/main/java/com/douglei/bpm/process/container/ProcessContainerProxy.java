package com.douglei.bpm.process.container;

import java.util.Arrays;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.entity.ProcessDefinition;
import com.douglei.bpm.process.metadata.ProcessMetadata;
import com.douglei.bpm.process.parser.ProcessParser;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

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
	 * 添加流程
	 * @param processDefinition
	 * @return 返回解析出的流程实例
	 */
	public ProcessMetadata addProcess(ProcessDefinition processDefinition) {
		ProcessMetadata process = parser.parse(processDefinition.getId(), processDefinition.getContent());
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
	 * 获取流程
	 * @param id
	 * @return
	 */
	@Transaction
	public ProcessMetadata getProcess(int id) {
		ProcessMetadata process =  container.getProcess(id);
		if(process == null) {
			ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, content_ from bpm_re_procdef where id=?", Arrays.asList(id));
			if(processDefinition == null)
				throw new NullPointerException("容器获取流程失败, 不存在id为["+id+"]的流程");
			process = addProcess(processDefinition);
		}
		return process;
	}
}
