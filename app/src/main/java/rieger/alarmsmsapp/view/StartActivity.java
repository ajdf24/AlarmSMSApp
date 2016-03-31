package rieger.alarmsmsapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.model.SettingsNotFoundException;

/**
 * This class is the start activity, which checks the permissions and the settings if the first use.
 * Created by sebastian on 19.08.15.
 */
public class StartActivity extends AppCompatActivity {

    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        redirectForFirstUse();
    }
    /**
     * This method redirect the user if there are no settings are found.
     */
    private  void redirectForFirstUse() {
        try {
            AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e) {
                Log.i(this.getClass().getSimpleName(), "Alarm Settings not set. Start Alarm Settings");
                startActivity(new Intent(this, AlarmSettings.class));
                return;
        }

        try {
            DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.i(this.getClass().getSimpleName(), "Department Settings not set. Start Alarm Settings");
            startActivity(new Intent(this, DepartmentSettings.class));
            return;
        }

        startActivity(new Intent(this, RuleSelection.class));
    }
}
