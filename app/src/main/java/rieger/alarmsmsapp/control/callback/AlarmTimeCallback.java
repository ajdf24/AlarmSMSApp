package rieger.alarmsmsapp.control.callback;

/**
 * Created by sebastian on 17.08.16.
 */
public interface AlarmTimeCallback {

    void alarmTimeCallBack(String position, int listPosition, int hours, int minutes, int days);

    void alarmTimeCallBack(int listPosition, int days);
}
