package rieger.alarmsmsapp.control.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.rules.Sound;
import rieger.alarmsmsapp.model.rules.AnswerBundle;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.view.AlarmSettings;

/**
 * Data source  for the all local app data.
 * Created by sebastian on 14.08.16.
 */
public class DataSource {

    private final String LOG_TAG = getClass().getSimpleName();

    private SQLiteDatabase database;
    private DatabaseHelper helper;

    private String[] allColumnsRule = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_RULE_NAME,
            DatabaseHelper.COLUMN_SENDER,
            DatabaseHelper.COLUMN_OCCURRED_WORDS,
            DatabaseHelper.COLUMN_NOT_OCCURRED_WORDS,
            DatabaseHelper.COLUMN_SOUND_NAME,
            DatabaseHelper.COLUMN_SOUND_ID,
            DatabaseHelper.COLUMN_INTERNAL_SOUND,
            DatabaseHelper.COLUMN_ANSWER_RECEIVER,
            DatabaseHelper.COLUMN_ANSWER_MESSAGE,
            DatabaseHelper.COLUMN_ANSWER_DISTANCE,
            DatabaseHelper.COLUMN_TWITTER_MESSAGE,
            DatabaseHelper.COLUMN_NAVIGATION_TARGET,
            DatabaseHelper.COLUMN_READ_THIS_MESSAGE,
            DatabaseHelper.COLUMN_READ_OTHER_MESSAGES,
            DatabaseHelper.COLUMN_ADD_MESSAGE_TO_TWITTER,
            DatabaseHelper.COLUMN_ACTIVATE_LIGHT,
            DatabaseHelper.COLUMN_ACTIVATE_LIGHT_ONLY_WHEN_DARK,
            DatabaseHelper.COLUMN_LIGHT_TIME
    };

    private String[] allColumnsDepartment = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS
    };

    private String[] allColumnsAlarm = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_ALARM_ACTIVE,
            DatabaseHelper.COLUMN_ALARM_VOLUME,
            DatabaseHelper.COLUMN_VIBRATION_ACTIVE,
            DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_ACTIVE,
            DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_COLOR,
            DatabaseHelper.COLUMN_MUTE_ALARM,
            DatabaseHelper.COLUMN_REPEAT_NUMBER
    };

    /**
     * constructor
     * @param context the context under which the db should be opened
     */
    public DataSource(Context context) {
        helper = new DatabaseHelper(context);
        helper.onUpgrade(helper.getWritableDatabase(),0,0);
    }

    /**
     * open a db connection
     * @throws SQLException if a error occurs
     */
    private void open() throws SQLException {
        database = helper.getWritableDatabase();
    }

    /**
     * close the db connection
     */
    private void close() {
        helper.close();
    }

    /**
     * create a new database entry for facebook login
     * @param rule the data for the entry
     * @return the entry which was saved in the database
     */
    public Rule createRule(Rule rule) {
        open();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_RULE_NAME, rule.getRuleName());
        values.put(DatabaseHelper.COLUMN_SENDER, rule.getSender());
        values.put(DatabaseHelper.COLUMN_OCCURRED_WORDS, rule.getOccurredWords());
        values.put(DatabaseHelper.COLUMN_NOT_OCCURRED_WORDS, rule.getNotOccurredWords());
        if(rule.getAlarmSound() != null) {
            values.put(DatabaseHelper.COLUMN_SOUND_NAME, rule.getAlarmSound().getName());
            values.put(DatabaseHelper.COLUMN_SOUND_ID, rule.getAlarmSound().getIdForSound());
            values.put(DatabaseHelper.COLUMN_INTERNAL_SOUND, (rule.getAlarmSound().isInternalSound()) ? 1 : 0);
        }else{
            values.put(DatabaseHelper.COLUMN_SOUND_NAME, "");
            values.put(DatabaseHelper.COLUMN_SOUND_ID, "");
            values.put(DatabaseHelper.COLUMN_INTERNAL_SOUND, 0);
        }
        if(rule.getAutomaticallyAnswer() != null) {
            values.put(DatabaseHelper.COLUMN_ANSWER_RECEIVER, rule.getAutomaticallyAnswer().getReceiver());
            values.put(DatabaseHelper.COLUMN_ANSWER_MESSAGE, rule.getAutomaticallyAnswer().getMessage());
            values.put(DatabaseHelper.COLUMN_ANSWER_DISTANCE, rule.getAutomaticallyAnswer().getDistance());
        }
        values.put(DatabaseHelper.COLUMN_TWITTER_MESSAGE, rule.getMessageToPostOnTwitter());
        values.put(DatabaseHelper.COLUMN_NAVIGATION_TARGET, rule.getNavigationTarget());
        values.put(DatabaseHelper.COLUMN_READ_THIS_MESSAGE, (rule.isReadThisMessage())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_READ_OTHER_MESSAGES, (rule.isReadOtherMessages())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ADD_MESSAGE_TO_TWITTER, (rule.isAddMessageToTwitterPost())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ACTIVATE_LIGHT, (rule.isActivateLight())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ACTIVATE_LIGHT_ONLY_WHEN_DARK, (rule.isActivateLightOnlyWhenDark())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_LIGHT_TIME, rule.getLightTime());
        long insertId = database.insert(DatabaseHelper.TABLE_RULES, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_RULES,
                allColumnsRule, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Rule newRule = cursorToRule(cursor);
        cursor.close();
        close();
        return newRule;
    }

    public void deleteRule(Rule rule){
        open();
        database.delete(helper.TABLE_RULES, helper.COLUMN_RULE_NAME
                + " = ?" , new String[]{rule.getRuleName()});
        close();
    }

    public List<Rule> getAllRules(){
        List<Rule> rules = new ArrayList<>();

        open();
        Cursor cursor = database.query(helper.TABLE_RULES, allColumnsRule, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Rule rule = cursorToRule(cursor);
            rules.add(rule);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return rules;
    }

    public DepartmentSettingsModel createDepartment(DepartmentSettingsModel departmentSettings){
        open();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS, departmentSettings.getAddress());

        long insertId = database.insert(DatabaseHelper.TABLE_DEPARTMENT_SETTINGS, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTMENT_SETTINGS,
                allColumnsDepartment, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DepartmentSettingsModel department = cursorToDepartment(cursor);
        cursor.close();
        close();
        return department;
    }

    public List<DepartmentSettingsModel> getAllDepartments(){
        List<DepartmentSettingsModel> departments = new ArrayList<>();

        open();
        Cursor cursor = database.query(helper.TABLE_DEPARTMENT_SETTINGS, allColumnsDepartment, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DepartmentSettingsModel department = cursorToDepartment(cursor);
            departments.add(department);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return departments;
    }

    public void deleteDepartment(DepartmentSettingsModel department){
        open();
        database.delete(helper.TABLE_DEPARTMENT_SETTINGS, helper.COLUMN_DEPARTMENT_ADDRESS
                + " = ?" , new String[]{department.getAddress()});
        close();
    }

    public AlarmSettingsModel createAlarm(AlarmSettingsModel alarm){
        open();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_ALARM_ACTIVE, alarm.isAlarmActivated());
        values.put(DatabaseHelper.COLUMN_ALARM_VOLUME, alarm.getAlarmVolume());
        values.put(DatabaseHelper.COLUMN_VIBRATION_ACTIVE, alarm.isVibrationActivated());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_ACTIVE, alarm.isNotificationLightActivated());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_COLOR, alarm.getNotificationLightColor());
        values.put(DatabaseHelper.COLUMN_MUTE_ALARM, alarm.isMuteAlarmActivated());
        values.put(DatabaseHelper.COLUMN_REPEAT_NUMBER, alarm.getRepeatAlarm());

        long insertId = database.insert(DatabaseHelper.TABLE_ALARM_SETTINGS, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARM_SETTINGS,
                allColumnsAlarm, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AlarmSettingsModel newAlarm = cursorToAlarm(cursor);
        cursor.close();
        close();
        return newAlarm;
    }

    public AlarmSettingsModel getAlarm(){

        open();
        Cursor cursor = database.query(helper.TABLE_ALARM_SETTINGS, allColumnsAlarm, null, null, null, null, null);

        cursor.moveToFirst();
        AlarmSettingsModel alarm = cursorToAlarm(cursor);

        cursor.close();
        close();
        return alarm;
    }

    public void deleteAlarm(){
        open();
        database.delete(helper.TABLE_ALARM_SETTINGS, null , null);
        close();
    }

    public Rule cursorToRule(Cursor cursor){
        Rule rule = new SMSRule();

        rule.setRuleName(cursor.getString(1));
        rule.setSender(cursor.getString(2));
        rule.setOccurredWords(cursor.getString(3));
        rule.setNotOccurredWords(cursor.getString(4));
        if(cursor.getString(5) != ""){
            rule.setAlarmSound(new Sound(cursor.getString(5), cursor.getString(6), (cursor.getInt(7) == 1)));
        }
        if(cursor.getString(8) != ""){
            rule.setAutomaticallyAnswer(new AnswerBundle(cursor.getString(8), cursor.getString(9), cursor.getInt(10)));
        }
        rule.setMessageToPostOnTwitter(cursor.getString(11));
        rule.setNavigationTarget(cursor.getString(12));
        rule.setReadThisMessage(cursor.getInt(13) == 1);
        rule.setReadOtherMessages(cursor.getInt(14) == 1);
        rule.setAddMessageToTwitterPost(cursor.getInt(15) == 1);
        rule.setActivateLight(cursor.getInt(16) == 1);
        rule.setActivateLightOnlyWhenDark(cursor.getInt(17) == 1);
        rule.setLightTime(cursor.getInt(18));

        return rule;
    }

    public DepartmentSettingsModel cursorToDepartment(Cursor cursor){
        DepartmentSettingsModel department = new DepartmentSettingsModel();

        department.setAddress(cursor.getString(1));

        return department;
    }

    public AlarmSettingsModel cursorToAlarm(Cursor cursor){
        AlarmSettingsModel alarmSettingsModel = new AlarmSettingsModel();

        try {
            alarmSettingsModel.setAlarmActivated((cursor.getInt(1) == 1));
            alarmSettingsModel.setAlarmVolume(cursor.getInt(2));
            alarmSettingsModel.setVibrationActivated((cursor.getInt(3) == 1));
            alarmSettingsModel.setNotificationLightColor(cursor.getInt(4));
            alarmSettingsModel.setMuteAlarmActivated((cursor.getInt(5) == 1));
            alarmSettingsModel.setRepeatAlarm(cursor.getInt(6));
        }catch (IndexOutOfBoundsException e){
            return null;
        }

        return  alarmSettingsModel;
    }
}
