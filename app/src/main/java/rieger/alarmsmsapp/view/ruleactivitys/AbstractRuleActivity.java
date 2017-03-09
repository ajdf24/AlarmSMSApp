package rieger.alarmsmsapp.view.ruleactivitys;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import rieger.alarmsmsapp.R;

/**
 * Abstract class, which renders the standard menu for all rule activities.
 *
 * Extended classes only must implement the method {@link AbstractRuleActivity#showHelpDialog()}
 * Created by sebastian on 09.03.17.
 */

abstract class AbstractRuleActivity extends AppCompatActivity {

    abstract protected void showHelpDialog();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.abstract_rule_activity, menu);

        return true;
    }
}
