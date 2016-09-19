package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.socialnetworks.StringCreator;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for creating a facebook post.
 * In this activity it is possible to set all the things which are needed for the post.
 */
public class TwitterPostSelection extends AppCompatActivity {

	private static final String LOG_TAG = TwitterPostSelection.class.getSimpleName();

    private static final int maximalCharacters = AppConstants.MAXIMAL_TWITTER_LENGTH;

    private int remainingCharacters = maximalCharacters;

	private Rule rule;

    @Bind(R.id.activity_facebook_post_selection_editText_message_for_facebook)
	TextView post;

    @Bind(R.id.activity_facebook_post_selection_textView_character_counter)
    TextView characterCounter;

    @Bind(R.id.activity_facebook_post_selection_checkBox_add_message_text)
	CheckBox addMessage;

    @Bind(R.id.activity_facebook_post_selection_button_save_facebook_post)
	FloatingActionButton save;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_post_selection);

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
		return CreateContextForResource.getStringFromID(R.string.title_activity_twitter_post_selection);
	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI() {
        ButterKnife.bind(this);

        characterCounter.setText(StringCreator.getCharacterCounterString(maximalCharacters,remainingCharacters));
	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements() {
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				RuleCreator.changeFacebookPost(rule, post.getText().toString(), addMessage.isChecked());

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				bundle.putInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER, 2);
				intent.putExtras(bundle);
				intent.setClass(TwitterPostSelection.this, RuleSettings.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

        post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= maximalCharacters) {
                    remainingCharacters = maximalCharacters - s.length();
                    characterCounter.setText(StringCreator.getCharacterCounterString(maximalCharacters, remainingCharacters));
                }else{
                    post.setText(s.subSequence(0,maximalCharacters));
                    int newCursorPosition = post.length();
                    Editable textForTheCursor = (Editable) post.getText();
                    Selection.setSelection(textForTheCursor, newCursorPosition);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI(){
		post.setText(rule.getMessageToPostOnTwitter());
		addMessage.setChecked(rule.isAddMessageToTwitterPost());
	}

}
