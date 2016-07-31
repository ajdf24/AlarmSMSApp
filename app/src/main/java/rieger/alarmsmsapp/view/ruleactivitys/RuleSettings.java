package rieger.alarmsmsapp.view.ruleactivitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.view.AlarmSettings;
import rieger.alarmsmsapp.view.DepartmentSettings;
import rieger.alarmsmsapp.view.RuleSelection;

/**
 * This activity is for creating a list with all possible settings.
 */
public class RuleSettings extends AppCompatActivity {

	private static final String LOG_TAG = RuleSelection.class.getSimpleName();

	private List<Activity> activityList = new ArrayList<Activity>();

	private Rule rule;

	private Bundle bundle = new Bundle();

	private Intent intent;

	SharedPreferences prefs;

    @Bind(R.id.activity_rule_settings_button_save_settings)
	Button save;

    @Bind(R.id.activity_rule_settings_button_quit)
	Button quit;

	@Bind(R.id.listView1)
	ListView listView;


    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_settings);

		rule = BundleHandler.getRuleFromBundle(this);
		setTitle(getResources().getString(R.string.title_activity_rule_settings)+ ": "+rule.getRuleName());

		initializeGUI();

		initializeActiveElements();

	}

    /**
     * This method makes the menu for this activity.
     * @param menu the menu which should inflate
     * @return
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rule_settings, menu);
		return true;
	}

    /**
     * In this method are the actions created for the menu
     * @param item the selected item
     * @return
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.alarm_settings_from_rule_settings) {

			startActivity(new Intent(this, AlarmSettings.class));
			return true;

		}
		if (id == R.id.department_settings_from_rule_settings) {

			startActivity(new Intent(this, DepartmentSettings.class));
			return true;

		}
		if (id == R.id.rule_list_from_rule_settings) {

			startActivity(new Intent(this, RuleSelection.class));
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

    /**
     * This method create a list with all setting activity's.
     * </br>
     * <b>Note</b>: If a new activity was created so this class must be added manually in this method.
     */
	private void createActivityList() {

		activityList.add(new SenderSelection());
		activityList.add(new WordSelection());
		activityList.add(new SoundSelection());
		activityList.add(new AnswerCreation());
		activityList.add(new LightSettings());
		activityList.add(new TwitterPostSelection());
		activityList.add(new NavigationTargetSelection());
		activityList.add(new ReadingSettings());

	}

    /**
     * This method creates the adapter for the list, which should be shown.
     */
	private void createListAdapter() {
		ListAdapter adapter = new ArrayAdapter<Activity>( getApplicationContext(), R.layout.list_item_rule_settings, activityList);
//		final ListView listView = (ListView) findViewById(R.id.listView1);

		listView.setAdapter(adapter);

//		listView.getChildAt(0)

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Log.d(LOG_TAG, listView.getAdapter().getItem(position).getClass().getName());

				intent = new Intent();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

				intent.putExtras(bundle);
				intent.setClassName(getPackageName(), listView.getAdapter().getItem(position).getClass().getName());
				startActivity(intent);
			}
		});


	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI() {
        ButterKnife.bind(this);

		createActivityList();

		createListAdapter();


	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements() {
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();

				intent.setClass(RuleSettings.this, RuleSelection.class);
				startActivity(intent);
			}
		});

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();

				intent.setClass(RuleSettings.this, RuleSelection.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		prefs = PreferenceManager.getDefaultSharedPreferences(RuleSettings.this);
		if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_SETTINGS, true)) {

			final RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_rule_settings);
			ViewTreeObserver vto = layout.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					int width = layout.getMeasuredWidth();
					int height = layout.getMeasuredHeight();
					System.out.println(listView.isShown());

					if (listView.getChildAt(0) != null) {
						ShowcaseView showcaseView = new ShowcaseView.Builder(RuleSettings.this)
								.setTarget(new ViewTarget(listView.getChildAt(0).findViewById(R.id.list_item_rule_settings_view)))
								.setContentTitle("Absenderauswahl")
								.setStyle(com.github.amlcurran.showcaseview.R.style.TextAppearance_ShowcaseView_Detail_Light)
								.setContentText("Wähle den Absender, welcher dich per SMS über einen Einsatz alarmiert.")
								.hideOnTouchOutside()
								.blockAllTouches()
								.build();

						SharedPreferences.Editor editor = prefs.edit();
						editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_SETTINGS, false);
						editor.commit();

					} else {
						System.out.println("Element nicht vorhanden");
					}
				}
			});
		}


	}
}