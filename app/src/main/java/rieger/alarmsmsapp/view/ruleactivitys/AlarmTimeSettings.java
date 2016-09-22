package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.ActionCallback;
import rieger.alarmsmsapp.control.adapter.AlarmTimeAdapter;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.viewholder.AlarmTimeViewHolder;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

public class AlarmTimeSettings extends AppCompatActivity implements ActionCallback {

    @Bind(R.id.activity_alarm_time_settings_switch)
    Switch isAlwaysAlarm;

    @Bind(R.id.activity_alarm_time_settings_cardview)
    RecyclerView alarmTimes;

    @Bind(R.id.activity_alarm_time_settings_save)
    FloatingActionButton save;

    @Bind(R.id.activity_alarm_time_settings_new_time)
    FloatingActionButton addNewEntry;

    List<AlarmTimeModel> alarmTimeList = new ArrayList<>();

    final AlarmTimeAdapter alarmTimeAdapter = new AlarmTimeAdapter(alarmTimeList, this, this);

    Rule rule;

    DataSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_settings);

        ButterKnife.bind(this);

        db = new DataSource(AlarmTimeSettings.this);

        rule = BundleHandler.getRuleFromBundle(this);

        if(db.getAlarmTimes(rule).size() != 0) {

            List<AlarmTimeModel> alarmTimeListSwap = db.getAlarmTimes(rule);

            for(int i = 0; i < db.getAlarmTimes(rule).size(); i++){
                alarmTimeList.add(alarmTimeListSwap.get(i));
            }
        }

//        isAlwaysAlarm.setChecked(rule.is);

        toggleListVisibility();

        isAlwaysAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleListVisibility();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alarmTimeAdapter.firstStart = false;
                alarmTimeAdapter.notifyDataSetChanged();

                if(alarmTimeAdapter.errors == 0) {
                    db.deleteAlarmTimesByRule(rule);

                    for (AlarmTimeModel alarmtime : alarmTimeList) {
                        db.saveAlarmTime(alarmtime, rule);
                    }

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
                    bundle.putInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER, 1);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setClass(AlarmTimeSettings.this, RuleSettings.class);
                    startActivity(intent);
                }
            }
        });

        addNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmTimeList.add(new AlarmTimeModel());
                alarmTimeAdapter.errors = alarmTimeAdapter.errors + 2;
                alarmTimeAdapter.firstStart = true;
                alarmTimeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void toggleListVisibility(){
        if(isAlwaysAlarm.isChecked()){
            alarmTimes.setVisibility(View.INVISIBLE);
            addNewEntry.setVisibility(View.INVISIBLE);
            Animation addButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_out);
            Animation alarmTimesAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            if(alarmTimes.getVisibility() == View.VISIBLE) {
                alarmTimes.startAnimation(alarmTimesAnimation);
            }
            if(addNewEntry.getVisibility() == View.VISIBLE){
                addNewEntry.startAnimation(addButtonAnimation);
            }
        }else {
            alarmTimes.setVisibility(View.VISIBLE);
            addNewEntry.setVisibility(View.VISIBLE);

            Animation addButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_in);
            Animation alarmTimesAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
            addNewEntry.startAnimation(addButtonAnimation);
            alarmTimes.startAnimation(alarmTimesAnimation);


            if(alarmTimeList.size() == 0) {
                alarmTimeList.add(new AlarmTimeModel());
                alarmTimeAdapter.errors = alarmTimeAdapter.errors + 2;
            }

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            alarmTimes.setLayoutManager(linearLayoutManager);

            alarmTimes.setAdapter(alarmTimeAdapter);



            ItemTouchHelper.SimpleCallback simpleItemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    alarmTimeAdapter.onRemoveItem(viewHolder.getAdapterPosition());
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchHelper);
            itemTouchHelper.attachToRecyclerView(alarmTimes);
        }
    }

    @Override
    public void actionCallBack(String identifier) {

        switch (identifier){
            case AppConstants.CallBacks.REMOVE_TIME_CALLBACK:
                isAlwaysAlarm.setChecked(true);
                toggleListVisibility();
                break;
            case AppConstants.CallBacks.SHOW_SHOW_CASE_VIEW_NEW_ALARM_TIME:
                ShowcaseView view = new ShowcaseView.Builder(this)
                        .setTarget(new ViewTarget(R.id.activity_alarm_time_settings_new_time, this))
                        .setContentTitle("")
                        .setStyle(R.style.CustomShowcaseTheme)
                        .setContentText("Erstelle eine neue Alarmzeit")
                        .hideOnTouchOutside()
                        .blockAllTouches()
                        .build();
                view.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));
                break;
        }
    }

    @Override
    public String toString() {
        return CreateContextForResource.getStringFromID(R.string.activity_alarm_time_settings_name);
    }
}
