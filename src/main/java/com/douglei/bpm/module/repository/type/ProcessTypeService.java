package com.douglei.bpm.module.repository.type;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.Bean;
import com.douglei.bpm.module.components.ExecutionResult;
import com.douglei.bpm.module.repository.type.entity.ProcessType;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean
public class ProcessTypeService {
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult<ProcessType> save(ProcessType type) {
		if(SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type) != null)
			return new ExecutionResult<ProcessType>("已存在编码为[%s]的流程类型", "bpm.process.type.code.exists", type.getCode());
		
		SessionContext.getTableSession().save(type);
		return new ExecutionResult<ProcessType>(type);
	}
	
	/**
	 * 修改类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult<ProcessType> update(ProcessType type) {
		Object[] obj = SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type);
		if(obj != null && type.getId() != Integer.parseInt(obj[0].toString())) 
			return new ExecutionResult<ProcessType>("已存在编码为[%s]的流程类型", "bpm.process.type.code.exists", type.getCode());
		
		SessionContext.getTableSession().update(type);
		return new ExecutionResult<ProcessType>(type);
	}
	
	/**
	 * 删除类型
	 * @param processTypeId
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return
	 */
	@Transaction
	public ExecutionResult<Integer> delete(int processTypeId, boolean strict) {
		List<Object> paramList = Arrays.asList(processTypeId);
		
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where type_id = ?", paramList)[0].toString());
		if(count > 0 && !strict)
			return new ExecutionResult<Integer>("该流程类型关联了[%d]条流程, 无法删除", "bpm.process.type.delete.fail", count);

		SessionContext.getSqlSession().executeUpdate("delete bpm_re_proctype where id=?", paramList);
		if(count > 0) 
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set type_id=0 where type_id=?", paramList);
		return new ExecutionResult<Integer>(processTypeId, strict);
	}
}
