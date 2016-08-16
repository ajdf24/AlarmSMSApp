package rieger.alarmsmsapp.view.ruleactivitys;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.adapter.AlarmTimeAdapter;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;

public class AlarmTimeSettings extends AppCompatActivity {

    @Bind(R.id.activity_alarm_time_settings_switch)
    Switch isAlwaysAlarm;

    @Bind(R.id.activity_alarm_time_settings_cardview)
    RecyclerView alarmTimes;

    @Bind(R.id.fragment_happy_hours_new_happy_hour)
    FloatingActionButton fab;

    List<AlarmTimeModel> alarmTimeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_time_settings);

        ButterKnife.bind(this);

        isAlwaysAlarm.setChecked(true);

        toggleListVisibility();

        isAlwaysAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleListVisibility();
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                location.getHappyHours().add(new HappyHour());
//                alarmTimeAdapter.notifyDataSetChanged();
//                alarmTimes.scrollToPosition(alarmTimeAdapter.getItemCount() - 1);
            }
        });
    }

    private void toggleListVisibility(){
        if(isAlwaysAlarm.isChecked()){
            alarmTimes.setVisibility(View.INVISIBLE);
        }else {
            alarmTimes.setVisibility(View.VISIBLE);
            alarmTimeList.add(new AlarmTimeModel());

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            alarmTimes.setLayoutManager(linearLayoutManager);

            final AlarmTimeAdapter alarmTimeAdapter = new AlarmTimeAdapter(alarmTimeList, this);
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
}
