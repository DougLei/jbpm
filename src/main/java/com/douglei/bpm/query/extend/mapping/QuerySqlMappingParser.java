package com.douglei.bpm.query.extend.mapping;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;

import com.douglei.bpm.query.extend.mapping.metadata.ContentMetadata;
import com.douglei.bpm.query.extend.mapping.metadata.ParameterStandardMetadata;
import com.douglei.bpm.query.extend.mapping.metadata.QuerySqlMetadata;
import com.douglei.orm.mapping.MappingParser;
import com.douglei.orm.mapping.impl.MappingParserContext;
import com.douglei.orm.mapping.metadata.parser.MetadataParseException;
import com.douglei.orm.util.Dom4jUtil;
import com.douglei.tools.utils.StringUtil;

/**
 * 
 * @author DougLei
 */
class QuerySqlMappingParser extends MappingParser<QuerySqlMapping>{
	private QuerySqlMetadata querySqlMetadata;
	
	@Override
	public QuerySqlMapping parse(InputStream input) throws Exception {
		Document document = MappingParserContext.getSAXReader().read(input);
		Element rootElement = document.getRootElement();
		
		// 创建QuerySqlMetadata实例
		Element querySqlElement = Dom4jUtil.getElement("query-sql", rootElement);
		querySqlMetadata = new QuerySqlMetadata(getNameAttributeValue("<query-sql>", querySqlElement));
		
		// 设置sql内容
		querySqlMetadata.setContent(parseContent(Dom4jUtil.getElement("content", querySqlElement)));
		
		// 设置参数标准
		setParameterStandardMap(querySqlElement.element("parameter-standards"));
		
		return new QuerySqlMapping(querySqlMetadata);
	}

	// 获取name的属性值
	private String getNameAttributeValue(String elementName, Element element) {
		String name = element.attributeValue("name");
		if(StringUtil.isEmpty(name)) 
			throw new MetadataParseException(elementName + "元素的name属性值不能为空");
		return name;
	}
	
	// 解析sql内容
	private ContentMetadata parseContent(Element element) {
		String content = element.getTextTrim();
		if(StringUtil.isEmpty(content))
			throw new MetadataParseException("<content>元素中的sql内容不能为空");
		
		boolean package_ = !"false".equalsIgnoreCase(element.attributeValue("package"));
		return new ContentMetadata(package_, package_?false:"true".equalsIgnoreCase(element.attributeValue("append")), content);
	}
	
	// 设置参数标准
	private void setParameterStandardMap(Element element) {
		if(element == null)
			return;
		
		for (Object object : element.elements("parameter-standard")) 
			querySqlMetadata.addParameterStandard(parseParameterStandard((Element)object));
	}

	// 解析参数标准
	private ParameterStandardMetadata parseParameterStandard(Element element) {
		String name = getNameAttributeValue("<parameter-standard>", element);
		// TODO 
		
		
		return null;
	}
}
