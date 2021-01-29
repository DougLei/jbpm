package com.douglei.bpm.query.extend.mapping;

import java.io.InputStream;

import com.douglei.orm.mapping.Mapping;
import com.douglei.orm.mapping.type.MappingType;

/**
 * 查询sql映射类型
 * @author DougLei
 */
public class QuerySqlMappingType extends MappingType {
	public static final String NAME = "query-sql";
	
	public QuerySqlMappingType() {
		super(NAME, ".qsmp.xml", 70, false);
	}

	@Override
	public Mapping parse(InputStream input) throws Exception {
		return new QuerySqlMappingParser().parse(input);
	}
}
