package rieger.alarmsmsapp.control.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
 * Created by sebastian on 16.11.16.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.list_item_message_body)
    TextView messageBody;

    @Bind(R.id.list_item_message_date)
    TextView messageDate;

    @Bind(R.id.list_item_message_sender)
    TextView messageSender;


    public MessageViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public TextView getMessageBody() {
        return messageBody;
    }

    public TextView getMessageDate() {
        return messageDate;
    }

    public TextView getMessageSender() {
        return messageSender;
    }
}
