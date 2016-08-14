package rieger.alarmsmsapp.model.rules;

import java.io.Serializable;

/**
 * Container class for a sound, which can played by a alarm.
 * @author sebastian
 *
 */
public class Sound implements Serializable{

	private String soundName;

	private String idForSound;

	private boolean internalSound;

	/**
	 * Default constructor for creating a instance.
	 */
	public Sound() {
	}

	/**
	 * Constructor, for creating a instance with the given parameter.
     * @param soundName the soundName of the sound
     * @param uri the uri for the sound
     * @param internalSound <code>true</code> if the sound is app internal
     *
     */
	public Sound(String soundName, String uri, boolean internalSound){
		this.soundName = soundName;
		this.idForSound = uri;
		this.internalSound = internalSound;
	}

	/**
	 * @return the soundName
	 */
	public String getName() {
		return soundName;
	}

	/**
	 * @param name the soundName to set
	 */
	public void setName(String name) {
		this.soundName = name;
	}

	/**
	 * @return the idForSound
	 */
	public String getIdForSound() {
		return idForSound;
	}

	/**
	 * @param iD the idForSound to set
	 */
	public void setID(String iD) {
		this.idForSound = iD;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return soundName;
	}

	/**
	 * @return the internalSound
	 */
	public boolean isInternalSound() {
		return internalSound;
	}

	/**
	 * @param internalSound the internalSound to set
	 */
	public void setInternalSound(boolean internalSound) {
		this.internalSound = internalSound;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Sound)) return false;

		Sound sound = (Sound) o;

		if (internalSound != sound.internalSound) return false;
		if (soundName != null ? !soundName.equals(sound.soundName) : sound.soundName != null)
			return false;
		return idForSound != null ? idForSound.equals(sound.idForSound) : sound.idForSound == null;

	}

	@Override
	public int hashCode() {
		int result = soundName != null ? soundName.hashCode() : 0;
		result = 31 * result + (idForSound != null ? idForSound.hashCode() : 0);
		result = 31 * result + (internalSound ? 1 : 0);
		return result;
	}
}
