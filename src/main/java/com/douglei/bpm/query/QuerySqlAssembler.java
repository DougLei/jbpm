package com.douglei.bpm.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.douglei.bpm.query.extend.mapping.QuerySqlMappingType;
import com.douglei.bpm.query.extend.mapping.metadata.ContentMetadata;
import com.douglei.bpm.query.extend.mapping.metadata.Operator;
import com.douglei.bpm.query.extend.mapping.metadata.QuerySqlMetadata;
import com.douglei.orm.context.SessionFactoryContainer;

/**
 * 
 * @author DougLei
 */
public class QuerySqlAssembler {
	private QuerySqlMetadata metadata;
	private List<AbstractParameter> parameters;
	
	/**
	 * 
	 * @param name querysql的映射名
	 * @param parameters 参数集合
	 */
	public QuerySqlAssembler(String name, List<AbstractParameter> parameters) {
		this.metadata = (QuerySqlMetadata) SessionFactoryContainer.getSingleton().get().getMappingHandler().getMapping(name, QuerySqlMappingType.NAME).getMetadata();
		this.parameters = parameters;
	}
	
	/**
	 * 装配查询sql实体
	 * @return
	 * @throws QuerySqlAssembleException
	 */
	public QuerySqlEntity assembling() throws QuerySqlAssembleException{
		if(this.parameters == null || this.parameters.isEmpty()) {
			if(this.metadata.existsRequired())
				throw new QuerySqlAssembleException("装配code为["+metadata.getCode()+"]的query-sql映射时, 未传入必要的参数");
			return new QuerySqlEntity(this.metadata.getContent().getContent(), null);
		}
		
		// 进行装配
		StringBuilder sql = new StringBuilder(400);
		List<Object> parameters = new ArrayList<Object>();
		Map<String, Map<Operator, Integer>> counter = new HashMap<String, Map<Operator,Integer>>();
		for (AbstractParameter parameter : this.parameters) 
			parameter.assembleSQL(sql, parameters, counter, this.metadata.getParameterStandardMap());
		
		// 验证是否没有传入必要的参数
		if(this.metadata.existsRequired()) {
			this.metadata.getParameterStandardMap().values().forEach(psm -> {
				if(psm.isRequired() && !counter.containsKey(psm.getName()))
					throw new QuerySqlAssembleException("装配code为["+metadata.getCode()+"]的query-sql映射时, 未传入必要的参数 ==> ["+psm.getName()+"]" );
			});
		}
		
		// 创建QuerySqlEntity实例
		ContentMetadata content = this.metadata.getContent();
		if(content.isPackage())
			return new QuerySqlEntity("select * from ("+content.getContent()+") _sub_qsmp_ where " + sql, parameters);
		
		if(content.isAppend())
			return new QuerySqlEntity(content.getContent()+" and " + sql, parameters);
		return new QuerySqlEntity(content.getContent()+" where " + sql, parameters);
	}
}
