package com.douglei.bpm.querysql;

import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;
import com.douglei.orm.mapping.Mapping;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMapping extends Mapping {

	public QuerySqlMapping(QuerySqlMetadata metadata) {
		super(QuerySqlMappingType.NAME, metadata);
	}
}
