package rieger.alarmsmsapp.model.events;

import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.model.Sound;

/**
 * Eventclass which is a container for a list of sounds
 *
 * Created by sebastian on 31.03.16.
 */
public class SoundEvent {

    /**
     * LOG_TAG
     */
    private static final String LOG_TAG = SoundEvent.class.getSimpleName();

    /**
     * List of all sounds
     */
    private List<Sound> soundList = new ArrayList<>();

    /**
     * constructor
     * @param soundList list of sounds, which should be common
     */
    public SoundEvent(List<Sound> soundList) {
        this.soundList = soundList;
    }

    /**
     *
     * @return the list of sounds
     */
    public List<Sound> getSoundList() {
        return soundList;
    }
}
