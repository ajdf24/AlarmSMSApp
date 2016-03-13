package rieger.alarmsmsapp.view.ruleactivitys;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.view.SenderSelectionDialog;
import rieger.alarmsmsapp.model.rules.EMailRule;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for creating a sender.
 * In this activity it is possible to set all the things which are needed for the sender.
 */
public class SenderSelection extends AppCompatActivity implements SenderSelectionDialog.OnFragmentInteractionListener{

	private Rule rule;

	@Bind(R.id.activity_sender_selection_editText_for_sender_information)
	TextView sender;

	@Bind(R.id.activity_sender_selection_button_choose_contacts_for_sender)
	Button selectSenderFromContacts;

	@Bind(R.id.activity_sender_selection_button_save_sender)
	Button save;

	@Bind(R.id.activity_sender_selection_button_quit)
	Button quit;

	private static final int PICK_CONTACT = 1;

	private View layoutView;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState instance state
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender_selection);

		ButterKnife.bind(this);

		layoutView = findViewById(R.id.activity_sender_selection);

		final Snackbar snackbar = Snackbar
				.make(layoutView, R.string.sender_selection_help, Snackbar.LENGTH_LONG);
		snackbar.setAction(R.string.help, new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialog();
				snackbar.dismiss();
			}
		});

		View snackbarView = snackbar.getView();
		TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.WHITE);
		snackbar.show();

		rule = BundleHandler.getRuleFromBundle(this);

		initializeGUI();

		getRuleSettingsForGUI();

		initializeActiveElements();

	}

	void showDialog() {

		// DialogFragment.show() will take care of adding the fragment
		// in a transaction.  We also want to remove any currently showing
		// dialog, so make our own transaction and take care of that here.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		// Create and show the dialog.
		DialogFragment newFragment = SenderSelectionDialog.newInstance();
		newFragment.show(ft, "dialog");
	}

    /**
     * Overrides the method and returns the name of the activity,
     * which is necessary for the {@link rieger.alarmsmsapp.view.ruleactivitys.RuleSettings} activity.
     * @return the name of the activity
     */
	@Override
	public String toString() {
		return CreateContextForResource.getStringFromID(R.string.title_activity_sender_selection);
	}

    /**
     * This method initialize the all GUI elements.
     */
	private void initializeGUI(){


		if (rule instanceof SMSRule) {
			sender.setHint(R.string.activity_sender_selection_editText_for_sender_information_hint_sms);
		}
		if (rule instanceof EMailRule) {
			sender.setHint(R.string.activity_sender_selection_editText_for_sender_information_hint_mail);
		}

	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements(){
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				RuleCreator.changeSender(rule, sender.getText().toString());

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(SenderSelection.this, RuleSettings.class);
				startActivity(intent);

			}
		});

		selectSenderFromContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ActivityCompat.checkSelfPermission(SenderSelection.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            		ActivityCompat.requestPermissions(SenderSelection.this, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
        		}else {
					Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(intent, PICK_CONTACT);
				}
			}
		});

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(SenderSelection.this, RuleSettings.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS: {
				if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

					Snackbar snackbar = Snackbar
							.make(layoutView, R.string.toast_permission_contacts_denied, Snackbar.LENGTH_LONG);

					View snackbarView = snackbar.getView();
					TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.WHITE);
					snackbar.show();
				}
			}
		}
	}

    /**
     * {@inheritDoc}
     * This method is called after the content provider starts and get the
     * specific information from provider.
     *
     * @param reqCode
     * @param resultCode
     * @param data
     */
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		switch (reqCode) {

		case (PICK_CONTACT):

			if (rule instanceof SMSRule) {
				getNumberFromContacts(resultCode, data);
			}if(rule instanceof EMailRule){
				getMailFromContacts(resultCode, data);
			}
			break;
		}
	}

    /**
     * This method get the selected mail address from the provider.
     * @param resultCode the code for the request
     * @param data thr data
     */
	private void getMailFromContacts(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Cursor mailCursor = null;
			String mailAddress = null;
			try {
				Uri contactData = data.getData();
				Log.v(AppConstants.DEBUG_TAG, "Got a contact result: " + contactData.toString());
				Cursor contactCursor = getContentResolver().query(contactData,
						new String[] { ContactsContract.Contacts._ID }, null,
						null, null);
				String id = null;
				if (contactCursor.moveToFirst()) {
					id = contactCursor.getString(contactCursor
							.getColumnIndex(ContactsContract.Contacts._ID));
				}
				contactCursor.close();
				mailCursor = getContentResolver()
						.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
								new String[] { ContactsContract.CommonDataKinds.Email.ADDRESS },
								ContactsContract.CommonDataKinds.Email.CONTACT_ID
										+ "= ? ", new String[] { id }, null);
				if (mailCursor.moveToFirst()) {
					mailAddress = mailCursor
							.getString(mailCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

				}
			} catch (Exception e) {
				Log.e(AppConstants.DEBUG_TAG, "Failed to get email data", e);
			} finally {
				sender.setText(mailAddress);
				if (mailCursor != null) {
					mailCursor.close();
				}
				if (mailAddress == null) {
					Toast.makeText(this, this.getResources().getString(R.string.toast_mail_not_found),
							Toast.LENGTH_LONG).show();
				}
			}

		} else {
			Log.w(AppConstants.DEBUG_TAG, "Warning: activity result not ok");
		}
	}

    /**
     * This method get the selected number from the content provider and translate this number
     * to the international german format.
     * @param resultCode the code of the result
     * @param data the data
     */
	private void getNumberFromContacts(int resultCode, Intent data){
		if (resultCode == Activity.RESULT_OK) {
			Cursor phoneCursor = null;
			String phoneNumber = null;
			try {
				Uri contactData = data.getData();
				Log.v(AppConstants.DEBUG_TAG, "Got a contact result: " + contactData.toString());

				Cursor contactCursor = getContentResolver().query(contactData,
						new String[] { ContactsContract.Contacts._ID }, null,
						null, null);
				String id = null;
				if (contactCursor.moveToFirst()) {
					id = contactCursor.getString(contactCursor
							.getColumnIndex(ContactsContract.Contacts._ID));
				}
				contactCursor.close();
				phoneCursor = getContentResolver()
						.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
								new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID
										+ "= ? ", new String[] { id }, null);
				if (phoneCursor.moveToFirst()) {
					phoneNumber = phoneCursor
							.getString(phoneCursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

					try {
						PhoneNumber number = phoneUtil.parse(phoneNumber, "DE");
						phoneNumber = phoneUtil.format(number,
								PhoneNumberFormat.E164);

					} catch (NumberParseException e) {
						Log.e(AppConstants.DEBUG_TAG, "Can't parse number!");
					}

				}
				phoneCursor.close();

			} catch (Exception e) {
				Log.e(AppConstants.DEBUG_TAG, "Failed to get phone data", e);
			} finally {
				sender.setText(phoneNumber);
				if (phoneCursor != null) {
					phoneCursor.close();
				}
				if (phoneNumber == null) {
					Toast.makeText(this, this.getResources().getString(R.string.toast_number_not_found), Toast.LENGTH_LONG).show();
				}
			}
		} else {
			Log.w(AppConstants.DEBUG_TAG, "Warning: activity result not ok");
		}
	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI() {
		sender.setText(rule.getSender());
	}

	@Override
	public void onFragmentInteraction(Uri uri) {

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
            showDialog();
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
        getMenuInflater().inflate(R.menu.sender_selection, menu);

        return true;
    }
}
