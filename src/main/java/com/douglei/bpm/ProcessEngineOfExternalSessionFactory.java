package com.douglei.bpm;

import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.context.SessionFactoryContainer;
import com.douglei.orm.mapping.handler.MappingHandlerException;
import com.douglei.orm.mapping.handler.entity.DeleteMappingEntity;
import com.douglei.orm.mapping.handler.entity.MappingEntity;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 外置SessionFactory的流程引擎, 在销毁时不能销毁SessionFactory
 * @author DougLei
 */
class ProcessEngineOfExternalSessionFactory extends ProcessEngine {
	private boolean removeSessionFactory; // 销毁时, 是否要从SessionFactoryContainer中移除SessionFactory
	private String[] mappingCodes = {
			"BPM_RE_DELEGATION", "BPM_RE_LISTENER", "BPM_RE_PROCDEF", "BPM_RE_PROCTYPE", "BPM_RE_SUGGEST"
			}; // 流程的映射code数组, 在销毁时, 从SessionFactory中移除

	ProcessEngineOfExternalSessionFactory(String id, boolean removeSessionFactory) {
		super(id);
		this.removeSessionFactory = removeSessionFactory;
	}
	
	@Override
	public void destroy() throws MappingHandlerException {
		SessionFactory sessionFactory = null;
		if(removeSessionFactory) 
			sessionFactory = SessionFactoryContainer.getSingleton().remove(id, false);
		if(sessionFactory == null)
			sessionFactory = SessionFactoryContainer.getSingleton().get(id);
		
		List<MappingEntity> entities = new ArrayList<MappingEntity>(mappingCodes.length);
		for (String code : mappingCodes) {
			entities.add(new DeleteMappingEntity(code, false));
		}
		sessionFactory.getMappingHandler().execute(entities);
		super.destroy();
	}
}
