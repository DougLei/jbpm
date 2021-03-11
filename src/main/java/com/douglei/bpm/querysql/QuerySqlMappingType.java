package com.douglei.bpm.querysql;

import java.io.InputStream;

import com.douglei.orm.mapping.MappingSubject;
import com.douglei.orm.mapping.MappingType;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;

/**
 * 查询sql映射类型
 * @author DougLei
 */
public class QuerySqlMappingType extends MappingType {
	private static QuerySqlMappingParser parser = new QuerySqlMappingParser();
	public static final String NAME = "query-sql";
	public static final String FILE_SUFFIX = ".qsmp.xml";
	
	public QuerySqlMappingType() {
		super(NAME, FILE_SUFFIX, 70, false);
	}

	@Override
	public MappingSubject parse(AddOrCoverMappingEntity entity, InputStream input) throws Exception {
		return parser.parse(entity, input);
	}
}
