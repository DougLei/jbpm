package com.douglei.bpm.module.repository.type;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.components.ProcessObjectException;
import com.douglei.bpm.module.repository.type.entity.ProcessType;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessTypeService {
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ProcessType insert(ProcessType type) {
		if(SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type) != null)
			throw new ProcessObjectException("已存在编码为["+type.getCode()+"]的流程类型");
		
		SessionContext.getTableSession().save(type);
		return type;
	}
	
	/**
	 * 修改类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ProcessType update(ProcessType type) {
		Object[] obj = SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type);
		if(obj != null && type.getId() != Integer.parseInt(obj[0].toString())) 
			throw new ProcessObjectException("已存在编码为["+type.getCode()+"]的流程类型");
		
		SessionContext.getTableSession().update(type);
		return type;
	}
	
	/**
	 * 删除类型
	 * @param processTypeId
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return
	 */
	@Transaction
	public int delete(int processTypeId, boolean strict) {
		List<Object> paramList = Arrays.asList(processTypeId);
		
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where type_id = ?", paramList)[0].toString());
		if(count > 0 && !strict)
			throw new ProcessObjectException("该流程类型关联了["+count+"]条流程, 无法删除");

		SessionContext.getSqlSession().executeUpdate("delete bpm_re_proctype where id=?", paramList);
		if(count > 0) 
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set type_id=0 where type_id=?", paramList);
		return processTypeId;
	}
}
