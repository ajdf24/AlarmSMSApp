package rieger.alarmsmsapp.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receives when the installation is completed and check if there are old rules, which should be saved in the Database.
 * Created by sebastian on 14.08.16.
 */
public class InstallationReceiver extends BroadcastReceiver{

    private static final String LOG_TAG = InstallationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(LOG_TAG, "Install");
    }

}
