package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.googleplaces.GooglePlacesAutocompleteAdapter;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for creating a navigation target.
 * In this activity it is possible to set all the things which are needed for the target.
 */
public class NavigationTargetSelection extends AppCompatActivity implements OnItemClickListener{

	private Rule rule;

	private AutoCompleteTextView autoCompView;

	private Button save;

	private Button quit;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		rule = BundleHandler.getRuleFromBundle(this);

		initializeGUI();

		getRuleSettingsForGUI();

		initializeActiveElements();


	}

    /**
     * This method makes a {@link Toast} after the user clicks to a item of the
     * adapter view.
     * @param adapterView the adapter view
     * @param view the of the element
     * @param position the position of the item
     * @param id the id
     */
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

    /**
     * Overrides the method and returns the name of the activity,
     * which is necessary for the {@link rieger.alarmsmsapp.view.ruleactivitys.RuleSettings} activity.
     * @return the name of the activity
     */
	@Override
	public String toString() {
		return CreateContextForResource.getStringFromID(R.string.title_activity_navigation_target_selection);
	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI() {
		setContentView(R.layout.activity_navigation_target_selection);
		autoCompView = (AutoCompleteTextView) findViewById(R.id.activity_navigation_target_selection_AutoCompleteTextView_navigation_target);

		save = (Button) findViewById(R.id.activity_navigation_target_selection_button_save_navigation_target);
		quit = (Button) findViewById(R.id.activity_navigation_target_selection_button_quit);
	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements() {
		autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_for_autocomplete));
		autoCompView.setOnItemClickListener(this);

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RuleCreator.changeNavigationTarget(rule, autoCompView.getText().toString());

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(NavigationTargetSelection.this, RuleSettings.class);
				startActivity(intent);
			}
		});

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(NavigationTargetSelection.this, RuleSettings.class);
				startActivity(intent);
			}
		});

	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI() {
		autoCompView.setText(rule.getNavigationTarget());
	}
}
