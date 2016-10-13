package rieger.alarmsmsapp.control.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.viewholder.AlarmTimeViewHolder;
import rieger.alarmsmsapp.control.viewholder.SoundSelectionViewHolder;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.Sound;

/**
 * Created by sebastian on 13.10.16.
 */

public class SoundSelectionAdapter extends RecyclerView.Adapter<SoundSelectionViewHolder>{

    private final String LOG_TAG = getClass().getSimpleName();

    List<Sound> soundList;

    Rule rule;

    public SoundSelectionAdapter(List<Sound> soundList, Rule rule) {
        this.soundList = soundList;
        this.rule = rule;
    }

    @Override
    public SoundSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sound, parent, false);
        return new SoundSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SoundSelectionViewHolder holder, int position) {
        holder.getSoundName().setText(soundList.get(position).getName());
        if(rule.getAlarmSound().equals(soundList.get(position))){
            holder.getIsActive().setVisibility(View.VISIBLE);
        }else {
            holder.getIsActive().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public void onSelectItem(){
    }
}
