package rieger.alarmsmsapp.view.ruleactivitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
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
public class SenderSelection extends AppCompatActivity {

	private Rule rule;

	private TextView sender;

	private Button selectSenderFromContacts;

	private Button save;

	private Button quit;

	private static final int PICK_CONTACT = 1;

	private View layoutView;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sender_selection);

		layoutView = findViewById(R.id.activity_sender_selection);

		rule = BundleHandler.getRuleFromBundle(this);

		initializeGUI();

		getRuleSettingsForGUI();

		initializeActiveElements();

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

		selectSenderFromContacts = (Button) findViewById(R.id.activity_sender_selection_button_choose_contacts_for_sender);
		save = (Button) findViewById(R.id.activity_sender_selection_button_save_sender);
		quit = (Button) findViewById(R.id.activity_sender_selection_button_quit);

		sender = (TextView) findViewById(R.id.activity_sender_selection_editText_for_sender_information);
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
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS: {
				if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

					Snackbar snackbar = Snackbar
							.make(layoutView, R.string.toast_permission_contacts_denied, Snackbar.LENGTH_LONG);

					View snackbarView = snackbar.getView();
					TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.WHITE);
					snackbar.show();

					return;
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
     * @param resultCode
     * @param data
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
     * @param resultCode
     * @param data
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

}
