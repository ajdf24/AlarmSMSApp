package rieger.alarmsmsapp.view;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.view.fragments.DepartmentFragment;

public class DepartmentSettings extends AppCompatActivity implements DepartmentFragment.OnFragmentInteractionListener  {

    private static final String LOG_TAG = DepartmentSettings.class.getSimpleName();

    @Bind(R.id.activity_department_settings_button_save_department)
    Button save;

    @Bind(R.id.activity_department_settings_button_quit)
    Button quit;

    DepartmentFragment departmentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_department_settings);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        departmentFragment = new DepartmentFragment();

        ft.replace(R.id.activity_department_settings_fragment_container, departmentFragment, "DepartmentFragment");

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

        save.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                departmentFragment.saveData();

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
		getMenuInflater().inflate(R.menu.department_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
    public void onFragmentInteraction(Uri uri) {

    }
}
