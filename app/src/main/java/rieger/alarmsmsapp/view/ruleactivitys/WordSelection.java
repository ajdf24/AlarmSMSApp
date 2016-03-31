package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for choosing all words.
 */
public class WordSelection extends AppCompatActivity {

	private static final String LOG_TAG = WordSelection.class.getSimpleName();

	private Rule rule;

    @Bind(R.id.activity_word_selection_editText_included_words)
	private TextView occurredWords;

    @Bind(R.id.activity_word_selection_editText_not_included_words)
	private TextView notOccurredWords;

    @Bind(R.id.activity_word_selection_button_save_words)
	private Button save;

    @Bind(R.id.activity_word_selection_button_quit)
	private Button quit;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_selection);

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
				intent.putExtras(bundle);
				intent.setClass(WordSelection.this, RuleSettings.class);
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
				intent.setClass(WordSelection.this, RuleSettings.class);
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
}
