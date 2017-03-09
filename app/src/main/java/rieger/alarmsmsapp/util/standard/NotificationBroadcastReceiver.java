package rieger.alarmsmsapp.util.standard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import rieger.alarmsmsapp.control.receiver.worker.AlarmSoundPlayer;

/**
 * Created by sebastian on 23.03.16.
 *
 * Receives a Broadcast from a Notification.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_TAG = "NotificationBroadcastReceiver";

    /**
     *
     * Diese Methode solle sich eigentlich über den Inten einen {@link NotificationObserver} holen und dann da die entsprechenden Methoden aufrufen.
     *
     * Die Implementierenden Klassen, führen dann den entsprechenden Befehl aus.
     *
     * Dies geht leider hier nicht, da der {@link AlarmSoundPlayer} nicht serialisiert werden kann, da er einen {@link android.media.MediaPlayer} enthält.
     * Dies sollte eigentlich den Ton beenden.
     *
     * ==> Workaround {@link AlarmSoundPlayer} als Singleton umgebaut!!! der folgende Aufruf bricht dann dass Abspielen ab.
     *
     * @param context called {@link Context}
     * @param intent data
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            AlarmSoundPlayer.getGlobalMediaPlayer().stop();
        }catch (NullPointerException e){
            FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "No mediaplayer Instance found.");
            FirebaseCrash.report(e);
        }
    }

}
