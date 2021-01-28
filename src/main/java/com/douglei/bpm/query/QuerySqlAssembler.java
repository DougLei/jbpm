package com.douglei.bpm.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.query.extend.mapping.QuerySqlMappingType;
import com.douglei.bpm.query.extend.mapping.metadata.OperatorEntity;
import com.douglei.bpm.query.extend.mapping.metadata.ParameterStandardMetadata;
import com.douglei.bpm.query.extend.mapping.metadata.QuerySqlMetadata;
import com.douglei.bpm.query.parameter.AbstractParameter;
import com.douglei.orm.context.SessionFactoryContainer;

/**
 * 
 * @author DougLei
 */
public class QuerySqlAssembler {
	private QuerySqlMetadata metadata;
	private StringBuilder sql;
	
	
	/**
	 * 
	 * @param name querysql的映射名
	 * @param parameters 参数集合
	 */
	public QuerySqlAssembler(String name, List<AbstractParameter> parameters) {
		this.metadata = (QuerySqlMetadata) SessionFactoryContainer.getSingleton().get().getMappingHandler().getMapping(name, QuerySqlMappingType.NAME).getMetadata();
	}
	
	/**
	 * 装配查询sql实体
	 * @return
	 * @throws QuerySqlAssembleException
	 */
	public QuerySqlEntity assembling() throws QuerySqlAssembleException{
		
		
		
		return null;
	}
	
	
	
	/**
	 * 参数标准的Map
	 * @author DougLei
	 */
	private class ParameterStandardMap {
		Map<String, ParameterStandardEntity> map;
		ParameterStandardMap(List<ParameterStandardMetadata> parameterStandards) {
			map = new HashMap<String, QuerySqlAssembler.ParameterStandardEntity>();
			parameterStandards.forEach(parameterStandard -> map.put(parameterStandard.getName(), new ParameterStandardEntity(parameterStandard)));
		}
	}
	
	/**
	 * 具体的参数标准
	 * @author DougLei
	 */
	private class ParameterStandardEntity{
		OperatorHandler operatorHandler;
		public ParameterStandardEntity(ParameterStandardMetadata parameterStandard) {
			
			
//			operatorHandler = new OperatorHandler(parameterStandard.getOperatorEntities());
		}
	}
	
	/**
	 * 运算处理器
	 * @author DougLei
	 */
	private class OperatorHandler{
		OperatorHandler(OperatorEntity[] entities) {
			
		}
	}
}
