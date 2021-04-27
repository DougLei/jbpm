package com.douglei.bpm.process.mapping.parser.task.user;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.APIContainer;
import com.douglei.bpm.process.api.user.option.AbstractOptionParser;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.StringUtil;
import com.douglei.tools.datatype.DataTypeValidateUtil;

/**
 *                                          
 * @author DougLei
 */
@Bean
public class OptionParser {
	
	@Autowired
	private APIContainer apiContainer;
	
	/**
	 * 解析options
	 * @param metadata
	 * @param elements
	 * @return
	 * @throws ProcessParseException
	 */
	List<Option> parse(UserTaskMetadata metadata, List<Element> elements) throws ProcessParseException{
		if(elements.isEmpty())
			return null;
		
		List<Option> options = new ArrayList<Option>(elements.size());
		for (Element element : elements) {
			AbstractOptionParser parser = apiContainer.getOptionParser(element.attributeValue("type"));
			if(parser == null)
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><option>的type属性值["+element.attributeValue("type")+"]不合法");
			
			if(existsRepeatedTypeOption(parser, options))
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">下配置了重复type("+parser.getType()+")的<option>");
			
			// option的name
			String name = element.attributeValue("name");
			if(StringUtil.isEmpty(name))
				name = parser.getType();
			
			// option的order
			int order = 0;
			String optionOrder = element.attributeValue("order");
			if(DataTypeValidateUtil.isInteger(optionOrder))
				order = Integer.parseInt(optionOrder);
			
			options.add(parser.parse(name, order, metadata, element));
		}
		
		if(options.size() > 1)
			options.sort(OPTION_SORT_COMPARATOR);
		
		return options;
	}
	
	// option的排序比较器
	private static final Comparator<Option> OPTION_SORT_COMPARATOR = new Comparator<Option>() {
		@Override
		public int compare(Option o1, Option o2) {
			if(o1.getOrder() == o2.getOrder()) 
				return 0;
			else if(o1.getOrder() < o2.getOrder()) 
				return -1;
			return 1;
		}
	};

	// 是否存在重复type的Option
	private boolean existsRepeatedTypeOption(AbstractOptionParser parser, List<Option> options) {
		if(options.isEmpty())
			return false;
		
		if(parser.supportMultiple())
			return false;
		
		for (Option option : options) {
			if(option.getType().equals(parser.getType()))
				return true;
		}
		return false;
	}
}