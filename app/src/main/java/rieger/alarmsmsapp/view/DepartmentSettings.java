package rieger.alarmsmsapp.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.googleplaces.GooglePlacesAutocompleteAdapter;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

public class DepartmentSettings extends AppCompatActivity implements OnItemClickListener {

    private static final String LOG_TAG = DepartmentSettings.class.getSimpleName();

    @Bind(R.id.activity_department_settings_autoCompleteTextView_department_location)
    AutoCompleteTextView autoCompView;

    @Bind(R.id.activity_department_settings_button_save_department)
    Button save;

    @Bind(R.id.activity_department_settings_button_quit)
    Button quit;

    private DepartmentSettingsModel departmentSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_settings);

        initializeGUI();

        getDepartmentSettingsForGUI();

        initializeActiveElements();

	}

    private void getDepartmentSettingsForGUI() {
        try {
            departmentSettings = DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.e(LOG_TAG, "Department Settings not found.");
            departmentSettings = new DepartmentSettingsModel();

            AlertDialog dialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(CreateContextForResource.getStringFromID(R.string.activity_department_settings_alert_dialog_title))

                    .setMessage(CreateContextForResource.getStringFromID(R.string.activity_department_settings_alert_dialog_text))
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info)

                    .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_department_settings_alert_dialog_button), null);

            dialog = builder.create();

            dialog.show();
        }

        if (departmentSettings != null) {
            autoCompView.setText(departmentSettings.getAddress());
        }
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

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_for_autocomplete, GooglePlacesAutocompleteAdapter.AUTO_FILTER_BY_THE_CURRENT_COUNTRY));
        autoCompView.setOnItemClickListener(this);

        save.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                departmentSettings = new DepartmentSettingsModel();

                departmentSettings.setAddress(autoCompView.getText().toString());

                DepartmentObserver.saveSettings(departmentSettings);

                Intent intent = new Intent();

                intent.setClass(DepartmentSettings.this, StartActivity.class);
                startActivity(intent);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setClass(DepartmentSettings.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.department_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.alarm_settings_from_department_settings) {
			startActivity(new Intent(this, AlarmSettings.class));
			return true;
		}if (id == R.id.rule_list_from_department_settings) {
			startActivity(new Intent(this, RuleSelection.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
