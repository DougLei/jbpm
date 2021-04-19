package com.douglei.bpm.module.runtime.instance.command;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.ProcessEngineBeans;
import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.instance.HistoryProcessInstanceEntity;
import com.douglei.bpm.module.runtime.instance.State;
import com.douglei.bpm.module.runtime.task.CarbonCopy;
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
	public ExecutionResult execute(ProcessEngineBeans processEngineBeans) {
		if(processInstance.getStateInstance().supportPhysicalDelete()) 
			return new ExecutionResult("撤销删除失败, [%s]流程实例处于[%s]状态", "jbpm.procinst.undo.delete.fail.state.error", processInstance.getTitle());
			
		SessionContext.getSqlSession().executeUpdate("update bpm_hi_procinst set state=? where id=?", Arrays.asList(processInstance.getState()-4, processInstance.getId()));
		
		// finished_delete状态的流程实例, 可能会有抄送信息在历史表, 要对其也进行撤销
		if(processInstance.getStateInstance() == State.FINISHED_DELETE) {
			List<HistoryTask> tasks = SessionContext.getSqlSession().query(HistoryTask.class, "select taskinst_id from bpm_hi_task where procinst_id=? and type_='userTask'", Arrays.asList(processInstance.getProcinstId()));
			if(!tasks.isEmpty()) {
				List<CarbonCopy> ccs = SessionContext.getSQLSession().query(CarbonCopy.class, "UndoDeleteProcess", "queryHistoryCCList", tasks);
				if(!ccs.isEmpty()) {
					SessionContext.getTableSession().save(ccs);
					SessionContext.getSQLSession().executeUpdate("UndoDeleteProcess", "deleteHistoryCC", ccs);
				}
			}
		}
		return ExecutionResult.getDefaultSuccessInstance();
	}
	
	
	
	
	
}
