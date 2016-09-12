package rieger.alarmsmsapp.model.rules;

import java.io.Serializable;

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

	private String receiver;

	private String message;

	private int distance;

	private boolean sendAnswerEveryTime = false;

	/**
	 * Default constructor for the serialization.<br>
	 * <b>Note</b>: Don't use them!
	 */
	public AnswerBundle() {
	}

	/**
	 * Constructor for creating a instance of this class.
	 * @param receiver the number of the receiver
	 * @param message the message which should be send
	 * @param distance the distance from which the home point
	 */
	public AnswerBundle(String receiver, String message, int distance) {
		this.receiver = receiver;
		this.message = message;
		this.distance = distance;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	/* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
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
		if (!(obj instanceof AnswerBundle)) {
			return false;
		}
		AnswerBundle other = (AnswerBundle) obj;
		if (distance != other.distance) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		if (receiver == null) {
			if (other.receiver != null) {
				return false;
			}
		} else if (!receiver.equals(other.receiver)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AnswerBundle [receiver=" + receiver + ", message="
				+ message + ", distance=" + distance + "]";
	}

}
