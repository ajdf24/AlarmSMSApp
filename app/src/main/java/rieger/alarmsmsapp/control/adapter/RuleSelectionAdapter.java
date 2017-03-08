package rieger.alarmsmsapp.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.RuleSelected;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.listener.OpenRuleSettingsListener;
import rieger.alarmsmsapp.control.viewholder.RuleSelectionViewHolder;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 08.03.17.
 */

public class RuleSelectionAdapter extends RecyclerView.Adapter<RuleSelectionViewHolder> {

    View itemView;

    List<Rule> ruleList;

    OpenRuleSettingsListener openRuleSettingsListener;

    RuleSelected callback;

    public RuleSelectionAdapter(RuleSelected callback, List<Rule> ruleList, OpenRuleSettingsListener openRuleSettingsListener) {
        this.ruleList = ruleList;
        this.callback = callback;
        this.openRuleSettingsListener = openRuleSettingsListener;
    }

    @Override
    public RuleSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rule_selection, parent, false);
        itemView.setOnClickListener(openRuleSettingsListener);

        return new RuleSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RuleSelectionViewHolder holder, int position) {
        final Rule currentRule = ruleList.get(position);

        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            callback.selectedRule(currentRule);

            menu.setHeaderTitle(CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_title));
            menu.add(0, itemView.getId(), 0, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_action_edit));
            menu.add(0, itemView.getId(), 0, CreateContextForResource.getStringFromID(R.string.test_rule));
            menu.add(0, itemView.getId(), 0, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_action_send));
            menu.add(0, itemView.getId(), 0, CreateContextForResource.getStringFromID(R.string.activity_rule_selection_context_menu_action_delete));
            }
        });

        System.out.println(currentRule.getRuleName());

        holder.getActive().setChecked(currentRule.isActive());

        holder.getActive().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentRule.setActive(isChecked);
                currentRule.notifyObserver();
            }
        });

        holder.getRuleName().setText(currentRule.getRuleName());

        if (currentRule.isAlarmEveryTime()) {
            holder.getTimeControlled().setVisibility(View.INVISIBLE);
        } else {

            String timeControlled = CreateContextForResource.getStringFromID(R.string.activity_rule_selection_time_controlled) + " ";

            DataSource db = new DataSource(itemView.getContext());

            Set<AlarmTimeModel.Days> daysSet = new HashSet<>();

            for (AlarmTimeModel alarmTime : db.getAlarmTimes(currentRule)) {
                if (!daysSet.contains(alarmTime.getDay())) {
                    timeControlled = timeControlled + AlarmTimeModel.getStringForDay(alarmTime.getDay()) + "; ";
                    daysSet.add(alarmTime.getDay());
                }
            }

            if (timeControlled.length() > 40) {
                timeControlled = timeControlled.substring(0, 40);
                timeControlled = timeControlled + "...";
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            holder.getTimeControlled().setText(timeControlled);
        }

    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

}
