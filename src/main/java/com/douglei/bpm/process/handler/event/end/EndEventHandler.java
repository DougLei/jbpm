package com.douglei.bpm.process.handler.event.end;

import java.util.Arrays;
import java.util.List;

import com.douglei.bpm.module.ExecutionResult;
import com.douglei.bpm.module.history.instance.HistoryProcessInstance;
import com.douglei.bpm.module.history.task.HistoryTask;
import com.douglei.bpm.module.runtime.instance.ProcessInstance;
import com.douglei.bpm.process.handler.HandleParameter;
import com.douglei.bpm.process.handler.TaskHandler;
import com.douglei.bpm.process.metadata.event.EndEventMetadata;
import com.douglei.orm.context.SessionContext;

/**
 * 
 * @author DougLei
 */
public class EndEventHandler extends TaskHandler<EndEventMetadata, HandleParameter> {

	@Override
	public ExecutionResult startup() {
		return handle();
	}

	@Override
	public ExecutionResult handle() {
		// 创建流程任务(因为是结束事件, 所以直接创建历史任务结束即可)
		HistoryTask historyTask = new HistoryTask(
				handleParameter.getProcessMetadata().getId(), 
				handleParameter.getProcessInstanceId(),
				handleParameter.getTask().getParentTaskinstId(),
				taskMetadataEntity.getTaskMetadata());
		SessionContext.getTableSession().save(historyTask);
		
		if(historyTask.getParentTaskinstId() == null) { // TODO 这里还要判断, 如果parentTaskInstid不为空, 那是不是最后一个不为空的数据, 是的话, 流程也就结束了
			// 将实例保存到历史
			List<Object> procinstId = Arrays.asList(handleParameter.getProcessInstanceId());
			ProcessInstance processInstance = SessionContext.getTableSession().uniqueQuery(ProcessInstance.class, "select * from bpm_ru_procinst where procinst_id=?", procinstId);
			SessionContext.getSqlSession().executeUpdate("delete bpm_ru_procinst where procinst_id=?", procinstId);
			SessionContext.getTableSession().save(new HistoryProcessInstance(processInstance, null));
			
			// 结束变量调度
			beanInstances.getVariableScheduler().endDispatch(handleParameter.getProcessInstanceId());
		}
		return ExecutionResult.getDefaultSuccessInstance();
	}
}
