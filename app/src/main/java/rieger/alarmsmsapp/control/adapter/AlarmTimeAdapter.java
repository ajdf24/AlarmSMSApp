package rieger.alarmsmsapp.control.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.ActionCallback;
import rieger.alarmsmsapp.control.callback.AlarmTimeCallback;
import rieger.alarmsmsapp.control.viewholder.AlarmTimeViewHolder;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.util.AppConstants;

/**
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeAdapter extends RecyclerView.Adapter<AlarmTimeViewHolder> implements AlarmTimeCallback {

    private final String LOG_TAG = getClass().getSimpleName();

    private List<AlarmTimeModel> alarmTimes;

    private Activity activity;

    private ActionCallback callback;

    private boolean firstStart = true;

    public AlarmTimeAdapter(List<AlarmTimeModel> alarmTimes, Activity activity, ActionCallback callback) {
        this.alarmTimes = alarmTimes;
        this.activity = activity;
        this.callback = callback;
    }

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    @Override
    public AlarmTimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_alarm_time, parent, false);
        return new AlarmTimeViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(AlarmTimeViewHolder holder, int position) {
        setAnimation(holder.getView(), position);

        try {
            holder.getDays().setSelection(AlarmTimeModel.daysToInt(alarmTimes.get(position).getDay()));
            holder.getTimeFrom().setText(AlarmTimeModel.timeToString(alarmTimes.get(position).getStartTimeHours(), alarmTimes.get(position).getStartTimeMinutes()));
            holder.getTimeTo().setText(AlarmTimeModel.timeToString(alarmTimes.get(position).getEndTimeHours(), alarmTimes.get(position).getEndTimeMinutes()));
        }catch (NullPointerException e){
            FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "No alarm time information");
            FirebaseCrash.report(e);
            if (!firstStart) {
                if(holder.getTimeFrom().getText().length() == 0){
                    holder.getTimeFrom().setError("");
                }
                if(holder.getTimeTo().getText().length() == 0){
                    holder.getTimeTo().setError("");
                }
            }
        }
    }

    public void onRemoveItem(final int position){
        alarmTimes.remove(position);
        if(alarmTimes.size() == 0){
            callback.actionCallBack(AppConstants.CallBacks.REMOVE_TIME_CALLBACK);
            firstStart = true;
        }
        lastPosition--;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return alarmTimes.size();
    }

    private void setAnimation(final View viewToAnimate, int position)
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
                        .setStyle(R.style.CustomShowcaseTheme)
                        .setContentTitle(R.string.activity_alarm_time_settings_name)
                        .setContentText(R.string.showcase_rule_settings_alarm_times_text)
                        .hideOnTouchOutside()
                        .build();
                RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                int margin = ((Number) (viewToAnimate.getResources().getDisplayMetrics().density * 12)).intValue();
                lps.setMargins(margin, margin, margin, margin);
                showcaseView.setButtonPosition(lps);
                showcaseView.setOnShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
                        callback.actionCallBack(AppConstants.CallBacks.SHOW_SHOW_CASE_VIEW_NEW_ALARM_TIME);
                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                });

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

    @Override
    public void alarmTimeCallBack(String position, int listPosition, int hours, int minutes, int days) {
        switch (position){
            case AppConstants.CallBacks.TIMEFIELED_FROM:
                alarmTimes.get(listPosition).setStartTimeHours(hours);
                alarmTimes.get(listPosition).setStartTimeMinutes(minutes);
                break;
            case AppConstants.CallBacks.TIMEFIELD_TO:
                alarmTimes.get(listPosition).setEndTimeHours(hours);
                alarmTimes.get(listPosition).setEndTimeMinutes(minutes);
                break;
        }

        alarmTimes.get(listPosition).setDay(AlarmTimeModel.intToDays(days));
    }

    @Override
    public void alarmTimeCallBack(int listPosition, int days) {
        alarmTimes.get(listPosition).setDay(AlarmTimeModel.intToDays(days));
    }

    public List<AlarmTimeModel> getAlarmTimes() {
        return alarmTimes;
    }
}
