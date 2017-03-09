package rieger.alarmsmsapp.control.viewholder;

import android.app.TimePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.AlarmTimeCallback;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.util.AppConstants;

/**
 * ViewHolder for alarm times
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeViewHolder extends RecyclerView.ViewHolder implements TimePickerDialog.OnTimeSetListener{

    private final String LOG_TAG = getClass().getSimpleName();

    @Bind(R.id.list_item_alarm_time_from)
    EditText timeFrom;

    @Bind(R.id.list_item_alarm_time_to)
    EditText timeTo;

    @Bind(R.id.list_item_alarm_time_days)
    Spinner days;

    private View view;

    private String timeFiled;

    private AlarmTimeCallback callback;

    public AlarmTimeViewHolder(final View itemView, final AlarmTimeCallback callback) {
        super(itemView);

        this.callback = callback;

        view = itemView;

        ButterKnife.bind(this, itemView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                itemView.getContext(), R.array.days_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days.setAdapter(adapter);

        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(), AlarmTimeViewHolder.this, 0, 0, true);
                timePickerDialog.show();
                timeFiled = AppConstants.CallBacks.TIMEFIELED_FROM;
            }
        });

        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(), AlarmTimeViewHolder.this, 0, 0, true);
                timePickerDialog.show();
                timeFiled = AppConstants.CallBacks.TIMEFIELD_TO;
            }
        });

        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.alarmTimeCallBack(getAdapterPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public EditText getTimeFrom() {
        return timeFrom;
    }

    public EditText getTimeTo() {
        return timeTo;
    }

    public Spinner getDays() {
        return days;
    }

    public View getView() {
        return view;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        switch (timeFiled){
            case AppConstants.CallBacks.TIMEFIELED_FROM:
                if(getTimeTo().getText().toString().compareTo(AlarmTimeModel.timeToString(hourOfDay, minute)) == -1){
                    Toast.makeText(view.getContext(), R.string.start_befor_end_time, Toast.LENGTH_LONG).show();
                    break;
                }
                timeFrom.setText(AlarmTimeModel.timeToString(hourOfDay, minute));
                timeFrom.setError(null);
                timeFiled = null;
                callback.alarmTimeCallBack(AppConstants.CallBacks.TIMEFIELED_FROM, getAdapterPosition(), hourOfDay, minute, days.getSelectedItemPosition());
                break;
            case AppConstants.CallBacks.TIMEFIELD_TO:
                if(getTimeFrom().getText().toString().compareTo(AlarmTimeModel.timeToString(hourOfDay, minute)) == 1){
                    Toast.makeText(view.getContext(), R.string.end_befor_start_time, Toast.LENGTH_LONG).show();
                    break;
                }
                timeTo.setText(AlarmTimeModel.timeToString(hourOfDay, minute));
                timeTo.setError(null);
                timeFiled = null;
                callback.alarmTimeCallBack(AppConstants.CallBacks.TIMEFIELD_TO, getAdapterPosition(), hourOfDay, minute, days.getSelectedItemPosition());
                break;
            default:
                Log.e(LOG_TAG, "WRONG timeString filed Selected");
                break;
        }
    }
}
