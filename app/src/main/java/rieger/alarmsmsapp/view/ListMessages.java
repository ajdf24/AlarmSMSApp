package rieger.alarmsmsapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.adapter.MessageListAdapter;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;

public class ListMessages extends AppCompatActivity {

    private static final String LOG_TAG = ListMessages.class.getSimpleName();

    List<Message> messageList = new ArrayList<Message>();

    @Bind(R.id.activity_list_messages_list_view)
    RecyclerView messageListView;

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


        MessageListAdapter messageListAdapter = new MessageListAdapter(this, messageList, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageListView.setLayoutManager(linearLayoutManager);
        messageListView.setAdapter(messageListAdapter);


    }


}
