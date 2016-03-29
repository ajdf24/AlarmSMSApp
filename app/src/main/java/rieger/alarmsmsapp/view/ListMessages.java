package rieger.alarmsmsapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.ContactsWorker;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

public class ListMessages extends AppCompatActivity {

    List<Message> messageList = new ArrayList<Message>();

    @Bind(R.id.activity_list_messages_list_view)
    ListView messageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        int currentMessage = 0;
        Message message = (Message) intent.getExtras().getSerializable(AppConstants.BUNDLE_CONTEXT_SERIALIZED_MESSAGE + currentMessage);
        while (message != null){
            messageList.add(message);
            currentMessage++;
            message = (Message) intent.getExtras().getSerializable(AppConstants.BUNDLE_CONTEXT_SERIALIZED_MESSAGE + currentMessage);
        }

        ListAdapter messageListAdapter = new MessageListAdapter(getApplicationContext(), R.layout.list_item_message, messageList);
        messageListView.setAdapter(messageListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MessageListAdapter extends ArrayAdapter<Message> {

        public MessageListAdapter(Context context, int textViewResourceId, List<Message> objects) {
            super(context, textViewResourceId, objects);
        }

        private class ViewHolder{
            TextView messageBody;
            TextView messageDate;
            TextView messageSender;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_message, null);

            viewHolder.messageBody = (TextView) convertView.findViewById(R.id.list_item_message_body);
            viewHolder.messageDate = (TextView) convertView.findViewById(R.id.list_item_message_date);
            viewHolder.messageSender = (TextView) convertView.findViewById(R.id.list_item_message_sender);

            viewHolder.messageBody.setText(messageList.get(position).getMessage());
            viewHolder.messageDate.setText(
                    new SimpleDateFormat(
                            CreateContextForResource.getStringFromID(
                                    R.string.general_string_date_format)).format(
                            messageList.get(position).getTimeStamp()));

            String contactName = ContactsWorker.getContactName(ListMessages.this, messageList.get(position).getSender());
            if(contactName != null){
                viewHolder.messageSender.setText(contactName);
            }else {
                viewHolder.messageSender.setText(messageList.get(position).getSender());
            }

            convertView.setTag(viewHolder);
            return convertView;
        }

    }
}
