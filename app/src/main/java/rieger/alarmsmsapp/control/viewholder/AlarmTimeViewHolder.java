package rieger.alarmsmsapp.control.viewholder;

import android.app.TimePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
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

    public AlarmTimeViewHolder(final View itemView) {
        super(itemView);

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
            }
        });

        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(itemView.getContext(), AlarmTimeViewHolder.this, 0, 0, true);
                timePickerDialog.show();
            }
        });
    }

    public EditText getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(EditText timeFrom) {
        this.timeFrom = timeFrom;
    }

    public EditText getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(EditText timeTo) {
        this.timeTo = timeTo;
    }

    public Spinner getDays() {
        return days;
    }

    public void setDays(Spinner days) {
        this.days = days;
    }

    public View getView() {
        return view;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
