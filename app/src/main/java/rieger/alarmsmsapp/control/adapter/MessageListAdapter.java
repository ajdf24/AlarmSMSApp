package rieger.alarmsmsapp.control.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.listener.CreateRuleFromSMSClickListener;
import rieger.alarmsmsapp.control.viewholder.MessageViewHolder;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.ContactsWorker;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 16.11.16.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private View itemView;

    private Activity activity;

    private List<Message> messageList;

    private CreateRuleFromSMSClickListener listener;

    public MessageListAdapter(Activity activity, List<Message> messageList, CreateRuleFromSMSClickListener listener) {
        this.activity = activity;
        this.messageList = messageList;
        this.listener = listener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message, parent, false);
        itemView.setOnClickListener(listener);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder viewHolder, int position) {
        viewHolder.getMessageBody().setText(messageList.get(position).getMessage());
        viewHolder.getMessageDate().setText(
                new SimpleDateFormat(
                        CreateContextForResource.getStringFromID(
                                R.string.general_string_date_format)).format(
                        messageList.get(position).getTimeStamp()));

        String contactName = null;

        if (ActivityCompat.checkSelfPermission(itemView.getContext(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
        }else{
            contactName = ContactsWorker.getContactName(itemView.getContext(), messageList.get(position).getSender());
        }

        if(contactName != null){
            viewHolder.getMessageSender().setText(contactName);
        }else {
            viewHolder.getMessageSender().setText(messageList.get(position).getSender());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
