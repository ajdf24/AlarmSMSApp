package rieger.alarmsmsapp.util.standard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rieger.alarmsmsapp.control.receiver.worker.AlarmSoundPlayer;

/**
 * Created by sebastian on 23.03.16.
 */
public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSoundPlayer player = AlarmSoundPlayer.getInstance();
        AlarmSoundPlayer.getGlobalMediaPlayer().stop();
    }

}
