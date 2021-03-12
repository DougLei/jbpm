package com.douglei.bpm.module.repository.type;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryParameter;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.AbstractParameter;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessTypeService {
	
	/**
	 * 查询类型
	 * @param parameters
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public List<ProcessType> query(List<AbstractParameter> parameters) {
		return SessionContext.getSQLQuerySession().query(ProcessType.class, new SQLQueryParameter("queryProcessTypeList", parameters));
	}
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult insert(ProcessType type) {
		if(SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type) != null)
			return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
		
		SessionContext.getTableSession().save(type);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	
	/**
	 * 修改类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult update(ProcessType type) {
		Object[] obj = SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4ValidateCodeExists", type);
		if(obj != null && type.getId() != Integer.parseInt(obj[0].toString())) 
			return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
		
		SessionContext.getTableSession().update(type);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除类型
	 * @param typeId
	 * @param strict 是否进行强制删除; 强制删除时, 如果被删除的类型下存在流程定义, 则将这些流程定义的类型值改为0(默认类型)
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int typeId, boolean strict) {
		List<Object> param = Arrays.asList(typeId);
		
		int count = Integer.parseInt(SessionContext.getSqlSession().uniqueQuery_("select count(id) from bpm_re_procdef where type_id = ?", param)[0].toString());
		if(count > 0 && !strict)
			return new ExecutionResult("该流程类型关联了[%d]条流程, 无法删除", "jbpm.process.type.fail.ref.procdefs", count);
		
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_proctype where id=?", param);
		if(count > 0) 
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set type_id=0 where type_id=?", param);
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
