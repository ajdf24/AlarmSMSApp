package rieger.alarmsmsapp.model.rules;

import java.io.Serializable;

/**
 * A class, which represents a E-MailRule.
 * @author sebastian
 *
 */
public class EMailRule extends Rule implements Serializable {

	/**
	 * the id for the serialisation
	 */
	private static final long serialVersionUID = -7075633802069919540L;

	/**
	 * {@inheritDoc}
	 */
	public EMailRule() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * @param ruleName the name of the rule
	 */
	public EMailRule(String ruleName){
		super(ruleName);

	}

	/**
	 * {@inheritDoc}
	 * @param ruleName the isRuleActidated of the rule
	 * @param sender the sender of the rule
	 * @param occurredWords occurredWords of the rule
	 * @param notOccurredWords notoccurredWords of the rule
	 * @param alarmSound alarm sound of the rule
	 * @param automaticallyAnswer the automatically answer of the rule
	 * @param messageToPostOnFacebook the facebook post of the rule
	 * @param navigationTarget the navigation target of the rule
	 * @param readThisMessage the flag for reading this message
	 * @param readOtherMessages the flag for reading other messages
	 */
	public EMailRule(String ruleName, String sender,
			String occurredWords, String notOccurredWords, Sound alarmSound,
			AnswerBundle automaticallyAnswer,
			String messageToPostOnFacebook, String navigationTarget,
			boolean readThisMessage, boolean readOtherMessages) {
		super(ruleName,sender,occurredWords, notOccurredWords, alarmSound, automaticallyAnswer,
				messageToPostOnFacebook, navigationTarget, readThisMessage, readOtherMessages);
	}

}
