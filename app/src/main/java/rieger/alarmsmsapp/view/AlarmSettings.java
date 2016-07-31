package rieger.alarmsmsapp.view;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.widget.AlarmWidget;
import rieger.alarmsmsapp.view.fragments.settings.AlarmSettingsFragment;

/**
 * Activity from which it is possible to set the global alarm settings.
 */
public class AlarmSettings extends AppCompatActivity implements AlarmSettingsFragment.OnFragmentInteractionListener {

    private static final String LOG_TAG = AlarmSettings.class.getSimpleName();

    @Bind(R.id.activity_alarm_settings_button_save_alarm_settings)
    Button save;

    @Bind(R.id.activity_alarm_settings_button_quit_alarm_settings)
    Button quit;

    AlarmSettingsFragment alarmSettingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_settings);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        alarmSettingsFragment = new AlarmSettingsFragment();

        ft.replace(R.id.activity_alarm_settings_fragment_container, alarmSettingsFragment, "AlarmSettingsFragment");
        ft.commit();

        initializeGUI();

        initializeActiveElements();
        
	}

    /**
     * This method initialize the all GUI elements.
     */
    private void initializeGUI() {
        ButterKnife.bind(this);
    }

    /**
     * This method initialize the active GUI elements with listeners.
     */
    private void initializeActiveElements() {

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmSettingsFragment.saveData();

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
		getMenuInflater().inflate(R.menu.alarm_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
