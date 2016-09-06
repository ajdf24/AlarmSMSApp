package rieger.alarmsmsapp.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.receiver.SMSReceiver;
import rieger.alarmsmsapp.util.AppConstants;

public class TestRule extends AppCompatActivity {

    private static final String LOG_TAG = TestRule.class.getSimpleName();

    @Bind(R.id.activity_test_rule_editText_for_sender_information)
    EditText numberTextFiled;

    @Bind(R.id.activity_test_rule_message_editText)
    EditText messageTextFiled;

    @Bind(R.id.activity_test_rule_button_test)
    FloatingActionButton testButon;

    @Bind(R.id.activity_test_rule_button_choose_contacts_for_sender)
    Button chooseContactButton;

    private String number;

    private String message;

    private static final int PICK_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rule);

        initializeGUT();

        initializeActiveElements();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            number = (String) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_NUMBER);
            numberTextFiled.setText(number);

            message = (String) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_MESSAGE);
            messageTextFiled.setText(message);

            SMSReceiver smsReceiver = new SMSReceiver();
            smsReceiver.testReceiver(number,message);

            finish();
        }
    }

    private void initializeActiveElements() {

        testButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = numberTextFiled.getText().toString();
                message = messageTextFiled.getText().toString();

                SMSReceiver smsReceiver = new SMSReceiver();
                smsReceiver.testReceiver(number, message);

                finish();
            }
        });

        chooseContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(TestRule.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TestRule.this, new String[]{Manifest.permission.READ_CONTACTS},
                            AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            }
        });
    }

    private void initializeGUT() {

        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_rule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
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

                    getNumberFromContacts(resultCode, data);
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
                Log.v(LOG_TAG, "Got a contact result: " + contactData.toString());

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
                        Phonenumber.PhoneNumber number = phoneUtil.parse(phoneNumber, "DE");
                        phoneNumber = phoneUtil.format(number,
                                PhoneNumberUtil.PhoneNumberFormat.E164);

                    } catch (NumberParseException e) {
                        Log.e(LOG_TAG, "Can't parse number!");
                    }

                }
                phoneCursor.close();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to get phone data", e);
            } finally {
                numberTextFiled.setText(phoneNumber);
                if (phoneCursor != null) {
                    phoneCursor.close();
                }
                if (phoneNumber == null) {
                    Toast.makeText(this, this.getResources().getString(R.string.toast_number_not_found), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Log.w(LOG_TAG, "Warning: activity result not ok");
        }
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

                    View layoutView = findViewById(R.id.activity_test_rule);

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
