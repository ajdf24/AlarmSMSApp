package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Abstract class, which renders the standard menu for all rule activities.
 *
 * Extended classes only must implement the method {@link AbstractRuleActivity#showHelpDialog()}
 *
 * <note>The layout file of the extended activity needs a {@link Toolbar} with the id "toolbar" in the layout file</note>
 *
 * <note>Activities which extend {@link AbstractRuleActivity} must call {@link android.app.Activity#setContentView(int)} before
 * <code>super.onCreate(Bundle)</code> is called. Otherwise the Toolbar is not shown correctly.</note>
 * Created by sebastian on 09.03.17.
 */

abstract class AbstractRuleActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    abstract protected void showHelpDialog();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        try {
            toolbar.inflateMenu(R.menu.abstract_rule_activity);
        }catch (NullPointerException e){
            throw new RuntimeException("setContentView() must be called before super.onCreate()");
        }

        setSupportActionBar(toolbar);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_HELP, true)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_HELP, false);
            editor.apply();

            ViewTarget target = new ViewTarget(toolbar.findViewById(R.id.menu_sender_selection_help));
            ShowcaseView showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setContentTitle(R.string.general_string_help_showcase_title)
                    .setContentText(R.string.general_string_help_showcase_text)
                    .hideOnTouchOutside()
                    .build();
            showcaseView.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));

        }
    }

    /**
     * This method handles the action for the options menu.
     * @param item the selected itel
     * @return boolean return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sender_selection_help){
            showHelpDialog();
        }else{
            return false;
        }

        return true;
    }

    /**
     * Creates the options menu for this activity.
     * @param menu the options menu in which you place your items.
     * @return you must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abstract_rule_activity, menu);

        return true;
    }
}
