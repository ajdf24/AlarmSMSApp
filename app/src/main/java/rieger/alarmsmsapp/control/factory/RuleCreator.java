package rieger.alarmsmsapp.control.factory;

import rieger.alarmsmsapp.model.RuleType;
import rieger.alarmsmsapp.model.Sound;
import rieger.alarmsmsapp.model.rules.AnswerBundle;
import rieger.alarmsmsapp.model.rules.EMailRule;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;

/**
 * Factory class, which can create and change a {@link Rule}.
 * @author sebastian
 *
 */
public class RuleCreator {

	/**
	 * Create a rule with a name and the given {@link RuleType}.
	 * @param name the name of the rule as {@link String}
     * @param type the type of the rule as instance of {@link RuleType}
	 * @return a instance of the given {@link Rule} like {@link SMSRule} or <code>null</code> when
     * the {@link RuleType} is not implemented in this class.
	 */
	public static Rule createRule(String name, RuleType type) {
		switch (type) {
		case EMAIL_RULE:

			EMailRule mailRule = new EMailRule(name);
			mailRule.notifyObserver();
			return mailRule;

		case SMS_RULE:

			SMSRule smsRule = new SMSRule(name);
			smsRule.notifyObserver();
			return smsRule;

		default:
			return null;
		}


	}

	/**
	 * Adds a String to the rule, which contains an identifier like a phonenumber or a mail-address.
     * @param rule the {@link rieger.alarmsmsapp.model.rules.Rule}, which should be changed
     * @param identifierString the identifier from the sender
     */
	public static void changeSender(Rule rule, String identifierString) {
		rule.setSender(identifierString);
		rule.notifyObserver();
	}

	/**
	 * This method changes different Words to the rule, which should separated with a blank.
     * @param rule the {@link rieger.alarmsmsapp.model.rules.Rule}, which should be changed
     * @param occurredWords a {@link String} with the occured words.
     * @param notOccurredWords a {@link String} with the words which should not occured.
     */
	public static void changeWords(Rule rule, String occurredWords, String notOccurredWords) {
		rule.setOccurredWords(occurredWords);
		rule.setNotOccurredWords(notOccurredWords);
		rule.notifyObserver();
	}

	/**
	 * This method changes a alarm sound to the rule, which should be played.
	 * @param rule the {@link Rule}, which should be changed
	 * @param soundUri the {@link String} of the sound.
	 */
	public static void changeAlarmSound(Rule rule, Sound soundUri) {
		rule.setAlarmSound(soundUri);
		rule.notifyObserver();
	}

	/**
	 * Changes a automatically answer to the rule.
	 * @param rule the {@link Rule}, which should be changed
	 * @param answer the answer which should be send
	 */
	public static void changeAutomaticallyAnswer(Rule rule, AnswerBundle answer) {
		rule.setAutomaticallyAnswer(answer);
		rule.notifyObserver();
	}

	/**
	 * Changes the message, which should be posted on facebook.
	 * @param rule the {@link Rule}, which should be changed
	 * @param message the message, which should posted
	 * @param addMessage true, if the message should add to the post
	 */
	public static void changeFacebookPost(Rule rule, String message, boolean addMessage) {
		rule.setMessageToPostOnTwitter(message);
		rule.setAddMessageToTwitterPost(addMessage);
		rule.notifyObserver();
	}

	/**
	 * Changes the rule a navigation target.
	 * @param rule the {@link Rule}, which should be changed
	 * @param target the target for the navigation
	 */
	public static void changeNavigationTarget(Rule rule, String target) {
		rule.setNavigationTarget(target);
		rule.notifyObserver();
	}

	/**
	 * Changes the reading settings of the rule.
	 * @param rule the {@link Rule}, which should be changed
	 * @param readThisMessage the value if this message should read
	 * @param readOtherMessages the value if other messages should read
	 */
	public static void changeReadingSettings(Rule rule, boolean readThisMessage, boolean readOtherMessages){
		rule.setReadThisMessage(readThisMessage);
		rule.setReadOtherMessages(readOtherMessages);
		rule.notifyObserver();
	}

	/**
	 * Changes the active status of the rule.
	 * @param rule the {@link Rule}, which should be changed
	 * @param active the value if the rule active
	 */
	public static void changeActive(Rule rule, boolean active){
		rule.setActive(active);
		rule.notifyObserver();
	}

	public static void changeLightSettings(Rule rule, boolean activateLight, int lightTime, boolean activateOnlyWhenDark){
		rule.setActivateLightOnlyWhenDark(activateOnlyWhenDark);
		rule.setActivateLight(activateLight);
		rule.setLightTime(lightTime);
		rule.notifyObserver();
	}
}
