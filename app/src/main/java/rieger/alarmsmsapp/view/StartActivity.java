package rieger.alarmsmsapp.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.control.observer.RuleObserver;
import rieger.alarmsmsapp.control.observer.VersionObserver;
import rieger.alarmsmsapp.control.widget.DynamicImageView;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.model.Version;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.fragments.settings.AlarmSettingsFragment;
import rieger.alarmsmsapp.view.fragments.welcome.DepartmentExplanationFragment;
import rieger.alarmsmsapp.view.fragments.settings.DepartmentFragment;
import rieger.alarmsmsapp.view.fragments.welcome.ReadyFragment;
import rieger.alarmsmsapp.view.fragments.welcome.WelcomeFragment;
import rieger.alarmsmsapp.view.ruleactivitys.CreateNewRule;

/**
 * This class is the start activity, which checks the permissions and the settings if the first use.
 * Created by sebastian on 19.08.15.
 */
public class StartActivity extends AppCompatActivity implements WelcomeFragment.OnFragmentInteractionListener,
                                                                AlarmSettingsFragment.OnFragmentInteractionListener,
                                                                DepartmentExplanationFragment.OnFragmentInteractionListener,
                                                                DepartmentFragment.OnFragmentInteractionListener,
                                                                ReadyFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    @Bind(R.id.start_activity_button_next)
    Button buttonNext;

    AlarmSettingsFragment alarmSettingsFragment;

    DepartmentFragment departmentFragment;

    SharedPreferences prefs;

    int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_activity);

        ButterKnife.bind(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        prefs = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, true)) {
            // run your one time code

            checkVersionAndUpdate();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final WelcomeFragment welcomeFragment = new WelcomeFragment();
            fragmentTransaction.add(R.id.fragment_container, welcomeFragment, "WelcomeFragment");
            fragmentTransaction.commit();

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startConfiguration(welcomeFragment);
                }
            });
        }else{
            Intent intent = new Intent();

            intent.setClass(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    /**
     * This method redirect the user if there are no settings are found.
     * @deprecated
     */
    @Deprecated
    private void redirectForFirstUse() {
        try {
            AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e) {
                Log.i(this.getClass().getSimpleName(), "Alarm Settings not set. Start Alarm Settings");
                startActivity(new Intent(this, AlarmSettings.class));
                return;
        }

        try {
            DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.i(this.getClass().getSimpleName(), "Department Settings not set. Start Alarm Settings");
            startActivity(new Intent(this, DepartmentSettings.class));
            return;
        }

        startActivity(new Intent(this, MainActivity.class));
    }

    private void startNextActivity(){
        Intent intent = new Intent();

        intent.setClass(StartActivity.this, CreateNewRule.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void startConfiguration(Fragment welcomeFragment){
        if (clickCounter == 0) {

            final DynamicImageView imageView = (DynamicImageView) welcomeFragment.getView().findViewById(R.id.view2);
            final TextView mainText = (TextView) welcomeFragment.getView().findViewById(R.id.textView13);
            final TextView mainText2 = (TextView) welcomeFragment.getView().findViewById(R.id.textView2);

            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
            animation.setDuration(500);
            animation.setFillAfter(false);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.GONE);
                    mainText.setText("AlarmSMS nutzt globale Alarmeinstellungen, welche für jede Alarmierung gelten.");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            imageView.startAnimation(animation);


            mainText.startAnimation(animation);
            mainText2.startAnimation(animation);

        }
        if (clickCounter == 1) {


            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            alarmSettingsFragment = new AlarmSettingsFragment();

            ft.replace(R.id.fragment_container, alarmSettingsFragment, "AlarmSettingsFragment");
            // Start the animated transition.
            ft.commit();
        }
        if(clickCounter == 2){

            alarmSettingsFragment.saveData();

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            DepartmentExplanationFragment departmentExplanationFragment = new DepartmentExplanationFragment();

            ft.replace(R.id.fragment_container, departmentExplanationFragment, "DepartmentExplanationFragment");
            // Start the animated transition.
            ft.commit();
        }
        if(clickCounter == 3){

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            departmentFragment = new DepartmentFragment();

            ft.replace(R.id.fragment_container, departmentFragment, "DepartmentFragment");
            // Start the animated transition.
            ft.commit();
        }
        if(clickCounter == 4){

            departmentFragment.saveData();

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            ReadyFragment readyFragment = new ReadyFragment();

            ft.replace(R.id.fragment_container, readyFragment, "ReadyFragment");
            // Start the animated transition.
            ft.commit();

        }
        if(clickCounter == 5){

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, false);
            editor.commit();
            startNextActivity();

        }
        clickCounter++;
    }

    /**
     * Check if an old version was found.
     * If the settings from a former version was found the method import this this to the database.
     */
    private void checkVersionAndUpdate() {
        int id = 0;

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            id = getResources().getIdentifier("activity_rule_selection_whats_new_text_for_version_" + packageInfo.versionCode, "string", getPackageName());
            String value = id == 0 ? "" : (String) getResources().getText(id);


            Version version = null;
            try {
                version = VersionObserver.readSettings();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }

            if (version == null){
                //Erster Aufruf nach Installation

                version = new Version();
                version.setVersion(packageInfo.versionCode);
                VersionObserver.saveSettings(version);

            }else {

                if (version.getVersion() < packageInfo.versionCode) {

                    boolean error = false;

                    //Alte Version gefunden --> import settings to database

                    ProgressDialog dialog = ProgressDialog.show(this, "Importiere Einstellungen von alter Version", "Gerätehaus", true);

                    DataSource source = new DataSource(this);
                    try {
                        DepartmentSettingsModel department = DepartmentObserver.readSettings();
                        source.saveDepartment(department);
                    } catch (SettingsNotFoundException e) {
                        e.printStackTrace();
                        error = true;
                    }

                    dialog.setMessage("Alarmeinstellungen");

                    try {
                        AlarmSettingsModel alarmSettingsModel = AlarmSettingsObserver.readSettings();
                        source.saveAlarmSetting(alarmSettingsModel);
                    } catch (SettingsNotFoundException e) {
                        e.printStackTrace();
                        error = true;
                    }

                    dialog.setMessage("Regeln");

                    try {
                        List<Rule> rules = RuleObserver.readAllRulesFromFileSystem();

                        for (Rule rule : rules) {
                            source.saveRule(rule);
                        }
                    }catch (Exception e){
                        error = true;
                    }

                    dialog.dismiss();

                    if(error){
                        AlertDialog errorDialog;

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);

                        builder.setTitle("Fehler beim Import")

                                .setMessage("Es konnten nicht alle Einstellungen übertragen werden. Bitte stell die App neu ein!")
                                .setCancelable(false)
                                .setIcon(R.drawable.ic_launcher)

                                .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

                        errorDialog = builder.create();

                        errorDialog.show();
                    }else {
                        startNextActivity();
                    }

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
