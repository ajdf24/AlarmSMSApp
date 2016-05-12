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
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
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
import rieger.alarmsmsapp.model.rules.AnswerBundle;
import rieger.alarmsmsapp.model.rules.EMailRule;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for creating a automatically answer after a alarm.
 * In this activity it is possible to set all the things which are needed for the answer.
 */
public class AnswerCreation extends AppCompatActivity {

	private Rule rule;

	private TextView receiver;

	private Button selectReceiverFromContext;

	private TextView message;

	private AppCompatSpinner area;

	private Button save;

	private Button quit;

	@Bind(R.id.activity_answer_creation_checkbox_send_every_time)
	SwitchCompat sendEveryTime;

	@Bind(R.id.activity_answer_creation_textView_label_for_spinner_for_answer_distance)
	TextView labelArea;

	private static final int PICK_CONTACT = 1;

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_creation);

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
		return CreateContextForResource.getStringFromID(R.string.title_activity_answer_creation);
	}

    /**
     * This method initialize the active GUI elements with listeners.
     */
	private void initializeActiveElements() {
		selectReceiverFromContext.setOnClickListener(getListenerForContactsButton());

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int distance = getDistanceFromSpinner();

				AnswerBundle answerBundle = new AnswerBundle();
				answerBundle.setReceiver(receiver.getText().toString());
				answerBundle.setMessage(message.getText().toString());
				answerBundle.setSendAnswerEveryTime(sendEveryTime.isChecked());
				answerBundle.setDistance(distance);

				RuleCreator.changeAutomaticallyAnswer(rule, answerBundle);

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(AnswerCreation.this, RuleSettings.class);
				startActivity(intent);
			}
		});

		quit.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				intent.putExtras(bundle);
				intent.setClass(AnswerCreation.this, RuleSettings.class);
				startActivity(intent);
			}
		});

		sendEveryTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked == true){
					area.setVisibility(View.INVISIBLE);
					labelArea.setVisibility(View.INVISIBLE);
				}else {
					area.setVisibility(View.VISIBLE);
					labelArea.setVisibility(View.VISIBLE);
				}
			}
		});
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
				receiver.setText(phoneNumber);
				if (phoneCursor != null) {
					phoneCursor.close();
				}
				if (phoneNumber == null) {

					View layoutView = findViewById(R.id.activity_answer_creation);

					Snackbar snackbar = Snackbar.make(layoutView, this.getResources().getString(R.string.toast_number_not_found), Snackbar.LENGTH_LONG)
							.setAction(R.string.general_string_button_choose_contact, getListenerForContactsButton());

					View snackbarView = snackbar.getView();
					TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.WHITE);
					snackbar.show();

				}
			}
		} else {
			Log.w(AppConstants.DEBUG_TAG, "Warning: activity result not ok");
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
				receiver.setText(mailAddress);
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
     * This method initialize the all GUI elements.
     */
	private void initializeGUI() {
		ButterKnife.bind(this);

		receiver = (TextView) findViewById(R.id.activity_answer_creation_editText_for_receiver);
		selectReceiverFromContext = (Button) findViewById(R.id.activity_answer_creation_button_choose_contacts_for_answer);
		message = (TextView) findViewById(R.id.activity_answer_creation_editText_for_message);
		area = (AppCompatSpinner) findViewById(R.id.activity_answer_creation_spinner_for_answer_distance);
		save = (Button) findViewById(R.id.activity_answer_creation_button_save_answer);
		quit = (Button) findViewById(R.id.activity_answer_creation_button_quit);
	}

    /**
     * This method get the selected distance from the {@link Spinner}.
     * @return the selected distance
     */
	private int getDistanceFromSpinner(){
		switch (area.getSelectedItem().toString()) {
		case "5 km":
			return 5;
		case "10 km":
			return 10;
		case "25 km":
			return  25;
		case "50 km":
			return  50;

		default:
			return 0;
		}
	}

    /**
     * This method get the current values from the rule and sets the GUI to this values.
     */
	private void getRuleSettingsForGUI() {
		receiver.setText(rule.getReceiver());
		message.setText(rule.getMessage());
		sendEveryTime.setChecked(rule.isSendEveryTime());
		if(rule.isSendEveryTime()){
			area.setVisibility(View.INVISIBLE);
			labelArea.setVisibility(View.INVISIBLE);
		}
		switch (rule.getDistance()) {
		case 5:
			area.setSelection(0);
			break;

		case 10:
			area.setSelection(1);
			break;
		case 25:
			area.setSelection(2);
			break;
		case 50:
			area.setSelection(3);
			break;
		}
	}

	/**
	 * This method creates the listener for the choose contacts button.
	 * This method also checks the permissions for android SDK 23 and uses the normal path if the android version is older than 23.
	 * @return
	 */
	private OnClickListener getListenerForContactsButton(){
		return new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ActivityCompat.checkSelfPermission(AnswerCreation.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            		ActivityCompat.requestPermissions(AnswerCreation.this, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
        		}else{
					Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(intent, PICK_CONTACT);
				}

			}
		};
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
					startActivityForResult(intent, PICK_CONTACT);

				} else {

					View layoutView = findViewById(R.id.activity_answer_creation);

					Snackbar snackbar = Snackbar
							.make(layoutView, R.string.toast_permission_contacts_denied, Snackbar.LENGTH_LONG);

					View snackbarView = snackbar.getView();
					TextView textView = (TextView)snackbarView .findViewById(android.support.design.R.id.snackbar_text);
					textView.setTextColor(Color.WHITE);
					snackbar.show();

				}
				return;
			}
		}
	}

}
