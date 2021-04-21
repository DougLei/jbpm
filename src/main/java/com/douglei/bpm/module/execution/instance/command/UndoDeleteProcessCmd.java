package com.douglei.bpm.module.execution.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.Result;
import com.douglei.bpm.module.execution.instance.State;
import com.douglei.bpm.module.execution.instance.history.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.execution.task.history.HistoryTask;
import com.douglei.bpm.module.execution.task.runtime.CarbonCopy;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class UndoDeleteProcessCmd extends DeleteProcessCmd {
	
	public UndoDeleteProcessCmd(HistoryProcessInstanceEntity entity) {
		super(entity);
	}

	@Override
	public Result execute(ProcessEngineBeans processEngineBeans) {
		if(!processInstance.getStateInstance().supportPhysicalDelete()) 
			return new Result("撤销删除失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.undo.delete.fail.state.error", processInstance.getTitle(), processInstance.getStateInstance().name());
		
		State targetState = processInstance.getSuspendTime()==null?State.FINISHED:State.TERMINATED;
		SessionContext.getSqlSession().executeUpdate(
				"update bpm_hi_procinst set state=? where id=?", Arrays.asList(targetState.getValue(), processInstance.getId()));
		
		// FINISHED状态的流程实例, 可能会有抄送信息在历史表, 要对其也进行撤销(恢复到运行表)
		if(targetState == State.FINISHED) {
			List<HistoryTask> tasks = SessionContext.getSqlSession().query(HistoryTask.class, "select taskinst_id from bpm_hi_task where procinst_id=? and type_='userTask'", Arrays.asList(processInstance.getProcinstId()));
			if(!tasks.isEmpty()) {
				List<CarbonCopy> ccs = SessionContext.getSQLSession().query(CarbonCopy.class, "UndoDeleteProcess", "queryHistoryCCList", tasks);
				if(!ccs.isEmpty()) {
					SessionContext.getSQLSession().executeUpdate("UndoDeleteProcess", "deleteHistoryCC", ccs);
					SessionContext.getTableSession().save(ccs);
				}
			}
		}
		return Result.getDefaultSuccessInstance();
	}
	
	
	
	
	
}
