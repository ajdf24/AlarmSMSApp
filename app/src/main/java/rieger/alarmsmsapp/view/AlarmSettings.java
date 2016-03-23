package rieger.alarmsmsapp.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.widget.AlarmWidget;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Activity from which it is possible to set the global alarm settings.
 */
public class AlarmSettings extends AppCompatActivity {

    private AlarmSettingsModel alarmSettingsModel = new AlarmSettingsModel();

    private SwitchCompat getAlarms;

    private SeekBar alarmVolume;

    private SwitchCompat alarmWithVibrate;

    private SwitchCompat alarmWithNotificationLight;

    private AppCompatSpinner notificationLightColor;

    private SwitchCompat alarmWhenMute;

    private Button save;

    private Button quit;

    private EditText repeatAlarm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_settings);

        initializeGUI();

        getAlarmSettingsForGUI();

        initializeActiveElements();
        
	}

    /**
     * This method initialize the all GUI elements.
     */
    private void initializeGUI() {
        getAlarms = (SwitchCompat) findViewById(R.id.activity_alarm_settings_switch_alarm_activated);
        alarmVolume = (SeekBar) findViewById(R.id.activity_alarm_settings_seekBar_volume);
        alarmWithVibrate = (SwitchCompat) findViewById(R.id.activity_alarm_settings_switch_vibration_activated);
        alarmWithNotificationLight = (SwitchCompat) findViewById(R.id.activity_alarm_settings_switch_notification_light_activated);
        notificationLightColor = (AppCompatSpinner) findViewById(R.id.activity_alarm_settings_spinner_for_notification_light_color);
        alarmWhenMute = (SwitchCompat) findViewById(R.id.activity_alarm_settings_switch_mute_alarm_activated);
        repeatAlarm = (EditText) findViewById(R.id.activity_alarm_settings_repeat_alarm);
        save = (Button) findViewById(R.id.activity_alarm_settings_button_save_alarm_settings);
        quit = (Button) findViewById(R.id.activity_alarm_settings_button_quit_alarm_settings);
    }

    /**
     * This method get the current values from the settings and sets the GUI to this values.
     */
    private void getAlarmSettingsForGUI() {
        try {
            alarmSettingsModel = AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "Alarm Settings not found.");
            alarmSettingsModel = new AlarmSettingsModel();

            AlertDialog dialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_title))

                    .setMessage(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_text))
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info)

                    .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

            dialog = builder.create();

            dialog.show();
        }

        if (alarmSettingsModel == null){
            alarmSettingsModel = new AlarmSettingsModel();
        }

        getAlarms.setChecked(alarmSettingsModel.isAlarmActivated());
        alarmVolume.setProgress(alarmSettingsModel.getAlarmVolume());
        alarmWithVibrate.setChecked(alarmSettingsModel.isVibrationActivated());
        alarmWithNotificationLight.setChecked(alarmSettingsModel.isNotificationLightActivated());
        switch (alarmSettingsModel.getNotificationLightColor()){
            case Color.RED:
                notificationLightColor.setSelection(0);
                break;
            case Color.GREEN:
                notificationLightColor.setSelection(1);
                break;
            case Color.BLUE:
                notificationLightColor.setSelection(2);
                break;
        }
        alarmWhenMute.setChecked(alarmSettingsModel.isMuteAlarmActivated());
        repeatAlarm.setText("" + alarmSettingsModel.getRepeatAlarm());
    }

    /**
     * This method initialize the active GUI elements with listeners.
     */
    private void initializeActiveElements() {

        getAlarms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setAlarmActivated(getAlarms.isChecked());
            }
        });

        alarmVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                alarmSettingsModel.setAlarmVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        alarmWithVibrate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setVibrationActivated(alarmWithVibrate.isChecked());
            }
        });

        alarmWithNotificationLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setNotificationLightActivated(alarmWithNotificationLight.isChecked());
            }
        });

        notificationLightColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        alarmSettingsModel.setNotificationLightColor(Color.RED);
                        break;
                    case 1:
                        alarmSettingsModel.setNotificationLightColor(Color.GREEN);
                        break;
                    case 2:
                        alarmSettingsModel.setNotificationLightColor(Color.BLUE);
                        break;

                    default:
                        alarmSettingsModel.setNotificationLightColor(Color.RED);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alarmWhenMute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setMuteAlarmActivated(alarmWhenMute.isChecked());
            }
        });

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmSettingsModel.setRepeatAlarm(Integer.parseInt(repeatAlarm.getText().toString()));

                alarmSettingsModel.notifyObserver();

                AlarmWidget.updateWidget();

                startActivity(new Intent(AlarmSettings.this, StartActivity.class));
            }
        });

        quit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmSettings.this, StartActivity.class));
            }
        });

    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.department_settings_from_alarm_settings) {
			startActivity(new Intent(this, DepartmentSettings.class));
			return true;
		}if (id == R.id.rule_list_from_alarm_settings) {
			startActivity(new Intent(this, RuleSelection.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
