package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

public class LightSettings extends AppCompatActivity {

    private Rule rule;

    private SwitchCompat activateLightSwitch;

    private AppCompatSpinner timeSpinner;

    private Button buttonSave;

    private Button buttonQuit;

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

                RuleCreator.changeLightSettings(rule, activateLightSwitch.isChecked(), lightTime);

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
        activateLightSwitch = (SwitchCompat) findViewById(R.id.activit_set_light_on_checkbox);
        timeSpinner = (AppCompatSpinner) findViewById(R.id.activity_light_settings_spinner_for_time);
        buttonSave = (Button) findViewById(R.id.activity_light_settings_button_save);
        buttonQuit = (Button) findViewById(R.id.activity_light_settings_button_quit);
    }

    @Override
    public String toString() {
        return CreateContextForResource.getStringFromID(R.string.title_activity_light_settings);
    }
}
