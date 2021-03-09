package com.douglei.bpm.querysql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.douglei.bpm.querysql.metadata.ContentMetadata;
import com.douglei.bpm.querysql.metadata.DataType;
import com.douglei.bpm.querysql.metadata.Operator;
import com.douglei.bpm.querysql.metadata.OperatorEntity;
import com.douglei.bpm.querysql.metadata.ParameterStandardMetadata;
import com.douglei.bpm.querysql.metadata.QuerySqlMetadata;
import com.douglei.orm.configuration.Dom4jUtil;
import com.douglei.orm.mapping.MappingParseToolContext;
import com.douglei.orm.mapping.MappingParser;
import com.douglei.orm.mapping.MappingSubject;
import com.douglei.orm.mapping.handler.entity.AddOrCoverMappingEntity;
import com.douglei.orm.mapping.metadata.MetadataParseException;
import com.douglei.tools.StringUtil;

/**
 * 
 * @author DougLei
 */
class QuerySqlMappingParser extends MappingParser{
	private QuerySqlMetadata querySqlMetadata;
	
	@Override
	public MappingSubject parse(AddOrCoverMappingEntity entity, InputStream input) throws Exception {
		Element rootElement = MappingParseToolContext.getMappingParseTool().getSAXReader().read(input).getRootElement();
		
		// 创建QuerySqlMetadata实例
		Element querySqlElement = Dom4jUtil.getElement(QuerySqlMappingType.NAME, rootElement);
		querySqlMetadata = new QuerySqlMetadata(getNameAttributeValue("<query-sql>", querySqlElement));
		
		// 设置sql内容
		querySqlMetadata.setContent(parseContent(Dom4jUtil.getElement("content", querySqlElement)));
		
		// 设置参数标准
		setParameterStandardMap(querySqlElement.element("parameter-standards"));
		
		return buildMappingSubjectByDom4j(new QuerySqlMapping(querySqlMetadata), rootElement);
	}

	// 获取name的属性值
	private String getNameAttributeValue(String elementName, Element element) {
		String name = element.attributeValue("name");
		if(StringUtil.isEmpty(name)) 
			throw new MetadataParseException(elementName + "的name属性值不能为空");
		return name;
	}
	
	// 解析sql内容
	private ContentMetadata parseContent(Element element) {
		String content = element.getTextTrim();
		if(StringUtil.isEmpty(content))
			throw new MetadataParseException("<content>中的sql内容不能为空");
		
		boolean package_ = !"false".equalsIgnoreCase(element.attributeValue("package"));
		return new ContentMetadata(package_, package_?false:"true".equalsIgnoreCase(element.attributeValue("append")), content);
	}
	
	// 设置参数标准
	@SuppressWarnings("unchecked")
	private void setParameterStandardMap(Element element) {
		if(element == null)
			return;
		
		List<Element> elements = element.elements("parameter-standard");
		if(elements.isEmpty())
			return;
		
		Map<String, ParameterStandardMetadata> psmMap= new HashMap<String, ParameterStandardMetadata>();
		ParameterStandardMetadata psm = null;
		boolean existsRequired = false;
		for (Element elem: elements) {
			psm = parseParameterStandard(elem);
			if(psmMap.containsKey(psm.getName()))
				throw new MetadataParseException("重复配置了name为["+psm.getName()+"]的<parameter-standard>");
			psmMap.put(psm.getName(), psm);
			
			if(psm.isRequired() && !existsRequired)
				existsRequired = true;
		}
		querySqlMetadata.setParameterStandardMap(existsRequired, psmMap);
	}

	// 解析参数标准
	@SuppressWarnings("unchecked")
	private ParameterStandardMetadata parseParameterStandard(Element element) {
		ParameterStandardMetadata psm = new ParameterStandardMetadata(
				getNameAttributeValue("<parameter-standard>", element),
				DataType.valueOf(element.attributeValue("dataType").toUpperCase()),
				"true".equalsIgnoreCase(element.attributeValue("required")));
		
		// 解析支持的操作
		List<Element> elements = element.elements("operator");
		if(elements.isEmpty()) 
			return psm;
		
		List<OperatorEntity> entities = new ArrayList<OperatorEntity>(elements.size());
		OperatorEntity entity = null;
		for (Element elem : elements) {
			entity = new OperatorEntity(Operator.valueOf(elem.attributeValue("name").toUpperCase()), parseTimes(elem.attributeValue("times")));
			if(!psm.getDataType().support(entity.getOperator()))
				throw new MetadataParseException("["+psm.getDataType()+"]类型不支持使用["+entity.getOperator()+"]操作");
			entities.add(entity);
		}
		
		psm.setOperatorEntities(entities);
		return psm;
	}
	private int parseTimes(String str) {
		if(StringUtil.isEmpty(str))
			return 0;
		
		int times = Integer.parseInt(str);
		if(times < 0)
			return 0;
		return times;
	}

	
//	/**
//	 * 添加参数支持的操作实体
//	 * @param entity
//	 */
//	public void addOperatorEntity(OperatorEntity entity) {
//		if(operatorEntities == null)
//			operatorEntities = new ArrayList<OperatorEntity>();
//		else if(operatorEntities.contains(entity))
//			throw new IllegalArgumentException("重复配置了name为["+entity.getOperator()+"]的<operator>元素");
//		operatorEntities.add(entity);
//	}
}
