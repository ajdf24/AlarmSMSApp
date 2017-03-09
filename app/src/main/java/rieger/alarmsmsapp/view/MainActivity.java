package rieger.alarmsmsapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
import rieger.alarmsmsapp.model.rules.SMSRule;
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

        checkForIncomingRule();

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

            final Intent intent = new Intent(Intent.ACTION_SEND);

            intent.setType("text/plain");

            File publicFile = new File(Environment.getExternalStorageDirectory() + "/" + selectedRule.getRuleName() + ".txt");
            try {

                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(publicFile, selectedRule);

            } catch (Exception e) {
                FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "can not write to external storage");
                FirebaseCrash.report(e);
            }

            Uri uri = Uri.fromFile(publicFile);
            intent.putExtra(Intent.EXTRA_STREAM, uri);


            this.startActivity(Intent.createChooser(intent, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_selection_title)));

        } else if (item.getTitle() == getResources().getString(
                R.string.activity_rule_selection_context_menu_action_delete)) {
            RuleObserver.deleteRuleFromFilesystem(selectedRule);

            DataSource db = new DataSource(this);
            db.deleteRule(selectedRule);

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
            Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void selectedRule(Rule rule) {
        selectedRule = rule;
    }

    /**
     * This method checks the {@link Intent} for a rule.
     * Is a rule was detected so the {@link rieger.alarmsmsapp.model.rules.Rule} is saved to the system.
     */
    @SuppressLint("StringFormatInvalid")
    private void checkForIncomingRule(){
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE);
                }

                Uri sharedText = intent.getData();
                String path = getPath(sharedText);

                ObjectMapper mapper = new ObjectMapper();
                try {
                    Rule rule = mapper.readValue(new File(path), SMSRule.class);
                    DataSource db = new DataSource(this);
                    db.saveRule(rule);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(this, getString(R.string.activity_rule_selection_toast_rule_received_but_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if(isKitKat) {
            // MediaStore (and general)
            return getForApi19(uri);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @TargetApi(19)
    private String getForApi19(Uri uri) {
        Log.e(LOG_TAG, "+++ API 19 URI :: " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            Log.e(LOG_TAG, "+++ Document URI");
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                Log.e(LOG_TAG, "+++ External Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    Log.e(LOG_TAG, "+++ Primary External Document URI");
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                Log.e(LOG_TAG, "+++ Downloads External Document URI");
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                Log.e(LOG_TAG, "+++ Media Document URI");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    Log.e(LOG_TAG, "+++ Image Media Document URI");
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    Log.e(LOG_TAG, "+++ Video Media Document URI");
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    Log.e(LOG_TAG, "+++ Audio Media Document URI");
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e(LOG_TAG, "+++ No DOCUMENT URI :: CONTENT ");

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e(LOG_TAG, "+++ No DOCUMENT URI :: FILE ");
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
