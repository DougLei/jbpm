package com.douglei.bpm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.douglei.orm.mapping.handler.MappingHandlerException;
import com.douglei.orm.mapping.handler.entity.MappingEntity;
import com.douglei.orm.mapping.handler.entity.impl.DeleteMappingEntity;
import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * 外置SessionFactory的流程引擎, 在销毁时不能销毁SessionFactory
 * @author DougLei
 */
class ProcessEngineByExternalSessionFactory extends ProcessEngine {
	private List<String> mappingFiles;// 流程的映射文件集合, 在销毁时, 根据文件获取映射的code, 并从SessionFactory中移除

	ProcessEngineByExternalSessionFactory(SessionFactory sessionFactory, List<String> mappingFiles) {
		super(sessionFactory);
		this.mappingFiles = mappingFiles;
	}
	
	@Override
	public void destroy() throws MappingHandlerException {
		List<MappingEntity> entities = new ArrayList<MappingEntity>(mappingFiles.size());
		mappingFiles.forEach(mappingFile -> {
			String filename = mappingFile.substring(mappingFile.lastIndexOf(File.separatorChar)+1);
			entities.add(new DeleteMappingEntity(filename.substring(0, filename.indexOf(".")), false));
		});
		sessionFactory.getMappingHandler().execute(entities);
	}
}
