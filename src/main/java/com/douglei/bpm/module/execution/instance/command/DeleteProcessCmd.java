package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Command;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.SourceType;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstance;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.execution.task.history.HistoryCarbonCopy;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class DeleteProcessCmd implements Command {
	protected HistoryProcessInstance processInstance;
	
	public DeleteProcessCmd(HistoryProcessInstanceEntity entity) {
		this.processInstance = entity.getProcessInstance();
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(!processInstance.getStateInstance().supportDelete()) 
			return new Result("删除失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.delete.fail.state.error", processInstance.getTitle());
		
		SessionContext.getSqlSession().executeUpdate("update bpm_hi_procinst set state=? where id=?", Arrays.asList(State.DELETE.getValue(), processInstance.getId()));
		
		// finished状态的流程实例, 可能会有抄送信息在运行表, 要对其也进行删除
		if(processInstance.getStateInstance() == State.FINISHED) {
			List<HistoryTask> tasks = SessionContext.getSqlSession().query(HistoryTask.class, "select taskinst_id from bpm_hi_task where procinst_id=? and type_='userTask'", Arrays.asList(processInstance.getProcinstId()));
			if(!tasks.isEmpty()) {
				List<HistoryCarbonCopy> ccs = SessionContext.getSQLSession().query(HistoryCarbonCopy.class, "TerminateProcess", "queryCCList", tasks);
				if(!ccs.isEmpty()) {
					SessionContext.getSQLSession().executeUpdate("TerminateProcess", "deleteCC", ccs);
					ccs.forEach(cc -> cc.setSourceTypeInstance(SourceType.BY_PROCINST_DELETED));
					SessionContext.getTableSession().save(ccs);
				}
			}
		}
		return Result.getDefaultSuccessInstance();
	}
}
