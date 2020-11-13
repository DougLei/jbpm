package com.douglei.bpm.process.parser;

import java.util.HashMap;
import java.util.Map;

import com.douglei.bpm.bean.annotation.Bean;
import com.douglei.bpm.process.executer.task.Task;
import com.douglei.bpm.process.parser.flow.FlowParser;
import com.douglei.bpm.process.parser.task.TaskMetadata;
import com.douglei.bpm.process.parser.task.event.StartEventParser;
import com.douglei.tools.instances.resource.scanner.impl.ClassScanner;
import com.douglei.tools.utils.reflect.ClassLoadUtil;
import com.douglei.tools.utils.reflect.ConstructorUtil;

/**
 * 解析器容器, 这里不包含{@link StartEventParser}和{@link FlowParser}两个解析器, 它们在中{@link ProcessParser}独立使用
 * @author DougLei
 */
@Bean(isTransaction = false)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParserContainer {
	private static Map<String, Parser<TaskMetadata, ? extends Task>> parserMap = new HashMap<String, Parser<TaskMetadata,? extends Task>>();
	static {
		Parser parser;
		for (String clazz : new ClassScanner().scan(ParserContainer.class.getPackage().getName() + ".impl")) {
			if(ClassLoadUtil.loadClass(clazz).getAnnotation(ParserBean.class) != null) {
				parser = (Parser) ConstructorUtil.newInstance(clazz);
				parserMap.put(parser.elementName(), parser);
			}
		}
	}
	
	/**
	 * 解析
	 * @param taskMetadata
	 * @return
	 */
	public Task parse(TaskMetadata taskMetadata) {
		Parser<TaskMetadata, ? extends Task> parser = parserMap.get(taskMetadata.getElement().getName());
		if(parser == null)
			throw new ProcessParseException("无法解析<"+taskMetadata.getElement().getName()+">标签");
		return parser.parse(taskMetadata);
	}
}
