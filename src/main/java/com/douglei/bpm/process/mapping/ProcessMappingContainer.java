package com.douglei.bpm.process.mapping;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.douglei.bpm.ProcessEngineException;
import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.repository.definition.ProcessDefinition;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadata;
import com.douglei.bpm.process.mapping.metadata.ProcessMetadataAdapter;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.mapping.Mapping;
import com.douglei.orm.mapping.handler.entity.DeleteMappingEntity;

/**
 * 
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessMappingContainer {
	
	@Autowired
	private SessionFactoryContainer container;
	
	/**
	 * 添加流程
	 * @param definition
	 * @return 
	 */
	public Mapping addProcess(ProcessDefinition definition) {
		AddOrCoverMappingEntity4Process entity = new AddOrCoverMappingEntity4Process(definition);
		container.get().getMappingHandler().execute(entity);
		return entity.getMapping();
	}
	
	
	/**
	 * 删除流程
	 * @param processDefinitionId
	 */
	public void deleteProcess(int processDefinitionId) {
		DeleteMappingEntity entity = new DeleteMappingEntity(processDefinitionId+"");
		container.get().getMappingHandler().execute(entity);
	}

	/**
	 * 获取流程
	 * @param processDefinitionId
	 * @return
	 */
	@Transaction
	public ProcessMetadata getProcess(int processDefinitionId) {
		Mapping mapping = container.get().getMappingHandler().getMapping(processDefinitionId+"", ProcessMappingType.NAME, false);
		if(mapping == null) {
			ProcessDefinition definition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, content_, signature from bpm_re_procdef where id=?", Arrays.asList(processDefinitionId));
			if(definition == null)
				throw new ProcessEngineException("从容器获取流程失败, 不存在id为["+processDefinitionId+"]的流程");
			if(!DigestUtils.md5Hex(definition.getContent()).equals(definition.getSignature()))
				throw new ProcessEngineException("从容器获取流程失败, id为["+processDefinitionId+"]的流程配置数据被篡改");
			mapping = addProcess(definition);
		}
		return ((ProcessMetadataAdapter)mapping.getMetadata()).getProcessMetadata();
	}
}