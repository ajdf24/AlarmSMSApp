package rieger.alarmsmsapp.control.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.viewholder.AlarmTimeViewHolder;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.util.AppConstants;

/**
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeAdapter extends RecyclerView.Adapter<AlarmTimeViewHolder> {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<AlarmTimeModel> alarmTimes;

    private Activity activity;

    public AlarmTimeAdapter(List<AlarmTimeModel> alarmTimes, Activity activity) {
        this.alarmTimes = alarmTimes;
        this.activity = activity;
    }

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    @Override
    public AlarmTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alarm_time, parent, false);
        return new AlarmTimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmTimeViewHolder holder, int position) {
        setAnimation(holder.getView(), position);
    }

    public void onRemoveItem(final int position){
        alarmTimes.remove(position);
    }

    @Override
    public int getItemCount() {
        return alarmTimes.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(viewToAnimate.getContext());
            if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_ALARM_TIME_LIST, true)){
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_ALARM_TIME_LIST, false);
                editor.apply();

                ShowcaseView showcaseView = new ShowcaseView.Builder(activity)
                        .setTarget(new ViewTarget(viewToAnimate.findViewById(R.id.list_item_alarm_time_cardview)))
                        .setContentTitle("Alarmzeiten")
                        .setStyle(com.github.amlcurran.showcaseview.R.style.TextAppearance_ShowcaseView_Detail_Light)
                        .setContentText("Setze zeiten, wann deine Regel einen Alarm auslösen soll. Wische nach links oder rechts, um die Zeit zu löschen.")
                        .hideOnTouchOutside()
                        .blockAllTouches()
                        .build();

                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.shake);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }else {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }

        }
    }
}
