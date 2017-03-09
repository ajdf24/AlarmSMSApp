package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.adapter.MessageListAdapter;
import rieger.alarmsmsapp.control.listener.CreateRuleFromSMSClickListener;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.view.MainActivity;

public class CreateRuleFromSMS extends AppCompatActivity {

    @Bind(R.id.activity_create_rule_from_sms_list)
    RecyclerView messageListView;

    @Bind(R.id.activity_create_rule_from_sms_progressBar)
    ProgressBar progressBar;

    List<Message> messageList = new ArrayList<Message>();

    MessageListAdapter messageListAdapter;

    LinearLayoutManager linearLayoutManager;

    Context context;

    Rule rule;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_from_sms);

        rule = BundleHandler.getRuleFromBundle(this);

        context = this;

        ButterKnife.bind(this);

        messageListAdapter = new MessageListAdapter(this, messageList, new CreateRuleFromSMSClickListener(messageListView, messageList, rule));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        new LoadSMSMessages().execute("");
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CreateRuleFromSMS Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class LoadSMSMessages extends AsyncTask<String, Integer, List<Message>> {
        protected List<Message> doInBackground(String... strings) {
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

            while (cur.moveToNext()) {
                Message message = new Message();
                message.setTimeStamp(cur.getLong(cur.getColumnIndexOrThrow("date")));
                message.setMessage(cur.getString(cur.getColumnIndexOrThrow("body")));
                message.setSender(cur.getString(cur.getColumnIndex("address")));

                messageList.add(message);
            }
            return messageList;
        }

        protected void onPostExecute(List<Message> result) {
            progressBar.setVisibility(View.INVISIBLE);
            messageListView.setAdapter(messageListAdapter);
            messageListView.setLayoutManager(linearLayoutManager);
            messageListAdapter.notifyDataSetChanged();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.dialog_choose_sms_title);
            builder.setMessage(R.string.dialog_choose_sms_text);


            builder.setPositiveButton(R.string.general_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            builder.setNegativeButton(R.string.general_string_button_quit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent().setClass(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));

            Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_create_rule_from_sms), R.string.snackbar_no_sms, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.setAction(R.string.general_string_button_quit, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CreateRuleFromSMS.this, MainActivity.class));
                }
            });
            snackbar.show();
        }
    }

}
