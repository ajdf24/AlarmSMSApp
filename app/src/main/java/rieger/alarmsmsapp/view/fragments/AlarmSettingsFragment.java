package rieger.alarmsmsapp.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.widget.AlarmWidget;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AlarmSettingsFragment extends Fragment {

    private static final String LOG_TAG = AlarmSettingsFragment.class.getSimpleName();

    @Bind(R.id.activity_alarm_settings_switch_alarm_activated)
    SwitchCompat getAlarms;

    @Bind(R.id.activity_alarm_settings_seekBar_volume)
    SeekBar alarmVolume;

    @Bind(R.id.activity_alarm_settings_switch_vibration_activated)
    SwitchCompat alarmWithVibrate;

    @Bind(R.id.activity_alarm_settings_switch_notification_light_activated)
    SwitchCompat alarmWithNotificationLight;

    @Bind(R.id.activity_alarm_settings_spinner_for_notification_light_color)
    AppCompatSpinner notificationLightColor;

    @Bind(R.id.activity_alarm_settings_switch_mute_alarm_activated)
    SwitchCompat alarmWhenMute;

    @Bind(R.id.activity_alarm_settings_repeat_alarm)
    EditText repeatAlarm;

    private OnFragmentInteractionListener mListener;

    private AlarmSettingsModel alarmSettingsModel = new AlarmSettingsModel();

    private View view;

    public AlarmSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);

        ButterKnife.bind(this, view);

        getAlarmSettingsForGUI();

        initializeActiveElements();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void saveData(){

        alarmSettingsModel.setRepeatAlarm(Integer.parseInt(repeatAlarm.getText().toString()));

        alarmSettingsModel.notifyObserver();

        AlarmWidget.updateWidget();

        Log.i(LOG_TAG, "Alarmeinstellungen gespeichert");

    }

    /**
     * This method get the current values from the settings and sets the GUI to this values.
     */
    private void getAlarmSettingsForGUI() {

        try {
            alarmSettingsModel = AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e) {
            Log.e(this.getClass().getSimpleName(), "Alarm Settings not found.");
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

        getAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setAlarmActivated(getAlarms.isChecked());
            }
        });

        alarmVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        alarmWithVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setVibrationActivated(alarmWithVibrate.isChecked());
            }
        });

        alarmWithNotificationLight.setOnClickListener(new View.OnClickListener() {
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

        alarmWhenMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmSettingsModel.setMuteAlarmActivated(alarmWhenMute.isChecked());
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
