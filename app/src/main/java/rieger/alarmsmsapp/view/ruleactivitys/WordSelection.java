package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.dialoghelper.DialogHelper;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for choosing all words.
 */
public class WordSelection extends AbstractRuleActivity {

	private static final String LOG_TAG = WordSelection.class.getSimpleName();

	private Rule rule;

    @Bind(R.id.activity_word_selection_editText_included_words)
	TextView occurredWords;

    @Bind(R.id.activity_word_selection_editText_not_included_words)
	TextView notOccurredWords;

    @Bind(R.id.activity_word_selection_button_save_words)
	FloatingActionButton save;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_word_selection);
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
		return CreateContextForResource.getStringFromID(R.string.title_activity_word_selection);
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

				RuleCreator.changeWords(rule, occurredWords.getText().toString(), notOccurredWords.getText().toString());

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				bundle.putInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER, 1);
				intent.putExtras(bundle);
				intent.setClass(WordSelection.this, RuleSettings.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI() {
		occurredWords.setText(rule.getOccurredWords());
		notOccurredWords.setText(rule.getNotOccurredWords());
	}

	@Override
	protected void showHelpDialog() {
		DialogHelper.createHelpDialog(this, R.string.activity_word_selection_help_dialog_title, R.string.activity_word_selection_help_dialog_text, R.string.dialog_button_got_it);
	}
}
