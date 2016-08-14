package rieger.alarmsmsapp.model.rules;

import java.io.Serializable;

/**
 * A class, which represents a SMSRule.
 *
 * @author sebastian
 *
 */
public class SMSRule extends Rule implements Serializable{

	/**
	 * the id for the serialisation
	 */
	private static final long serialVersionUID = -5676251882283267346L;

	/**
	 * {@inheritDoc}
	 */
	public SMSRule() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * @param ruleName the name of the rule
	 */
	public SMSRule(String ruleName){
		super(ruleName);

	}

	/**
     * {@inheritDoc}
	 * @param ruleName the name of the rule
	 * @param sender the sender of the rule
	 * @param occurredWords occurredWords of the rule
	 * @param notOccurredWords notOccurredWords of the rule
	 * @param alarmSound alarm sound of the rule
	 * @param automaticallyAnswer the automatically answer of the rule
	 * @param messageToPostOnFacebook the facebook post of the rule
	 * @param navigationTarget the navigation target of the rule
	 * @param readThisMessage the flag for reading this message
	 * @param readOtherMessages the flag for reading other messages
	 */
	public SMSRule(String ruleName, String sender,
			String occurredWords, String notOccurredWords, Sound alarmSound,
			AnswerBundle automaticallyAnswer,
			String messageToPostOnFacebook, String navigationTarget,
			boolean readThisMessage, boolean readOtherMessages) {
		super(ruleName, sender, occurredWords, notOccurredWords, alarmSound, automaticallyAnswer,
				messageToPostOnFacebook, navigationTarget, readThisMessage, readOtherMessages);
	}
}
