package com.douglei.bpm.process.mapping.parser.task.user;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.douglei.bpm.bean.annotation.Autowired;
import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.api.user.option.OptionHandler;
import com.douglei.bpm.process.api.user.option.OptionHandlerContainer;
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
	List<Option> parse(String id, String name, List<Element> elements) throws ProcessParseException{
		if(elements.isEmpty())
			return null;
		
		List<Option> options = new ArrayList<Option>(elements.size());
		
		OptionHandler optionHandler = null;
		String optionName, optionOrder;
		int order;
		Option option = null;
		for (Element element : elements) {
			optionHandler = optionHandlerContainer.get(element.attributeValue("type"));
			if(optionHandler == null)
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><option>的type属性值["+element.attributeValue("type")+"]不合法");
			
			// option的name
			optionName = element.attributeValue("name");
			if(StringUtil.isEmpty(optionName))
				optionName = element.attributeValue("type");
			
			// option的order
			order = 0;
			optionOrder = element.attributeValue("order");
			if(DataTypeValidateUtil.isInteger(optionOrder))
				order = Integer.parseInt(optionOrder);
			
			option = optionHandler.parse(optionName, order, id, name, element);
			if(option == null)
				throw new ProcessParseException("<userTask id="+id+" name="+name+"><option type="+element.attributeValue("type")+">的配置不合法");
			if(options.size() > 0 && options.contains(option))
				throw new ProcessParseException("<userTask id="+id+" name="+name+">下配置了重复type("+element.attributeValue("type")+")的<option>");
			options.add(option);
		}
		return options;
	}
}