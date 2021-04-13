package com.douglei.bpm.process.mapping.parser.task.user;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.api.user.option.OptionHandlerContainer;
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
	private OptionHandlerContainer optionHandlerContainer;
	
	/**
	 * 解析options
	 * @param id
	 * @param name
	 * @param elements
	 * @return
	 * @throws ProcessParseException
	 */
	List<Option> parse(UserTaskMetadata metadata, List<Element> elements) throws ProcessParseException{
		if(elements.isEmpty())
			return null;
		
		List<Option> options = new ArrayList<Option>(elements.size());
		for (Element element : elements) {
			OptionHandler optionHandler = optionHandlerContainer.get(element.attributeValue("type"));
			if(optionHandler == null)
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><option>的type属性值["+element.attributeValue("type")+"]不合法");
			
			// option的name
			String optionName = element.attributeValue("name");
			if(StringUtil.isEmpty(optionName))
				optionName = element.attributeValue("type");
			
			// option的order
			int order = 0;
			String optionOrder = element.attributeValue("order");
			if(DataTypeValidateUtil.isInteger(optionOrder))
				order = Integer.parseInt(optionOrder);
			
			Option option = optionHandler.parse(optionName, order, metadata, element);
			if(option == null)
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+"><option type="+element.attributeValue("type")+">的配置不合法");
			if(options.size() > 0 && options.contains(option))
				throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">下配置了重复type("+element.attributeValue("type")+")的<option>");
			options.add(option);
		}
		return options;
	}
}