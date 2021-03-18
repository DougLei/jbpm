package com.douglei.bpm.module.repository.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.repository.RepositoryException;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

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
	public ExecutionResult insert(ProcessType type) {
		boolean parentIdExists = type.getParentId()==0; // 是否存在parentId
		for (ProcessType pt : SessionContext.getSQLSession().query(ProcessType.class, "ProcessType", "query4Insert", type)) {
			if(pt.getCode().equals(type.getCode()))
				return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
			
			if(pt.getId() == type.getParentId())
				parentIdExists = true;
		}
		if(!parentIdExists)
			throw new RepositoryException("保存失败, 不存在id为["+type.getParentId()+"]的父流程类型");
		
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
		ProcessType old = SessionContext.getSqlSession().uniqueQuery(ProcessType.class, "select id, parent_id, code from bpm_re_proctype where id=?", Arrays.asList(type.getId()));
		if(old == null)
			throw new RepositoryException("修改失败, 不存在id为["+type.getId()+"]的流程类型");
		
		if(!type.getCode().equals(old.getCode()) && SessionContext.getSQLSession().uniqueQuery_("ProcessType", "query4Update", type) != null)
			return new ExecutionResult("已存在编码为[%s]的流程类型", "jbpm.process.type.fail.code.exists", type.getCode());
		
		// 验证parentId是否合法
		if(type.getParentId() != 0 && type.getParentId() != old.getParentId()) {
			HashSet<Integer> counter = new HashSet<Integer>();
			counter.add(type.getId());
			recursiveValidateParent(type.getParentId(), counter);
		}
		
		SessionContext.getTableSession().update(type);
		return ExecutionResult.getDefaultSuccessInstance();
	}
	// 递归验证父流程类型
	private void recursiveValidateParent(int parentId, HashSet<Integer> counter) {
		if(parentId == 0)
			return;
		
		if(counter.contains(parentId))
			throw new RepositoryException("修改失败, id为["+parentId+"]的流程类型重复作为父级出现");
		counter.add(parentId);
		
		Object[] array = SessionContext.getSqlSession().uniqueQuery_("select parent_id from bpm_re_proctype where id=?", Arrays.asList(parentId));
		if(array == null)
			throw new RepositoryException("修改失败, 不存在id为["+parentId+"]的父流程类型");
		recursiveValidateParent(Integer.parseInt(array[0].toString()), counter);
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
		if(list.size() == 2)
			throw new RepositoryException("删除失败, 不存在id为["+typeId+"]的流程类型");
		
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
	// 递归查询子类型的id
	private void recursiveQueryChildrenIds(List<Object[]> childrenIds, List<Object> ids) {
		if(childrenIds.isEmpty())
			return;
		
		childrenIds.forEach(array -> {
			if(ids.contains(array[0]))
				throw new RepositoryException("递归查询子流程类型时, 出现重复的id值["+array[0]+"]");
			ids.add(array[0]);
		});
		recursiveQueryChildrenIds(SessionContext.getSQLSession().query_("ProcessType", "queryChildrenIds", childrenIds), ids);
	}
}
