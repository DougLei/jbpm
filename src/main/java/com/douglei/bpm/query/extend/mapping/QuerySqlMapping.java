package com.douglei.bpm.query.extend.mapping;

import com.douglei.orm.mapping.Mapping;
import com.douglei.orm.mapping.metadata.AbstractMetadata;

/**
 * 
 * @author DougLei
 */
public class QuerySqlMapping extends Mapping {

	public QuerySqlMapping(AbstractMetadata metadata) {
		super(QuerySqlMappingType.NAME, metadata);
	}
}
