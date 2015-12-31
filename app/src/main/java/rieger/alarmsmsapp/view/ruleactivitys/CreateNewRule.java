package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.RuleType;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.RuleSelection;

/**
 * This activity is for creating a new rule, with the name of the rule
 * and the type.
 */
public class CreateNewRule extends AppCompatActivity {

	private TextView rulename;

	private RadioGroup ruleType;

	private Button quit;

	private Button save;

	private Rule rule;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initializeGUI();

		initializeActiveElements();

	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI(){
		setContentView(R.layout.activity_create_new_rule);

		rulename = (TextView) findViewById(R.id.activity_create_new_rule_editText_rule_name);
		ruleType = (RadioGroup) findViewById(R.id.activity_create_new_rule_radioGroup_content_chooser);
		quit = (Button) findViewById(R.id.activity_create_new_rule_button_quit_rule_name);
		save = (Button) findViewById(R.id.activity_create_new_rule_button_save_rule_name);
	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements(){
		quit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewRule.this, RuleSelection.class));
            }
        });

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(AppConstants.DEBUG_TAG, rulename.getText().toString());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				RadioButton rb = (RadioButton) findViewById(ruleType.getCheckedRadioButtonId());
				if(getString(R.string.activity_create_new_rule_radio_content_sms)==rb.getText().toString() ){

					rule = RuleCreator.createRule(rulename.getText().toString(), RuleType.SMS_RULE);

					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

					intent.putExtras(bundle);
					intent.setClass(CreateNewRule.this, RuleSettings.class);
					startActivity(intent);

				}if (getString(R.string.activity_create_new_rule_radio_content_mail)==rb.getText().toString() ){

					rule = RuleCreator.createRule(rulename.getText().toString(), RuleType.EMAIL_RULE);

					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

					intent.putExtras(bundle);
					intent.setClass(CreateNewRule.this, RuleSettings.class);
					startActivity(intent);
				}

			}
		});
	}
}
