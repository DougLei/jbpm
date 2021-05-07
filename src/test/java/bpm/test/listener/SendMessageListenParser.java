package bpm.test.listener;

import org.dom4j.Element;

import com.douglei.bpm.process.api.listener.ListenParser;
import com.douglei.bpm.process.handler.AbstractHandleParameter;
import com.douglei.bpm.process.mapping.metadata.listener.ActiveTime;
import com.douglei.bpm.process.mapping.metadata.listener.Listener;

/**
 * 
 * @author DougLei
 */
public class SendMessageListenParser implements ListenParser {

	@Override
	public Listener parse(ActiveTime activeTime, Element element) {
		return new Listener(activeTime) {
			@Override
			public void notify(AbstractHandleParameter handleParameter) {
				handleParameter.getUserEntity().getAssignEntity().getAssignedUserIds().forEach(userId -> {
					System.out.println("给userId=["+userId+"]的用户推送消息");
				});
			}
		};
	}
}
