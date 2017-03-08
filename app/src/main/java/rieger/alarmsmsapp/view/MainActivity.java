package rieger.alarmsmsapp.view;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.RuleSelected;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.observer.RuleObserver;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.fragments.bottombar.*;
import rieger.alarmsmsapp.view.fragments.bottombar.RuleSelection;
import rieger.alarmsmsapp.view.fragments.settings.AlarmSettingsFragment;
import rieger.alarmsmsapp.view.fragments.settings.DepartmentFragment;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;

public class MainActivity extends AppCompatActivity implements
                                                    AlarmSettingsFragment.OnFragmentInteractionListener,
                                                    DepartmentFragment.OnFragmentInteractionListener,
                                                    AlarmChart.OnFragmentInteractionListener, RuleSelected, Serializable{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.activity_main_coordinator_layout)
    RelativeLayout coordinatorLayout;

    @Bind(R.id.navigation)
    BottomNavigationView bottomNavigationView;

    private RuleSelection ruleSelection;
    private AlarmSettingsFragment alarmSettings;
    private DepartmentFragment departmentSettings;
    private AlarmChart alarmChart;

    private int currentFragment = 0;

    private Rule selectedRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_LOCATION);
        }

        ButterKnife.bind(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ruleSelection = rieger.alarmsmsapp.view.fragments.bottombar.RuleSelection.newInstance(this);

        ft.add(R.id.fragment_container, ruleSelection, "RuleSelectionFragment");
        ft.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if(alarmSettings != null) {
                alarmSettings.saveData();
            }
            if(departmentSettings != null) {
                departmentSettings.saveData();
            }

            switch (item.getItemId()) {
                case R.id.bottomBarItemOne:

                    setAnimation(ft, currentFragment, 0);

                    ruleSelection = RuleSelection.newInstance(MainActivity.this);

                    ft.replace(R.id.fragment_container, ruleSelection, "RuleSelectionFragment");
                    ft.commit();
                    currentFragment = 0;

                    return true;
                case R.id.bottomBarItemTwo:

                    setAnimation(ft, currentFragment, 1);

                    alarmSettings = new AlarmSettingsFragment();

                    ft.replace(R.id.fragment_container, alarmSettings, "AlarmSettingsFragment");
                    ft.commit();
                    currentFragment = 1;

                    return true;
                case R.id.bottomBarItemThree:

                    setAnimation(ft, currentFragment, 2);

                    departmentSettings = new DepartmentFragment();

                    ft.replace(R.id.fragment_container, departmentSettings, "DepartmentSettingsFragment");
                    ft.commit();
                    currentFragment = 2;

                    return true;
                case R.id.bottomBarItemFour:


                    setAnimation(ft, currentFragment, 3);

                    alarmChart = new AlarmChart();

                    ft.replace(R.id.fragment_container, alarmChart, "AlarmChartFragment");
                    ft.commit();
                    currentFragment = 3;

                    return true;
            }
            return false;
        }

    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setAnimation(FragmentTransaction fragmentTransaction, int from, int to){
        if(to == 0){
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
        }
        if(to == 1){
            if(from == 0){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            }
            if(from > 1){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        }
        if(to == 2){
            if(from < 2) {
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            }
            if(from > 2){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        }
        if(to == 3){
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(grantResults.length > 0) {
            switch (requestCode) {
                case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS:
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        Snackbar snackbar = Snackbar
                                .make(this.findViewById(android.R.id.content), R.string.toast_permission_sms_denied, Snackbar.LENGTH_LONG);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();

                        return;
                    }
                    break;
                case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE:
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, R.string.toast_permission_storage_denied, Snackbar.LENGTH_LONG);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    break;
                case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_LOCATION:
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, R.string.toast_permission_location_denied, Snackbar.LENGTH_LONG);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.WHITE);
                        snackbar.show();
                    }
                    break;
            }
        }
    }

    /**
     * This method start a action after click on a item in the context menu
     * @param item The context menu item that was selected.
     * @return boolean Return false to allow normal context menu processing to
     *         proceed, true to consume it here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == getResources().getString(
                R.string.activity_rule_selection_context_menu_action_edit)) {

            Intent intent = new Intent();
            Bundle bundle = new Bundle();

            bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE,
                    selectedRule);
            intent.putExtras(bundle);
            intent.setClass(this, RuleSettings.class);
            startActivity(intent);

        } else if (item.getTitle() == getResources().getString(
                R.string.activity_rule_selection_context_menu_action_send)) {

            // Create the intent
            final Intent intent = new Intent(Intent.ACTION_SEND);

            // set the MIME type and grant access to the uri (for the attached file, although I'm not sure if the grant access is required)
            intent.setType("text/plain");
            //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


            // Copy file to external storage
            File publicFile = new File("/sdcard/" + selectedRule.getRuleName());
            try {
                InputStream initialStream = new FileInputStream(new File(RuleObserver.getUriFromSMSRule(selectedRule.getRuleName()).getPath()));
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);

                OutputStream outStream = new FileOutputStream(publicFile);
                outStream.write(buffer);
                initialStream.close();
                outStream.close();
            } catch (Exception e) {
                FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "can not write to external storage");
                FirebaseCrash.report(e);
            }

            // Get the Uri from the external file and add it to the intent
            Uri uri = Uri.fromFile(publicFile);
            intent.putExtra(Intent.EXTRA_STREAM, uri);


            this.startActivity(Intent.createChooser(intent, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_selection_title)));

        } else if (item.getTitle() == getResources().getString(
                R.string.activity_rule_selection_context_menu_action_delete)) {
            RuleObserver.deleteRuleFromFilesystem(selectedRule);

            DataSource db = new DataSource(this);
            db.deleteRule(selectedRule);
//            ruleList.remove(selectedRule);

//            ruleSelection.notifyDataSetChanced();

            Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);

        }else if (item.getTitle() == getResources().getString(R.string.test_rule)) {

            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.BUNDLE_CONTEXT_NUMBER, selectedRule.getSender());
            bundle.putString(AppConstants.BUNDLE_CONTEXT_MESSAGE, selectedRule.getOccurredWords());
            Intent intent = new Intent(this, TestRule.class);
            intent.putExtras(bundle);

            startActivity(intent);
        }else {
            return false;
        }
        return true;
    }

    public void onFragmentInteraction(Rule rule){
        selectedRule = rule;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(ruleSelection != null){
//            ruleSelection.notifyDataSetChanced();

            Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void selectedRule(Rule rule) {
        selectedRule = rule;
    }
}
