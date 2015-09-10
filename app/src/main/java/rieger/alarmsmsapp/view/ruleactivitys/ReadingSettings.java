package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for creating the reading settings.
 * In this activity it is possible to set all the things which are needed for the reading.
 */
public class ReadingSettings extends AppCompatActivity {

	private Rule rule;

	private CheckBox readThisMessage;

	private CheckBox readOtherMessages;

	private Button save;

	private Button quit;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		rule = BundleHandler.getRuleFromBundle(this);

		initializeGUI();

		getRuleSettingsForGUI();

		initializeActiveElements();
	}

    /**
     * Overrides the method and returns the name of the activity,
     * which is necessary for the {@link rieger.alarmsmsapp.view.ruleactivitys.RuleSettings} activity.
     * @return the name of the activity
     */
	@Override
	public String toString() {
		return CreateContextForResource.getStringFromID(R.string.title_activity_reading_settings);
	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI() {
		setContentView(R.layout.activity_reading_settings);

		readThisMessage = (CheckBox) findViewById(R.id.activity_reading_settings_checkBox_read_this_message);
		readOtherMessages = (CheckBox) findViewById(R.id.activity_reading_settings_checkBox_read_other_messages);
		save = (Button) findViewById(R.id.activity_reading_settings_button_save_read_settings);
		quit = (Button) findViewById(R.id.activity_reading_settings_button_quit);

	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements() {
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RuleCreator.changeReadingSettings(rule, readThisMessage.isChecked(), readOtherMessages.isChecked());

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(ReadingSettings.this, RuleSettings.class);
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
				intent.setClass(ReadingSettings.this, RuleSettings.class);
				startActivity(intent);
			}
		});
	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI() {
		readThisMessage.setChecked(rule.isReadThisMessage());
		readOtherMessages.setChecked(rule.isReadOtherMessages());
	}
}
