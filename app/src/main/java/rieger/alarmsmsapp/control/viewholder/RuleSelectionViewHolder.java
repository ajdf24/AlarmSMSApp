package rieger.alarmsmsapp.control.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
 * ViewHolder for rule selection
 * Created by sebastian on 08.03.17.
 */

public class RuleSelectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.list_item_rule_name)
    TextView ruleName;

    @Bind(R.id.list_item_rule_time_controlled)
    TextView timeControlled;

    @Bind(R.id.list_item_is_active)
    CheckBox active;

    public RuleSelectionViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public TextView getRuleName() {
        return ruleName;
    }

    public TextView getTimeControlled() {
        return timeControlled;
    }

    public CheckBox getActive() {
        return active;
    }

}
