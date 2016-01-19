package rieger.alarmsmsapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.control.observer.RuleObserver;
import rieger.alarmsmsapp.control.observer.VersionObserver;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.model.Version;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.ruleactivitys.CreateNewRule;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;

/**
 * Activity, which shows all existing rules.
 */
public class RuleSelection extends AppCompatActivity {

	private List<Rule> ruleList;

	private ListView listView;

	private Rule selectedRule;

	private ListAdapter listAdapter;

    private FloatingActionButton fab;

    private View layoutView;
    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState state of the saved instance
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_selection);

        checkVersion();

        layoutView = findViewById(R.id.activity_rule_selection);
//        ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
//                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_LOCATION);
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE);
//        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS);
        }
        checkForIncomingRule();

        ruleList = Collections.synchronizedList(new ArrayList<Rule>());

		ruleList = RuleObserver.readAllRulesFromFileSystem();

        showInfoDialogForTheFirstUse();

        initializeGUI();

		initializeActiveElements();

        sortRuleList();


    }

    private void checkVersion() {
        int id = 0;

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            id = getResources().getIdentifier("activity_rule_selection_whats_new_text_for_version_" + packageInfo.versionCode, "string", getPackageName());
            String value = id == 0 ? "" : (String) getResources().getText(id);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Version version = null;
        try {
            version = VersionObserver.readSettings();
        } catch (SettingsNotFoundException e) {
            e.printStackTrace();
        }

        if (version == null){

            AlertDialog dialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(CreateContextForResource.getStringFromID(R.string.activity_rule_selection_whats_new))

                    .setMessage(CreateContextForResource.getStringFromID(id))
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_launcher)

                    .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

            dialog = builder.create();

            dialog.show();

            version = new Version();
            version.setVersion(packageInfo.versionCode);
            VersionObserver.saveSettings(version);

        }else {

            if (version.getVersion() < packageInfo.versionCode) {
                System.out.println(version.getVersion());
                System.out.println(packageInfo.versionCode);
                AlertDialog dialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(CreateContextForResource.getStringFromID(R.string.activity_rule_selection_whats_new))

                        .setMessage(CreateContextForResource.getStringFromID(id))
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_launcher)

                        .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

                dialog = builder.create();

                dialog.show();
                version.setVersion(packageInfo.versionCode);
                VersionObserver.saveSettings(version);

//                AlertDialog dialog2;
//
//                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
//
//                final SpannableString s =
//                        new SpannableString("https://docs.google.com/forms/d/1MNjAA1GoYvwVxXUZFiimRdMeeqSXZc5hLdHWOdxtcNQ/viewform");
//                Linkify.addLinks(s, Linkify.ALL);
//
//
//                builder.setTitle("Umfrage zur Nutzung!")
//
//                        .setMessage("Die Teilnahme an der Umfrage hilft uns AlarmSMS weiter zu verbessern!" + s)
//                        .setCancelable(false)
//                        .setIcon(R.drawable.ic_launcher)
//
//                        .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);
//
//                dialog2 = builder2.create();
//
//                dialog2.show();
//                version.setVersion(packageInfo.versionCode);
//                VersionObserver.saveSettings(version);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_SMS: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Snackbar snackbar = Snackbar
                            .make(layoutView, R.string.toast_permission_sms_denied, Snackbar.LENGTH_LONG);

                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                    return;
                }
            }
        }
    }

    /**
     * This Method sorts the ruleList by name.
     */
    private void sortRuleList() {
        //Sorting
        Collections.sort(ruleList, new Comparator<Rule>() {
            @Override
            public int compare(Rule rule1, Rule rule2) {

                return rule1.getRuleName().compareTo(rule2.getRuleName());
            }
        });
    }

    /**
     * This method initialize the active GUI elements with listeners.
     */
    private void initializeActiveElements() {

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE,
                        (Rule) listView.getAdapter().getItem(position));
                intent.putExtras(bundle);
                intent.setClass(RuleSelection.this, RuleSettings.class);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RuleSelection.this, CreateNewRule.class));
            }
        });
        fab.setColorNormal(getResources().getColor(R.color.my_primary));
    }

    /**
     * This method initialize the all GUI elements.
     */
    private void initializeGUI() {

        listView = (ListView) findViewById(R.id.activity_rule_selection_listView);

        listAdapter = new RuleSelectionAdapter(this, R.layout.list_item_rule_selection, (ArrayList<Rule>) ruleList);

        listView.setAdapter(listAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);
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
			intent.setClass(RuleSelection.this, RuleSettings.class);
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
                Log.e("Can not write", e.getMessage());
            }

            // Get the Uri from the external file and add it to the intent
            Uri uri = Uri.fromFile(publicFile);
            intent.putExtra(Intent.EXTRA_STREAM, uri);


            this.startActivity(Intent.createChooser(intent, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_selection_title)));

		} else if (item.getTitle() == getResources().getString(
                R.string.activity_rule_selection_context_menu_action_delete)) {
            RuleObserver.deleteRuleFromFilesystem(selectedRule);
            ruleList.remove(selectedRule);

            ((BaseAdapter) listAdapter).notifyDataSetChanged();
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
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

    /**
     * Creates the options menu for this activity.
     * @param menu the options menu in which you place your items.
     * @return you must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rule_selection, menu);
		return true;
	}

    /**
     * This method handles the action for the options menu.
     * @param item the selected itel
     * @return boolean return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.department_settings_from_rule_selection) {
			startActivity(new Intent(this, DepartmentSettings.class));
			return true;
		}
		if (id == R.id.alarm_settings_from_rule_selection) {
			startActivity(new Intent(this, AlarmSettings.class));
			return true;
		}
		if (id == R.id.create_new_rule_from_rule_selection) {
			startActivity(new Intent(this, CreateNewRule.class));
			return true;
		}
        if (id == R.id.test_rules) {
            startActivity(new Intent(this, TestRule.class));
            return true;
        }
        if (id == R.id.help) {
            String url = "https://www.facebook.com/alarmsmsapp/videos/vb.1474990309460074/1477302559228849/?type=2&theater";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse(url));
            CreateContextForResource.getContext().startActivity(i);
        }

		return super.onOptionsItemSelected(item);
	}

    /**
     * Internal class, which creates a special adapter for this activity.
     */
	private class RuleSelectionAdapter extends ArrayAdapter<Rule> {

        /**
         * Constructor
         *
         * @param context The current context.
         * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.
         * @param ruleList The objects to represent in the ListView.
         */
		public RuleSelectionAdapter(Context context, int textViewResourceId,
				ArrayList<Rule> ruleList) {
			super(context, textViewResourceId, ruleList);
		}

        /**
         * Internal class, which creates a holder for the adapter elements,
         */
		private class ViewHolder {
			TextView ruleName;
			CheckBox isRuleActivated;
		}

        /**
         * {@inheritDoc}
         * Creates the view with the elements and her listeners.
         */
		@Override
		public synchronized View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder = new ViewHolder();
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.list_item_rule_selection, null);

				viewHolder.ruleName = (TextView) convertView.findViewById(R.id.list_item_rule_name);
				viewHolder.isRuleActivated = (CheckBox) convertView.findViewById(R.id.list_item_is_active);
				convertView.setTag(viewHolder);

				viewHolder.isRuleActivated.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox = (CheckBox) view;
                        Rule rule = (Rule) checkBox.getTag();
                        if(checkBox.isChecked()){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.activity_rule_selection_toast_rule_activated, rule.getRuleName()), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.activity_rule_selection_toast_rule_deactivated, rule.getRuleName()), Toast.LENGTH_SHORT).show();
                        }
                        RuleCreator.changeActive(rule,checkBox.isChecked());
                    }
                });
				viewHolder.ruleName.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, (Rule) listView.getAdapter().getItem(position));
                        intent.putExtras(bundle);
                        intent.setClass(RuleSelection.this, RuleSettings.class);
                        startActivity(intent);
                    }
                });
				viewHolder.ruleName.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View view,
                                                    ContextMenuInfo menuInfo) {

                        menu.setHeaderTitle(getResources().getString(R.string.activity_rule_selection_context_menu_title));
                        menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_edit));
                        menu.add(0, view.getId(), 0, getResources().getString(R.string.test_rule));
                        menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_send));
                        menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_delete));

                        selectedRule = (Rule) viewHolder.ruleName.getTag();
                    }

                });
			}

			Rule rule = ruleList.get(position);

            if (viewHolder.ruleName != null) {
                viewHolder.ruleName.setText(rule.getRuleName());
                viewHolder.isRuleActivated.setChecked(rule.isActive());

                /**
                 * This sets the {@link rieger.alarmsmsapp.model.rules.Rule} as tag to the ruleName and isRuleActivated,
                 * so the context menu and other methods can work with the selected rule.
                 */
                viewHolder.ruleName.setTag(rule);
                viewHolder.isRuleActivated.setTag(rule);

            }
                return convertView;
		}

	}

    /**
     * This method checks the {@link Intent} for a rule.
     * Is a rule was detected so the {@link rieger.alarmsmsapp.model.rules.Rule} is saved to the system.
     */
    private void checkForIncomingRule(){
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if ("text/plain".equals(type)) {

                Uri sharedText = intent.getData();

                if (sharedText != null) {
                    BufferedReader reader;
                    try {
                        reader = new BufferedReader( new FileReader(new File(sharedText.getPath())));
                        String line;
                        StringBuilder stringBuilder = new StringBuilder();
                        String lineSeparator = System.getProperty("line.separator");

                        while( ( line = reader.readLine() ) != null ) {
                            stringBuilder.append( line );
                            stringBuilder.append( lineSeparator );
                        }

                        String ruleContent = stringBuilder.toString();
                        System.out.println(ruleContent);
                        RuleObserver.saveSMSRuleToFileSystem(ruleContent);
                        String[] rule=ruleContent.split("\"ruleName\":\"");
                        String[] ruleName=rule[1].split("\",\"");
                        Toast.makeText(this, getString( R.string.activity_rule_selection_toast_rule_received , ruleName[0]), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Update UI to reflect text being shared
                }
            }else{
                Toast.makeText(this, getString(R.string.activity_rule_selection_toast_rule_received_but_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * This method creates a help dialog when no rules were found. (RuleSelection#ruleList size == 0)
     *
     */
    private void showInfoDialogForTheFirstUse() {
        if (ruleList.size() == 0){

            AlertDialog dialog;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_title))

                    .setMessage(CreateContextForResource.getStringFromID(R.string.activity_rule_selection_alert_dialog_text))
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_info)

                    .setPositiveButton(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button), null);

            dialog = builder.create();

            dialog.show();

        }
    }

}
