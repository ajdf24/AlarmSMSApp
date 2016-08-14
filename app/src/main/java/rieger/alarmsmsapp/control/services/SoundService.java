package rieger.alarmsmsapp.control.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.control.eventbus.BusProvider;
import rieger.alarmsmsapp.model.rules.Sound;
import rieger.alarmsmsapp.model.events.SoundEvent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class SoundService extends IntentService {

    /**
     * LOG_TAG
     */
    private static final String LOG_TAG = SoundService.class.getSimpleName();

    private static final String ACTION_LOAD_SOUNDS = "rieger.alarmsmsapp.control.services.action.load.sounds";

    public SoundService() {
        super("SoundService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startLoadSounds(Context context) {
        Intent intent = new Intent(context, SoundService.class);
        intent.setAction(ACTION_LOAD_SOUNDS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOAD_SOUNDS.equals(action)) {
                handleActionFoo();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo() {

        List<Sound> soundList = new ArrayList<Sound>();

        soundList.add(new Sound("Motorola BMD Schleife 1","bmd_ton1", true));
        soundList.add(new Sound("Motorola BMD Schleife 2","bmd_ton2", true));
        soundList.add(new Sound("E57 Sirene","e57_sirene", true));
        soundList.add(new Sound("Skyfire Schleife 1","firestorm_ton1", true));
        soundList.add(new Sound("Skyfire Schleife 2","firestorm_ton2", true));
        soundList.add(new Sound("Quattro 96 Schleife 1","q96_schleife1", true));
        soundList.add(new Sound("Quattro 96 Schleife 2","q96_schleife2", true));
        soundList.add(new Sound("Quattro 96 Schleife 3","q96_schleife3", true));
        soundList.add(new Sound("Quattro 96 Schleife 4","q96_schleife4", true));
        soundList.add(new Sound("Quattro 98 Schleife 1","quattro_98_ton1", true));
        soundList.add(new Sound("Quattro 98 Schleife 2","quattro_98_ton2", true));

        File[] files = new File("/system/media/audio/ringtones/").listFiles();

        if(files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    int pos = name.lastIndexOf(".");
                    if (pos > 0) {
                        name = name.substring(0, pos);
                    }
                    soundList.add(new Sound(name, "/system/media/audio/ringtones/" + file.getName(), false));

                }
            }
        }

        BusProvider.post(new SoundEvent(soundList));
    }

}
