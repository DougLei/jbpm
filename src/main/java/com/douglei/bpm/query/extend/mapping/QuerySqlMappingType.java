package com.douglei.bpm.query.extend.mapping;

import java.io.InputStream;

import com.douglei.orm.mapping.Mapping;
import com.douglei.orm.mapping.type.MappingType;

/**
 * 查询sql资源
 * @author DougLei
 */
public class QuerySqlMappingType extends MappingType {

	public QuerySqlMappingType() {
		super("querysql", ".qsmp.xml", 70, false);
	}

	@Override
	public Mapping parse(InputStream input) throws Exception {
		return new QuerySqlMappingParser().parse(input);
	}
}
