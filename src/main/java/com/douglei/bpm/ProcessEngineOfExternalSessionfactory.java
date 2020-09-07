package com.douglei.bpm;

import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.configuration.environment.mapping.MappingEntity;
import com.douglei.orm.configuration.impl.element.environment.mapping.DeleteMappingEntity;
import com.douglei.orm.context.SessionFactoryRegister;
import com.douglei.orm.core.dialect.mapping.MappingExecuteException;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 外置SessionFactory的流程引擎, 在销毁时不能销毁SessionFactory
 * @author DougLei
 */
public class ProcessEngineOfExternalSessionfactory extends ProcessEngine {
	private boolean removeSessionFactory; // 销毁时, 是否要从SessionFactoryRegister中移除SessionFactory
	private String[] mappingCodes = {
			"BPM_RE_DELEGATION", "BPM_RE_LISTENER", "BPM_RE_PROCDEF", "BPM_RE_PROCDEF_CONTENT", "BPM_RE_PROCTYPE", "BPM_RE_SUGGEST"
			}; // 流程的映射code数组, 在销毁时, 从SessionFactory中移除

	ProcessEngineOfExternalSessionfactory(String id, boolean removeSessionFactory) {
		super(id);
		this.removeSessionFactory = removeSessionFactory;
	}
	
	@Override
	public void destroy() throws MappingExecuteException {
		SessionFactory sessionFactory = null;
		if(removeSessionFactory) 
			sessionFactory = SessionFactoryRegister.getSingleton().remove(id, false);
		if(sessionFactory == null)
			sessionFactory = SessionFactoryRegister.getSingleton().get(id);
		
		List<MappingEntity> entities = new ArrayList<MappingEntity>(mappingCodes.length);
		for (String code : mappingCodes) {
			entities.add(new DeleteMappingEntity(code, false));
		}
		sessionFactory.getMappingProcessor().execute(entities);
	}
}
