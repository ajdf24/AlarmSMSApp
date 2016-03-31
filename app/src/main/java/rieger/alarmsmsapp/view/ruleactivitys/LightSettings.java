package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Activity, on which the user can set the light settings for a rule
 */
public class LightSettings extends AppCompatActivity {

    private static final String LOG_TAG = LightSettings.class.getSimpleName();

    private Rule rule;

    @Bind(R.id.activit_set_light_on_checkbox)
    SwitchCompat activateLightSwitch;

    @Bind(R.id.activit_set_light_on_when_dark_checkbox)
    SwitchCompat activateWhenDarkSwitch;

    @Bind(R.id.activity_light_settings_spinner_for_time)
    AppCompatSpinner timeSpinner;

    @Bind(R.id.activity_light_settings_button_save)
    Button buttonSave;

    @Bind(R.id.activity_light_settings_button_quit)
    Button buttonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_settings);

        rule = BundleHandler.getRuleFromBundle(this);

        initializeGUI();

        getRuleSettingsForGUI();

        initializeActiveElements();
    }

    private void initializeActiveElements() {

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lightTime = 0;
                switch (timeSpinner.getSelectedItemPosition()){
                    case 0:
                        lightTime = 30000;
                        break;
                    case 1:
                        lightTime = 60000;
                        break;
                    case 2:
                        lightTime = 120000;
                        break;
                    case 3:
                        lightTime = 180000;
                        break;
                }

                RuleCreator.changeLightSettings(rule, activateLightSwitch.isChecked(), lightTime, activateWhenDarkSwitch.isChecked());

                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
                intent.putExtras(bundle);
                intent.setClass(LightSettings.this, RuleSettings.class);
                startActivity(intent);
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
                intent.putExtras(bundle);
                intent.setClass(LightSettings.this, RuleSettings.class);
                startActivity(intent);
            }
        });
    }

    private void getRuleSettingsForGUI() {
        activateLightSwitch.setChecked(rule.isActivateLight());
        activateWhenDarkSwitch.setChecked(rule.isActivateLightOnlyWhenDark());

        switch (rule.getLightTime()){
            case 30000:
                timeSpinner.setSelection(0);
                break;
            case 60000:
                timeSpinner.setSelection(1);
                break;
            case 120000:
                timeSpinner.setSelection(2);
                break;
            case 180000:
                timeSpinner.setSelection(3);
                break;
        }
    }

    private void initializeGUI(){
        ButterKnife.bind(this);
    }

    @Override
    public String toString() {
        return CreateContextForResource.getStringFromID(R.string.title_activity_light_settings);
    }
}
