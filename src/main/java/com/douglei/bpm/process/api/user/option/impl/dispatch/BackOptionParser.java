package com.douglei.bpm.process.api.user.option.impl.dispatch;

import org.dom4j.Element;

import com.douglei.bpm.process.api.user.option.OptionTypeConstants;
import com.douglei.bpm.process.mapping.metadata.task.user.UserTaskMetadata;
import com.douglei.bpm.process.mapping.metadata.task.user.option.Option;
import com.douglei.bpm.process.mapping.metadata.task.user.option.dispatch.BackOption;
import com.douglei.bpm.process.mapping.parser.ProcessParseException;
import com.douglei.tools.datatype.DataTypeValidateUtil;

/**
 * 
 * @author DougLei
 */
public class BackOptionParser extends AbstractDispatchOptionParser{
	
	@Override
	public String getType() {
		return OptionTypeConstants.BACK;
	}
	
	@Override
	protected Option parse(String name, int order, boolean suggestIsRequired, boolean attitudeIsRequired, UserTaskMetadata metadata, Element parameterElement, Element element) {
		String value = parameterElement.attributeValue("steps");
		if(!DataTypeValidateUtil.isInteger(value))
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"<parameter>的steps属性值不合法");
		
		int steps = Integer.parseInt(value);
		if(steps < 1)
			throw new ProcessParseException("<userTask id="+metadata.getId()+" name="+metadata.getName()+">"+getXmlStruct()+"<parameter>的steps属性值不合法");
		
		return new BackOption(OptionTypeConstants.BACK, name, order, steps, suggestIsRequired, attitudeIsRequired);
	}
}
