package rieger.alarmsmsapp.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.standard.NotificationCreator;
import rieger.alarmsmsapp.util.standard.NotificationObserver;
import rieger.alarmsmsapp.view.StartActivity;

/**
 * Receives when the installation is completed and check if there are old rules, which should be saved in the Database.
 * Created by sebastian on 14.08.16.
 */
public class InstallationReceiver extends BroadcastReceiver{

    private static final String LOG_TAG = InstallationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCreator.createFreeNotification(R.drawable.ic_notification, "Willkomen im Beta-Programm", "Probiere jetzt die Neuerungen aus.", Color.RED, 1000, 1000, null, StartActivity.class);
        Log.e(LOG_TAG, "Install");
    }

}
