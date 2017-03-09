package rieger.alarmsmsapp.control.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;

/**
 * Listener for recycler view which shows {@link Rule}
 * Created by sebastian on 08.03.17.
 */

public class OpenRuleSettingsListener implements View.OnClickListener {

    private RecyclerView recyclerView;

    private List<Rule> ruleList;

    private Activity activity;

    public OpenRuleSettingsListener(Activity activity, RecyclerView recyclerView, List<Rule> ruleList) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.ruleList = ruleList;
    }

    @Override
    public void onClick(View view) {

        int itemPosition = recyclerView.getChildLayoutPosition(view);
        Rule rule = ruleList.get(itemPosition);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
        intent.putExtras(bundle);
        intent.setClass(activity, RuleSettings.class);
        activity.startActivity(intent);

    }
}
