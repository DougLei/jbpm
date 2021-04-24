package com.douglei.bpm.module.repository.definition;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.task.HandleState;
import com.douglei.bpm.module.repository.RepositoryException;
import com.douglei.bpm.process.mapping.ProcessMappingContainer;
import com.douglei.orm.context.PropagationBehavior;
import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.Transaction;

/**
 * 流程定义服务
 * @author DougLei
 */
@Bean(isTransaction = true)
public class ProcessDefinitionService {
	
	@Autowired
	private ProcessMappingContainer container;
	
	/**
	 * 修改流程的状态
	 * @param id
	 * @param target
	 */
	private void updateState_(int id, State target) {
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set state=?, is_major_version=0 where id=?", Arrays.asList(target.getValue(), id));
	}
	
	/**
	 * 是否存在(运行/历史)实例
	 * @param id 流程定义id
	 * @return
	 */
	@Transaction(propagationBehavior=PropagationBehavior.SUPPORTS)
	public boolean existsInstance(int id) {
		List<Object[]> list = SessionContext.getSqlSession().query_(
				"select count(id) from bpm_ru_procinst where procdef_id=? union all select count(id) from bpm_hi_procinst where procdef_id=?", 
				Arrays.asList(id, id));
		return Integer.parseInt(list.get(0)[0].toString()) > 0 || Integer.parseInt(list.get(1)[0].toString()) > 0;
	}
	
	/**
	 * 保存流程定义
	 * @param builder
	 * @return
	 */
	@Transaction
	public Result insert(ProcessDefinitionBuilder builder) {
		ProcessDefinition processDefinition = builder.build();
		ProcessDefinition old = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "query4Insert", processDefinition);
		
		// 新的流程定义, 进行save
		if(old == null) { 
			validateTypeId(processDefinition.getTypeId());
			SessionContext.getTableSession().save(processDefinition);
			return new Result(processDefinition);
		}
		
		// 旧的流程定义, 进行update
		if(old.getStateInstance() == State.DELETE)
			return new Result("保存失败, 已存在code为[%s], version为[%s]的流程", "jbpm.procdef.save.fail.code.version.exists", processDefinition.getCode(), processDefinition.getVersion());
		
		// 如果修改了类型, 则对类型进行验证
		if(processDefinition.getTypeId() != old.getTypeId())
			validateTypeId(processDefinition.getTypeId());
		
		// 没有修改流程定义的内容, 进行update
		if(old.getSignature().equals(processDefinition.getSignature())){ 
			processDefinition.setId(old.getId());
			processDefinition.setIsMajorVersion(old.getIsMajorVersion());
			processDefinition.setSubversion(old.getSubversion());
			processDefinition.setStateInstance(old.getStateInstance());
			processDefinition.setContent(null);
			processDefinition.setSignature(null);
			SessionContext.getTableSession().update(processDefinition);
		} 
		
		// 修改了内容, 但旧的流程处于初始化状态, 或忽略流程实例, 或不存在实例, 均进行update
		else if(old.getStateInstance() == State.INITIAL || builder.isIgnore() || !existsInstance(old.getId())) { 
			processDefinition.setId(old.getId());
			processDefinition.setIsMajorVersion(old.getIsMajorVersion());
			processDefinition.setSubversion(old.getSubversion());
			processDefinition.setStateInstance(old.getStateInstance());
			SessionContext.getTableSession().update(processDefinition);
			
			if(processDefinition.getStateInstance() == State.DEPLOY) // 刷新容器中的流程定义实例
				container.addProcess(processDefinition);
		}
		
		// 修改了内容, 且旧的流程定义存在实例, 根据strict值, 进行升级save, 或提示操作失败
		else { 
			if(!builder.isStrict()) 
				return new Result("保存失败, [%s]流程存在实例", "jbpm.procdef.save.fail.instance.exists", processDefinition.getName());
			
			// 修改旧流程定义的信息
			SessionContext.getSqlSession().executeUpdate(
					"update bpm_re_procdef set type_id=?, state=?, is_major_version=0, is_major_subversion=0 where code=? and version=?", 
					Arrays.asList(processDefinition.getTypeId(), State.INVALID.getValue(), processDefinition.getCode(), processDefinition.getVersion()));
			
			// 保存新的流程定义
			processDefinition.setIsMajorVersion(old.getIsMajorVersion());
			processDefinition.setSubversion(old.getSubversion()+1);
			processDefinition.setStateInstance(old.getStateInstance());
			SessionContext.getTableSession().save(processDefinition);
		}
		return new Result(processDefinition);
	}
	// 验证类型id值
	private void validateTypeId(int typeId) {
		if(typeId != 0 && SessionContext.getSqlSession().uniqueQuery_("select id from bpm_re_type where id=?", Arrays.asList(typeId)) == null)
			throw new RepositoryException("保存失败, 不存在id为["+typeId+"]的流程类型");
	}

	/**
	 * 流程部署
	 * @param id 
	 * @return
	 */
	@Transaction
	public Result deploy(int id) {
		ProcessDefinition processDefinition = SessionContext.getTableSession().uniqueQuery(ProcessDefinition.class, "select id, name, code, version, is_major_subversion, state, content_, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(processDefinition == null)
			throw new RepositoryException("部署失败, 不存在id为["+id+"]的流程");
		if(!processDefinition.isMajorSubversion())
			return new Result("部署失败, [%s]流程不是主要子版本", "jbpm.procdef.deploy.fail.not.major.subversion", processDefinition.getName());
		if(!processDefinition.getStateInstance().supportDeploy())
			return new Result("部署失败, [%s]流程处于[%s]状态", "jbpm.procdef.deploy.fail.state.error", processDefinition.getName(), processDefinition.getStateInstance());
		
		updateState_(id, State.DEPLOY);
		container.addProcess(processDefinition);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 取消流程部署
	 * @param id
	 * @return
	 */
	@Transaction
	public Result undeploy(int id) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, code, version, is_major_subversion, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(processDefinition == null)
			throw new RepositoryException("取消部署失败, 不存在id为["+id+"]的流程");
		if(!processDefinition.isMajorSubversion())
			return new Result("取消部署失败, [%s]流程不是主要子版本", "jbpm.procdef.undeploy.fail.not.major.subversion", processDefinition.getName());
		if(!processDefinition.getStateInstance().supportUnDeploy())
			return new Result("取消部署失败, [%s]流程处于[%s]状态", "jbpm.procdef.undeploy.fail.state.error", processDefinition.getName(), processDefinition.getState());
		
		updateState_(id, State.UNDEPLOY);
		container.deleteProcess(id);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 删除流程定义
	 * @param id
	 * @return
	 */
	@Transaction
	public Result delete(int id) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, code, version, is_major_subversion, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(processDefinition == null)
			throw new RepositoryException("删除失败, 不存在id为["+id+"]的流程");
		if(!processDefinition.isMajorSubversion())
			return new Result("删除失败, [%s]流程不是主要子版本", "jbpm.procdef.delete.fail.not.major.subversion", processDefinition.getName());
		if(!processDefinition.getStateInstance().supportDelete())
			return new Result("删除失败, [%s]流程处于[%s]状态", "jbpm.procdef.delete.fail.state.error", processDefinition.getName(), processDefinition.getState());
		
		updateState_(id, State.DELETE);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 撤销删除, 从{@link HandleState.DELETE}还原为{@link HandleState.UNDEPLOY};
	 * @param id
	 * @param targetState
	 * @return
	 */
	@Transaction
	public Result undoDelete(int id) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select name, is_major_subversion, state from bpm_re_procdef where id=?", Arrays.asList(id));
		if(processDefinition == null)
			throw new RepositoryException("撤销删除失败, 不存在id为["+id+"]的流程");
		if(!processDefinition.isMajorSubversion())
			return new Result("撤销删除失败, [%s]流程不是主要子版本", "jbpm.procdef.undo.delete.fail.not.major.subversion", processDefinition.getName());
		if(processDefinition.getStateInstance() != State.DELETE)
			return new Result("撤销删除失败, [%s]流程未处于[%s]状态", "jbpm.procdef.undo.delete.fail.state.error", processDefinition.getName(), State.DELETE);

		updateState_(id, State.UNDEPLOY);
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 物理删除流程定义; 如果存在实例, 则无法删除
	 * @param id
	 * @return
	 */
	@Transaction
	public Result delete4Physical(int id) {
		ProcessDefinition processDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select type_id, name, code, version, subversion, is_major_subversion, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(processDefinition == null)
			throw new RepositoryException("删除失败, 不存在id为["+id+"]的流程");
		if(!processDefinition.getStateInstance().supportPhysicalDelete())
			return new Result("删除失败, [%s]流程处于[%s]状态", "jbpm.procdef.physical.delete.fail.state.error", processDefinition.getName(), processDefinition.getState());
		if(existsInstance(id))
			return new Result("删除失败, [%s]流程存在实例, 如确实需要删除, 请先处理相关实例", "jbpm.procdef.physical.delete.fail.instance.exists", processDefinition.getName());
		
		// 进行删除操作
		SessionContext.getSqlSession().executeUpdate("delete bpm_re_procdef where id=?", Arrays.asList(id));
		
		// 如果被删除的流程是主要子版本, 且子版本值不为0, 需要尝试将上一个子版本的流程设置为主要子版本
		if(processDefinition.isMajorSubversion() && processDefinition.getSubversion() > 0) { 
			List<ProcessDefinition> beforeList = SessionContext.getSQLSession().limitQuery(ProcessDefinition.class, 1, 1, "ProcessDefinition", "querySubversions", processDefinition);
			if(beforeList.size() > 0) {
				ProcessDefinition before = beforeList.get(0);
				before.setIsMajorSubversion(1);
				before.setStateInstance(processDefinition.getStateInstance());
				SessionContext.getTableSession().update(before);
			}
		}
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 设置流程的主版本
	 * @param id
	 * @return
	 */
	public Result setMajorVersion(int id) {
		ProcessDefinition targetProcessDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select id, name, code, is_major_version, is_major_subversion, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(targetProcessDefinition == null)
			throw new RepositoryException("设置主版本失败, 不存在id为["+id+"]的流程");
		if(targetProcessDefinition.isMajorVersion())
			return new Result("设置主版本失败, [%s]流程就是主版本", "jbpm.procdef.set.major.version.fail.already.was", targetProcessDefinition.getName());
		if(!targetProcessDefinition.isMajorSubversion())
			return new Result("设置主版本失败, [%s]流程不是主要子版本", "jbpm.procdef.set.major.version.fail.not.major.subversion", targetProcessDefinition.getName());
		if(targetProcessDefinition.getStateInstance() != State.DEPLOY)
			return new Result("设置主版本失败, [%s]流程未处于[%s]状态", "jbpm.procdef.set.major.version.fail.unsupport", targetProcessDefinition.getName(), State.DEPLOY);
		
		Object[] majorVersionProcessDefinitionId = SessionContext.getSQLSession().uniqueQuery_("ProcessDefinition", "queryMajorVersionId", targetProcessDefinition);
		if(majorVersionProcessDefinitionId != null)
			SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set is_major_version=0 where id=?", Arrays.asList(majorVersionProcessDefinitionId[0]));
		SessionContext.getSqlSession().executeUpdate("update bpm_re_procdef set is_major_version=1 where id=?", Arrays.asList(id));
		return Result.getDefaultSuccessInstance();
	}
	
	/**
	 * 设置流程的主要子版本
	 * @param id
	 * @return
	 */
	@Transaction
	public Result setMajorSubversion(int id) {
		ProcessDefinition targetProcessDefinition = SessionContext.getSqlSession().uniqueQuery(ProcessDefinition.class, "select id, name, code, version, subversion, is_major_subversion, state, tenant_id from bpm_re_procdef where id=?", Arrays.asList(id));
		if(targetProcessDefinition == null)
			throw new RepositoryException("设置主要子版本失败, 不存在id为["+id+"]的流程");
		if(targetProcessDefinition.isMajorSubversion())
			return new Result("设置主要子版本失败, [%s]流程就是主要子版本", "jbpm.procdef.set.major.subversion.fail.already.was", targetProcessDefinition.getName());
		
		// 查询主要子版本的流程定义信息, 进行必要的数据交换后, 执行update
		ProcessDefinition majorSubversionProcessDefinition = SessionContext.getSQLSession().uniqueQuery(ProcessDefinition.class, "ProcessDefinition", "queryMajorSubversion", targetProcessDefinition);
		if(majorSubversionProcessDefinition == null)
			throw new RepositoryException("设置流程的主要子版本时, 未查询到code=["+targetProcessDefinition.getCode()+"], version=["+targetProcessDefinition.getVersion()+"]的主要子版本数据");
		
		int isMajorVersion = majorSubversionProcessDefinition.getIsMajorVersion();
		State state = majorSubversionProcessDefinition.getStateInstance();
		
		majorSubversionProcessDefinition.setIsMajorVersion(0);
		majorSubversionProcessDefinition.setIsMajorSubversion(0);
		majorSubversionProcessDefinition.setStateInstance(targetProcessDefinition.getStateInstance()); // targetProcessDefinition的state, 逻辑上只能是State.INVALID
		SessionContext.getTableSession().update(majorSubversionProcessDefinition);
		
		targetProcessDefinition.setIsMajorVersion(isMajorVersion);
		targetProcessDefinition.setIsMajorSubversion(1);
		targetProcessDefinition.setSubversion(majorSubversionProcessDefinition.getSubversion()+1);
		targetProcessDefinition.setStateInstance(state);
		SessionContext.getTableSession().update(targetProcessDefinition);
		
		return Result.getDefaultSuccessInstance();
	}
}
