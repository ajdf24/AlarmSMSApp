package rieger.alarmsmsapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

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
import rieger.alarmsmsapp.control.listener.AnimationListener;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.fragments.settings.AlarmSettingsFragment;
import rieger.alarmsmsapp.view.fragments.welcome.DepartmentExplanationFragment;
import rieger.alarmsmsapp.view.fragments.settings.DepartmentFragment;
import rieger.alarmsmsapp.view.fragments.welcome.ReadyFragment;
import rieger.alarmsmsapp.view.fragments.welcome.RightsFragment;
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

    RightsFragment rightsFragment;

    int clickCounter = 0;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_activity);

        ButterKnife.bind(this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS},
//                AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS);


        prefs = PreferenceManager.getDefaultSharedPreferences(StartActivity.this);
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, true)) {

            checkVersionAndUpdate();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            final WelcomeFragment welcomeFragment = new WelcomeFragment();
            fragmentTransaction.add(R.id.fragment_container, welcomeFragment, AppConstants.Fragments.WELCOME_FRAGMENT);
            fragmentTransaction.commit();

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startConfiguration(welcomeFragment);
                }
            });
        }else{
            startNextActivity(MainActivity.class);
        }

    }

    /**
     * start the next activity
     * @param cls the activity which should be started
     */
    private void startNextActivity(Class<?> cls){
        Intent intent = new Intent();

        intent.setClass(StartActivity.this, cls);
        startActivity(intent);
        finish();
    }

    /**
     * {@inheritDoc}
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * This method navigate the user through the different start fragments with the main settings.
     * @param welcomeFragment the start fragment
     */
    private void startConfiguration(Fragment welcomeFragment){
        if (clickCounter == 0) {
            try {
                final DynamicImageView imageView = (DynamicImageView) welcomeFragment.getView().findViewById(R.id.welcome_fragment_main_image);
                final TextView headLineText = (TextView) welcomeFragment.getView().findViewById(R.id.welcome_fragment_headline);
                final TextView welcomeText = (TextView) welcomeFragment.getView().findViewById(R.id.welcome_fragment_info_text);

                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
                animation.setDuration(500);
                animation.setFillAfter(false);

                animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView.setVisibility(View.GONE);
                        welcomeText.setText(CreateContextForResource.getStringFromID(R.string.welcome_fragment_info_text_2));
                        welcomeText.setGravity(Gravity.CENTER);
                    }
                });

                imageView.startAnimation(animation);

                headLineText.startAnimation(animation);
                welcomeText.startAnimation(animation);
            }catch (NullPointerException e){
                FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "View not found");
                FirebaseCrash.report(e);
            }
        }
        if (clickCounter == 1) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            alarmSettingsFragment = new AlarmSettingsFragment();

            ft.replace(R.id.fragment_container, alarmSettingsFragment, AppConstants.Fragments.ALARM_SETTINGS_FRAGMENT);
            ft.commit();
        }
        if(clickCounter == 2){
            alarmSettingsFragment.saveData();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            DepartmentExplanationFragment departmentExplanationFragment = new DepartmentExplanationFragment();

            ft.replace(R.id.fragment_container, departmentExplanationFragment, AppConstants.Fragments.DEPARTMENT_EXPLANATION_FRAGMENT);
            ft.commit();
        }
        if(clickCounter == 3){
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            departmentFragment = new DepartmentFragment();

            ft.replace(R.id.fragment_container, departmentFragment, AppConstants.Fragments.DEPARTMENT_FRAGMENT);
            ft.commit();
        }
        if(clickCounter == 4){
            departmentFragment.saveData();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            rightsFragment = new RightsFragment();

            ft.replace(R.id.fragment_container, rightsFragment, "Rights_Fragment");
            ft.commit();
        }
        if(clickCounter == 5){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS);
            rightsFragment.changeTest(clickCounter);
        }
        if(clickCounter == 6){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_LOCATION);
            rightsFragment.changeTest(clickCounter);
        }
        if(clickCounter == 7){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

            ReadyFragment readyFragment = new ReadyFragment();

            ft.replace(R.id.fragment_container, readyFragment, AppConstants.Fragments.READY_FRAGMENT);
            ft.commit();
        }
        if(clickCounter == 8){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, false);
            editor.apply();
            startNextActivity(CreateNewRule.class);
        }


        clickCounter++;
    }

    /**
     * Check if an old version was found.
     * If the settings from a former version was found the method import this this to the database.
     * @deprecated will be deleted in v3.0
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
                    ProgressDialog dialog = ProgressDialog.show(this, CreateContextForResource.getStringFromID(R.string.import_settings_dialog_title), CreateContextForResource.getStringFromID(R.string.import_settings_dialog_department), true);

                    DataSource source = new DataSource(this);
                    try {
                        DepartmentSettingsModel department = DepartmentObserver.readSettings();
                        source.saveDepartment(department);
                    } catch (SettingsNotFoundException e) {
                        e.printStackTrace();
                        error = true;
                    }

                    dialog.setMessage(CreateContextForResource.getStringFromID(R.string.import_settings_dialog_alarm_settings));
                    try {
                        AlarmSettingsModel alarmSettingsModel = AlarmSettingsObserver.readSettings();
                        source.saveAlarmSetting(alarmSettingsModel);
                    } catch (SettingsNotFoundException e) {
                        e.printStackTrace();
                        error = true;
                    }

                    dialog.setMessage(CreateContextForResource.getStringFromID(R.string.import_settings_dialog_rules));

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

                        builder.setTitle(R.string.import_settings_dialog_import_error)
                                .setMessage(R.string.import_settings_dialog_import_error_message)
                                .setCancelable(false)
                                .setIcon(R.drawable.ic_launcher)
                                .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

                        errorDialog = builder.create();
                        errorDialog.show();
                    }else {

                        List<Rule> rules = RuleObserver.readAllRulesFromFileSystem();
                        for (Rule rule : rules) {
                            RuleObserver.deleteRuleFromFilesystem(rule);
                        }

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, false);
                        editor.apply();

                        Intent intent = new Intent();

                        intent.setClass(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Version string not found");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("SMS Recht verweigert")
                        .setMessage("Die App arbeitet nur korrekt, wenn dieses Recht gewährt wird!")
                        .setCancelable(false)
                        .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.RECEIVE_SMS},
                                        AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS);
                            }
                        })
                        .setNegativeButton(CreateContextForResource.getStringFromID(R.string.general_string_button_quit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAnalytics.logEvent("sms_right_not_granted", null);
                                finish();
                            }
                        });

                AlertDialog errorDialog = builder.create();
                errorDialog.show();
            }
        }

    }
}
