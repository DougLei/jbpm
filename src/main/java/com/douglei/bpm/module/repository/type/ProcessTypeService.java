package com.douglei.bpm.module.repository.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.RepositoryException;
import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.SQLQueryEntity;
import com.douglei.orm.sessionfactory.sessions.session.sqlquery.impl.AbstractParameter;
import com.douglei.orm.sessionfactory.sessions.sqlsession.RecursiveEntity;

/**
 * 流程类型服务
 * @author DougLei
 */
@Bean(isTransaction=true)
public class ProcessTypeService {
	
	/**
	 * 查询类型
	 * @param entity
	 * @param parameters
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public List<ProcessType> query(RecursiveEntity entity, List<AbstractParameter> parameters) {
		return SessionContext.getSQLQuerySession().recursiveQuery(ProcessType.class, entity, new SQLQueryEntity("queryProcessTypeList", parameters));
	}
	
	/**
	 * 保存类型
	 * @param type
	 * @return
	 */
	@Transaction
	public ExecutionResult insert(ProcessType type) {
		List<ProcessType> types = SessionContext.getSQLSession().query(ProcessType.class, "ProcessType", "query4Validate", type);
		if(types.isEmpty()) {
			if(type.getParentId() != 0)
				throw new RepositoryException("保存失败, 不存在(parent)id为["+type.getParentId()+"]的流程类型");
		}else {
			boolean existsParentId = type.getParentId()==0; // parentId是否存在
			for (ProcessType pt : types) {
				if(pt.getCode().equals(type.getCode()))
					return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
				
				if(pt.getId() == type.getParentId())
					existsParentId = true;
			}
				
			if(!existsParentId)
				throw new RepositoryException("保存失败, 不存在(parent)id为["+type.getParentId()+"]的流程类型");
		}
		
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
		List<ProcessType> types = SessionContext.getSQLSession().query(ProcessType.class, "ProcessType", "query4Validate", type);
		
		boolean existsParentId = type.getParentId()==0; // parentId是否存在
		for (ProcessType pt : types) {
			if(pt.getId() != type.getId() && pt.getCode().equals(type.getCode()))
				return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
			
			if(pt.getId() == type.getParentId())
				existsParentId = true;
		}
			
		if(!existsParentId)
			throw new RepositoryException("修改失败, 不存在(parent)id为["+type.getParentId()+"]的流程类型");
		
		SessionContext.getTableSession().update(type);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除类型
	 * @param typeId
	 * @param strict 是否进行强制删除
	 * @return
	 */
	@Transaction
	public ExecutionResult delete(int typeId, boolean strict) {
		List<Object[]> list = SessionContext.getSQLSession().query_("ProcessType", "query4Delete", typeId);
		
		int childrenCount = Integer.parseInt(list.get(0)[0].toString());
		if(childrenCount > 0 && !strict)
			return new ExecutionResult("该流程类型下存在子类型, 无法删除", "jbpm.process.type.fail.children.exists");
		
		int refProessCount = Integer.parseInt(list.get(1)[0].toString());
		if(refProessCount > 0 && !strict)
			return new ExecutionResult("该流程类型关联了[%d]条流程定义, 无法删除", "jbpm.process.type.fail.ref.procdef", refProessCount);
		
		List<Object> ids = new ArrayList<Object>();
		ids.add(typeId);
		
		// 获取子类型的id集合
		if(childrenCount> 0) 
			recursiveQueryChildrenIds(SessionContext.getSqlSession().query_("select id from bpm_re_proctype where parent_id=?", ids), ids);
		
		// 有子类型, 或有关联流程定义; 更新流程定义关联的类型
		if(childrenCount> 0 || refProessCount> 0) { 
			Map<String, Object> paramMap = new HashMap<String, Object>(4);
			paramMap.put("ids", ids);
			paramMap.put("targetId", Integer.parseInt(list.get(2)[0].toString())); // 目标类型id
			SessionContext.getSQLSession().executeUpdate("ProcessType", "updateProcdefRefType", paramMap);
		}
		
		// 删除类型
		SessionContext.getSQLSession().executeUpdate("ProcessType", "deleteType", ids); 
		return ExecutionResult.getDefaultSuccessInstance();
	}

	// 递归查询出子类型的id
	private void recursiveQueryChildrenIds(List<Object[]> childrenIds, List<Object> ids) {
		if(childrenIds.isEmpty())
			return;
		
		childrenIds.forEach(array -> ids.add(array[0]));
		recursiveQueryChildrenIds(SessionContext.getSQLSession().query_("ProcessType", "queryChildrenIds", childrenIds), ids);
	}
}
