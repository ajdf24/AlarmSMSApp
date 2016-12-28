package rieger.alarmsmsapp.control.listener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.MainActivity;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;

/**
 * Created by sebastian on 16.11.16.
 */

public class CreateRuleFromSMSClickListener implements View.OnClickListener {

    private RecyclerView recyclerView;

    private List<Message> messageList;

    private Rule rule;


    public CreateRuleFromSMSClickListener(RecyclerView recyclerView, List<Message> messageList, Rule rule) {
        this.recyclerView = recyclerView;
        this.messageList = messageList;
        this.rule = rule;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = recyclerView.getChildLayoutPosition(view);
        final Message message = messageList.get(itemPosition);

        AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
        builder.setTitle(R.string.dialog_create_alarm_title);
        builder.setMessage(String.format(view.getResources().getString(R.string.dialog_create_alarm_text), message.getSender()));


        builder.setPositiveButton(R.string.general_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                rule.setSender(message.getSender());
                rule.notifyObserver();

                Intent intent = new Intent().setClass(recyclerView.getContext(), RuleSettings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
                bundle.putInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER, 2);
                bundle.putBoolean(AppConstants.BUNDLE_SETTINGS_SHOW_TRIGGER_ALREADY_SET, true);
                intent.putExtras(bundle);
                recyclerView.getContext().startActivity(intent);

                ((Activity) recyclerView.getContext()).finish();
            }
        });

        builder.setNegativeButton(R.string.general_string_button_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recyclerView.getContext().startActivity(new Intent().setClass(recyclerView.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(view.getContext().getResources().getColor(R.color.my_accent));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(view.getContext().getResources().getColor(R.color.my_accent));
    }
}
