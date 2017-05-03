package rieger.alarmsmsapp.model.rules;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bundle class for the automatically answer.
 * Contains all information for a automatically answer when the rule applies.
 *
 * @author sebastian
 *
 */
public class AnswerBundle implements Serializable {

	/**
	 * the id for the serialisation
	 */
	private static final long serialVersionUID = 913136032042403079L;

	private List<String> receivers;

	private String message;

	private int distance;

	private boolean sendAnswerEveryTime = false;

	private boolean addOriginalMessage = false;

	/**
	 * Default constructor for the serialization.<br>
	 * <b>Note</b>: Don't use them!
	 */
	public AnswerBundle() {
		receivers = new ArrayList<>();
	}

	/**
	 * Constructor for creating a instance of this class.
	 * @param receivers list of numbers of the receiver
	 * @param message the message which should be send
	 * @param distance the distance from which the home point
	 */
	public AnswerBundle(@NonNull List<String> receivers, String message, int distance) {
		this.receivers = receivers;
		this.message = message;
		this.distance = distance;
	}

	/**
	 * @return the receiver
	 */
	public List<String> getReceivers() {
		return receivers;
	}

	/**
	 * @param receivers the receivers to set
	 */
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean isSendAnswerEveryTime() {
		return sendAnswerEveryTime;
	}

	public void setSendAnswerEveryTime(boolean sendAnswerEveryTime) {
		this.sendAnswerEveryTime = sendAnswerEveryTime;
	}

	public void addReceiver(String receiver){
		receivers.add(receiver);
	}

	public void removeReceiver(String receiver){
		receivers.remove(receiver);
	}

	public boolean isAddOriginalMessage() {
		return addOriginalMessage;
	}

	public void setAddOriginalMessage(boolean addOriginalMessage) {
		this.addOriginalMessage = addOriginalMessage;
	}

	@Override
	public String toString() {
		return "AnswerBundle{" +
				"receivers=" + receivers +
				", message='" + message + '\'' +
				", distance=" + distance +
				", sendAnswerEveryTime=" + sendAnswerEveryTime +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AnswerBundle)) return false;

		AnswerBundle that = (AnswerBundle) o;

		if (distance != that.distance) return false;
		if (sendAnswerEveryTime != that.sendAnswerEveryTime) return false;
		if (receivers != null ? !receivers.equals(that.receivers) : that.receivers != null)
			return false;
		return message != null ? message.equals(that.message) : that.message == null;

	}

	@Override
	public int hashCode() {
		int result = receivers != null ? receivers.hashCode() : 0;
		result = 31 * result + (message != null ? message.hashCode() : 0);
		result = 31 * result + distance;
		result = 31 * result + (sendAnswerEveryTime ? 1 : 0);
		return result;
	}
}
