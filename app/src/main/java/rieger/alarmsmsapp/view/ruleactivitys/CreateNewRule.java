package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.MainActivity;

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

	private boolean saveVisible = false;
	RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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

		lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
		lps.setMargins(margin, margin, margin, margin);

		showCases();

	}

	private void showCases(){

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE, true)) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE, false);
			editor.apply();

			ShowcaseView showcaseView = new ShowcaseView.Builder(this)
					.setTarget(new ViewTarget(R.id.activity_create_new_rule_editText_rule_name, this))
					.setContentTitle(R.string.showcase_rule_name_title)
					.setStyle(R.style.CustomShowcaseTheme)
					.setContentText(R.string.showcase_rule_name_text)
					.hideOnTouchOutside()
					.build();
			showcaseView.setButtonPosition(lps);
			showcaseView.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));

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

		if(rulename.length() > 0){
			save.setVisibility(View.VISIBLE);
			saveVisible = true;
		}

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View view = CreateNewRule.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				Log.d(LOG_TAG, rulename.getText().toString());
				final Intent intent = new Intent();
				Bundle bundle = new Bundle();

				RadioButton rb = (RadioButton) findViewById(ruleType.getCheckedRadioButtonId());
				if(getString(R.string.activity_create_new_rule_radio_content_sms)==rb.getText().toString() ){

					DataSource db = new DataSource(CreateNewRule.this);

					rule = new SMSRule();
					rule.setRuleName(rulename.getText().toString());

					rule = db.saveRule(rule);

					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

					intent.putExtras(bundle);

					AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewRule.this, R.style.AlertDialogCustom);
					builder.setTitle(R.string.dialog_create_alarm_from_sms_title);
					builder.setMessage(R.string.dialog_create_alarm_from_sms_text);


					builder.setPositiveButton(R.string.general_yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
								intent.setClass(CreateNewRule.this, CreateRuleFromSMS.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

								CreateNewRule.this.finish();
							}else {
								final int REQUEST_CODE_ASK_PERMISSIONS = 123;
								ActivityCompat.requestPermissions(CreateNewRule.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
							}
						}
					});
					builder.setNegativeButton(R.string.general_no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							intent.setClass(CreateNewRule.this, MainActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);

							CreateNewRule.this.finish();
						}
					});

					AlertDialog dialog = builder.create();
					dialog.show();
					dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
					dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));

				}
			}
		});
		rulename.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0){
					if(!saveVisible) {
						Animation showSave = AnimationUtils.loadAnimation(CreateNewRule.this, R.anim.expand_in);
						save.startAnimation(showSave);
						save.setVisibility(View.VISIBLE);
						saveVisible = true;
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreateNewRule.this);
						if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE_SAVE, true)) {
							SharedPreferences.Editor editor = prefs.edit();
							editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_CREATE_RULE_SAVE, false);
							editor.apply();
							ShowcaseView view = new ShowcaseView.Builder(CreateNewRule.this)
									.setTarget(new ViewTarget(R.id.activity_create_new_rule_button_save_rule_name, CreateNewRule.this))
									.setContentTitle(R.string.general_string_button_save)
									.setStyle(R.style.CustomShowcaseTheme)
									.setContentText(R.string.showcase_rule_name_save_text)
									.hideOnTouchOutside()
									.build();
							view.setButtonPosition(lps);
							view.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));
						}
					}
				}else {
					Animation showSave = AnimationUtils.loadAnimation(CreateNewRule.this, R.anim.expand_out);
					save.startAnimation(showSave);
					save.setVisibility(View.INVISIBLE);
					saveVisible = false;
				}

			}
		});

	}
}
