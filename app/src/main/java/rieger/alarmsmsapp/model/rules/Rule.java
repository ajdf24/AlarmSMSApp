package rieger.alarmsmsapp.model.rules;

import java.io.Serializable;

import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * An abstract class, which all rules should extend.
 * This class is the base for a serialization of the rules by Jackson.
 * It contains all relevant settings.
 *
 * @author sebastian
 *
 */
public abstract class Rule implements Serializable{


	/**
	 * the id for serialisation
	 */
	private static final long serialVersionUID = 9118889470997965118L;

	protected boolean active = true;

	protected String ruleName;

	protected String sender;

	protected String occurredWords;

	protected String notOccurredWords;

	protected Sound alarmSoundUri;

	protected AnswerBundle automaticallyAnswer;

	protected String messageToPostOnTwitter;

	protected String navigationTarget;

	protected boolean readThisMessage;

	protected boolean readOtherMessages;

	protected boolean addMessageToTwitterPost;

	protected boolean activateLight = false;

	protected boolean activateLightOnlyWhenDark = false;

	protected int lightTime = 30000;

	/**
	 * Default constructor for the serialization.<br>
	 * <b>Note</b>: Don't use them!
	 */
	public Rule() {
		this.automaticallyAnswer = new AnswerBundle();
	}

	/**
	 * Initial constructor, which only sets the name of the rule.
	 * @param ruleName the name of the rule
	 */
	public Rule(String ruleName){
		this.ruleName = ruleName;
		this.automaticallyAnswer = new AnswerBundle();
	}

	/**
     * Constructor, which sets all values of the class.
     *
	 * @param ruleName the name of the rule
	 * @param sender the sender of the rule
	 * @param occurredWords occurredWords of the rule
	 * @param notOccurredWords notoccurredWords of the rule
	 * @param alarmSound alarm sound of the rule
	 * @param automaticallyAnswer the automatically answer of the rule
	 * @param messageToPostOnTwitter the Twitter post of the rule
	 * @param navigationTarget the navigation target of the rule
	 * @param readThisMessage the flag for reading this message
	 * @param readOtherMessages the flag for reading other messages
	 */
	public Rule(String ruleName, String sender,
			String occurredWords, String notOccurredWords, Sound alarmSound,
			AnswerBundle automaticallyAnswer,
			String messageToPostOnTwitter, String navigationTarget,
			boolean readThisMessage, boolean readOtherMessages) {
		this.ruleName = ruleName;
		this.sender = sender;
		this.occurredWords = occurredWords;
		this.notOccurredWords = notOccurredWords;
		this.alarmSoundUri = alarmSound;
		this.automaticallyAnswer = automaticallyAnswer;
		this.messageToPostOnTwitter = messageToPostOnTwitter;
		this.navigationTarget = navigationTarget;
		this.readThisMessage = readThisMessage;
		this.readOtherMessages = readOtherMessages;
	}


	/**
	 * @return the readThisMessage
	 */
	public boolean isReadThisMessage() {
		return readThisMessage;
	}

	/**
	 * @param readThisMessage the readThisMessage to set
	 */
	public void setReadThisMessage(boolean readThisMessage) {
		this.readThisMessage = readThisMessage;
	}

	/**
	 * @return the readOtherMessages
	 */
	public boolean isReadOtherMessages() {
		return readOtherMessages;
	}

	/**
	 * @param readOtherMessages the readOtherMessages to set
	 */
	public void setReadOtherMessages(boolean readOtherMessages) {
		this.readOtherMessages = readOtherMessages;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the occurredWords
	 */
	public String getOccurredWords() {
		return occurredWords;
	}

	/**
	 * @param occurredWords the occurredWords to set
	 */
	public void setOccurredWords(String occurredWords) {
		this.occurredWords = occurredWords;
	}

	/**
	 * @return the notOccurredWords
	 */
	public String getNotOccurredWords() {
		return notOccurredWords;
	}

	/**
	 * @param notOccurredWords the notOccurredWords to set
	 */
	public void setNotOccurredWords(String notOccurredWords) {
		this.notOccurredWords = notOccurredWords;
	}

	/**
	 * @return the alarmSoundUri
	 */
	public Sound getAlarmSound() {
		return alarmSoundUri;
	}

	/**
	 * @param alarmSound the alarmSoundUri to set
	 */
	public void setAlarmSound(Sound alarmSound) {
		this.alarmSoundUri = alarmSound;
	}

	/**
	 * @return the automaticallyAnswer
	 */
	public AnswerBundle getAutomaticallyAnswer() {
		return automaticallyAnswer;
	}

	/**
	 * @param automaticallyAnswer the automaticallyAnswer to set
	 */
	public void setAutomaticallyAnswer(AnswerBundle automaticallyAnswer) {
		this.automaticallyAnswer = automaticallyAnswer;
	}

	/**
	 * @return the messageToPostOnTwitter
	 */
	public String getMessageToPostOnTwitter() {
		return messageToPostOnTwitter;
	}

	/**
	 * @param messageToPostOnTwitter the messageToPostOnTwitter to set
	 */
	public void setMessageToPostOnTwitter(String messageToPostOnTwitter) {
		this.messageToPostOnTwitter = messageToPostOnTwitter;
	}

	/**
	 * @return the navigationTarget
	 */
	public String getNavigationTarget() {
		return navigationTarget;
	}

	/**
	 * @param navigationTarget the navigationTarget to set
	 */
	public void setNavigationTarget(String navigationTarget) {
		this.navigationTarget = navigationTarget;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#getReceiver()
	 */
	public String getReceiver() {
		if (automaticallyAnswer==null) {
			return null;
		}
		return automaticallyAnswer.getReceiver();
	}

	/**
	 * @param receiver
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#setReceiver(String)
	 */
	public void setReceiver(String receiver) {
		if (automaticallyAnswer!=null) {
			automaticallyAnswer.setReceiver(receiver);
		}
	}

	/**
	 * @return
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#getMessage()
	 */
	public String getMessage() {
		if (automaticallyAnswer==null) {
			return null;
		}
		return automaticallyAnswer.getMessage();
	}

	/**
	 * @param message
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#setMessage(String)
	 */
	public void setMessage(String message) {
		if (automaticallyAnswer!=null) {
			automaticallyAnswer.setMessage(message);
		}
	}

	/**
	 * @return
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#getDistance()
	 */
	public int getDistance() {
		if (automaticallyAnswer==null) {
			return 0;
		}
		return automaticallyAnswer.getDistance();
	}

	/**
	 * @param distance
	 * @see rieger.alarmsmsapp.model.rules.AnswerBundle#setDistance(int)
	 */
	public void setDistance(int distance) {
		if (automaticallyAnswer!=null) {
			automaticallyAnswer.setDistance(distance);
		}
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ruleName == null) ? 0 : ruleName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Rule)) {
			return false;
		}
		Rule other = (Rule) obj;
		if (ruleName == null) {
			if (other.ruleName != null) {
				return false;
			}
		} else if (!ruleName.equals(other.ruleName)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the alarmSoundUri
	 */
	public Sound getAlarmSoundUri() {
		return alarmSoundUri;
	}

	/**
	 * @param alarmSoundUri the alarmSoundUri to set
	 */
	public void setAlarmSoundUri(Sound alarmSoundUri) {
		this.alarmSoundUri = alarmSoundUri;
	}

	/**
	 * @return the addMessageToTwitterPost
	 */
	public boolean isAddMessageToTwitterPost() {
		return addMessageToTwitterPost;
	}

	/**
	 * @param addMessageToTwitterPost the addMessageToTwitterPost to set
	 */
	public void setAddMessageToTwitterPost(boolean addMessageToTwitterPost) {
		this.addMessageToTwitterPost = addMessageToTwitterPost;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getRuleName();
	}

    /**
     * Notify the observer.
     */
	public void notifyObserver(){
		DataSource db = new DataSource(CreateContextForResource.getContext());
		db.saveRule(this);
//		RuleObserver.saveRuleToFileSystem(this);
	}

	/**
	 *
	 * @return
	 */
	public boolean isActivateLight() {
		return activateLight;
	}

	/**
	 *
	 * @param activateLight must light be activated
	 */
	public void setActivateLight(boolean activateLight) {
		this.activateLight = activateLight;
	}

	/**
	 *
	 * @return
	 */
	public int getLightTime() {
		return lightTime;
	}

	/**
	 *
	 * @param lightTime how long should the light be on
	 */
	public void setLightTime(int lightTime) {
		this.lightTime = lightTime;
	}

	public boolean isActivateLightOnlyWhenDark() {
		return activateLightOnlyWhenDark;
	}

	public void setActivateLightOnlyWhenDark(boolean activateLightOnlyWhenDark) {
		this.activateLightOnlyWhenDark = activateLightOnlyWhenDark;
	}

	public boolean isSendEveryTime(){
		if (automaticallyAnswer==null) {
			return false;
		}
		return automaticallyAnswer.isSendAnswerEveryTime();
	}

	public void setSendEveryTime(boolean set){
		if (automaticallyAnswer!=null) {
			automaticallyAnswer.setSendAnswerEveryTime(set);
		}
	}
}
