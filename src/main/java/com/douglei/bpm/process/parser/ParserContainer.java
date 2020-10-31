package com.douglei.bpm.process.parser;

import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executer.task.Task;
import com.douglei.bpm.process.parser.element.TaskElement;
import com.douglei.bpm.process.parser.impl.ProcessParser;
import com.douglei.bpm.process.parser.impl.flow.FlowParser;
import com.douglei.bpm.process.parser.impl.task.event.StartEventParser;
import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 解析器容器
 * 这里不包含{@link StartEventParser}和{@link FlowParser}两个解析器, 它们在中{@link ProcessParser}独立使用
 * @author DougLei
 */
@Bean(transaction = false)
public class ParserContainer {
	private Map<String, Parser<TaskElement, ? extends Task>> parserMap = new HashMap<String, Parser<TaskElement,? extends Task>>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" } )
	public ParserContainer() {
		Parser parser;
		for (String clazz : new ClassScanner().scan(getClass().getPackage().getName() + ".impl")) {
			if(ClassLoadUtil.loadClass(clazz).getAnnotation(ParserBean.class) != null) {
				parser = (Parser) ConstructorUtil.newInstance(clazz);
				parserMap.put(parser.elementName(), parser);
			}
		}
	}
	
	/**
	 * 解析
	 * @param taskElement
	 * @return
	 */
	public Task parse(TaskElement taskElement) {
		Parser<TaskElement, ? extends Task> parser = parserMap.get(taskElement.getElement().getName());
		if(parser == null)
			throw new ProcessParseException("无法解析<"+taskElement.getElement().getName()+">标签");
		return parser.parse(taskElement);
	}
}
