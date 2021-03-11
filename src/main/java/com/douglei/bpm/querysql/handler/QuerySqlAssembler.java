package com.douglei.bpm.querysql.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglei.bpm.querysql.QuerySqlMappingType;
import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;
import com.douglei.orm.context.SessionFactoryContainer;

/**
 * 
 * @author DougLei
 */
public class QuerySqlAssembler {
	private static final Logger logger = LoggerFactory.getLogger(QuerySqlAssembler.class);
	private QuerySqlEntity entity;
	
	/**
	 * 
	 * @param name
	 * @param parameters
	 * @param container
	 */
	QuerySqlAssembler(String name, List<AbstractParameter> parameters, SessionFactoryContainer container) {
		QuerySqlMetadata metadata = (QuerySqlMetadata) container.get().getMappingHandler().getMapping(name, QuerySqlMappingType.NAME, true).getMetadata();
		entity = new QuerySqlEntity(metadata.getSql());
		
		if(parameters != null) {
			for(int i=0;i<parameters.size();i++) {
				parameters.get(i).assembleSQL(entity, metadata);
				if(i < parameters.size()-1)
					entity.appendConditionSQLNext(parameters.get(i).next);
			}
		}
		
		// 验证是否没有传入必要的参数
		metadata.getParameterMap().values().forEach(parameter -> {
			if(parameter.isRequired() && !entity.existsInConditionSQL(parameter.getName()))
				throw new QuerySqlAssembleException("装配code为["+metadata.getCode()+"]的query-sql时, 未传入必要的参数["+parameter.getName()+"]");
		});
		
		logger.debug("装配后的QuerySqlEntity: {}", entity);
	}

	/**
	 * 获取装配后的查询sql实体类实例
	 * @return
	 */
	public QuerySqlEntity getQuerySqlEntity() {
		return entity;
	}
}