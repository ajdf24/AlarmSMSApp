package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.RuleType;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.MainActivity;
import rieger.alarmsmsapp.view.RuleSelection;

/**
 * This activity is for creating a new rule, with the name of the rule
 * and the type.
 */
public class CreateNewRule extends AppCompatActivity {

	private static final String LOG_TAG = CreateNewRule.class.getSimpleName();

	@Bind(R.id.activity_create_new_rule_editText_rule_name)
	TextView rulename;

	@Bind(R.id.activity_create_new_rule_radioGroup_content_chooser)
	RadioGroup ruleType;

	@Bind(R.id.activity_create_new_rule_button_save_rule_name)
	FloatingActionButton save;

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

		showCases();

	}

	private void showCases(){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE, true)) {
			// run your one time code
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE, false);
			editor.commit();

			ShowcaseView showcaseView = new ShowcaseView.Builder(this)
					.setTarget(new ViewTarget(R.id.activity_create_new_rule_editText_rule_name, this))
					.setContentTitle("Namenwahl")
					.setStyle(com.github.amlcurran.showcaseview.R.style.TextAppearance_ShowcaseView_Detail_Light)
					.setContentText("Wähle einen Namen für deine Regel")
					.hideOnTouchOutside()
					.blockAllTouches()
					.build();

			showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
				@Override
				public void onShowcaseViewHide(ShowcaseView showcaseView) {

				}

				@Override
				public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
					ShowcaseView view = new ShowcaseView.Builder(CreateNewRule.this)
							.setTarget(new ViewTarget(R.id.activity_create_new_rule_button_save_rule_name, CreateNewRule.this))
							.setContentTitle("Speichern")
							.setStyle(com.github.amlcurran.showcaseview.R.style.TextAppearance_ShowcaseView_Detail_Light)
							.setContentText("Speichern und weiter")
							.hideOnTouchOutside()
							.blockAllTouches()
							.build();
				}

				@Override
				public void onShowcaseViewShow(ShowcaseView showcaseView) {

				}

				@Override
				public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

				}
			});
		}
	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI(){
		setContentView(R.layout.activity_create_new_rule);

		ButterKnife.bind(this);

	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements(){

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View view = CreateNewRule.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				Log.d(LOG_TAG, rulename.getText().toString());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				RadioButton rb = (RadioButton) findViewById(ruleType.getCheckedRadioButtonId());
				if(getString(R.string.activity_create_new_rule_radio_content_sms)==rb.getText().toString() ){

					DataSource db = new DataSource(CreateNewRule.this);

					rule = new SMSRule();
					rule.setRuleName(rulename.getText().toString());

//					rule = RuleCreator.createRule(rulename.getText().toString(), RuleType.SMS_RULE);

					rule = db.createRule(rule);

					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

					intent.putExtras(bundle);
					intent.setClass(CreateNewRule.this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);

				}
//				if (getString(R.string.activity_create_new_rule_radio_content_mail)==rb.getText().toString() ){
//
//					rule = RuleCreator.createRule(rulename.getText().toString(), RuleType.EMAIL_RULE);
//
//					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
//
//					intent.putExtras(bundle);
//					intent.setClass(CreateNewRule.this, RuleSettings.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intent);
//				}

			}
		});
	}
}
