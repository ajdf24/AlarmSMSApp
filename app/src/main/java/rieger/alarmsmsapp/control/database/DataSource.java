package rieger.alarmsmsapp.control.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
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

    private boolean innerDelete = false;

    private boolean isConnected = false;

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
            DatabaseHelper.COLUMN_LIGHT_TIME,
            DatabaseHelper.COLUMN_RULE_ACTIVE,
            DatabaseHelper.COLUMN_ALARM_EVERY_TIME
    };

    private String[] allColumnsDepartment = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS
    };

    private String[] allColumnsMessageReceiver = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_RULE_FOREIGN_KEY,
            DatabaseHelper.COLUMN_RECEIVER
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

    private String[] allColumnsMessage = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_SENDER_MESSAGE,
            DatabaseHelper.COLUMN_MESSAGE,
            DatabaseHelper.COLUMN_TIME_STAMP,
            DatabaseHelper.COLUMN_DAY,
            DatabaseHelper.COLUMN_MONTH,
            DatabaseHelper.COLUMN_YEAR,
            DatabaseHelper.COLUMN_MATCHING_RULE_NAME,
            DatabaseHelper.COLUMN_DAY_NAME
    };

    private String[] allCollumnsAllarmTimes = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_RULE_FOREIGN_KEY,
            DatabaseHelper.COLUMN_DAYS,
            DatabaseHelper.COLUMN_START_TIME_MINUTES,
            DatabaseHelper.COLUMN_START_TIME_HOURS,
            DatabaseHelper.COULMN_END_TIME_MINUTES,
            DatabaseHelper.COULMN_END_TIME_HOURS
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
        isConnected = true;
        database = helper.getWritableDatabase();
    }

    /**
     * close the db connection
     */
    private void close() {
        helper.close();
        isConnected = false;
    }

    /**
     * create a new database entry for facebook login
     * @param rule the data for the entry
     * @return the entry which was saved in the database
     */
    public Rule saveRule(Rule rule) {
        ContentValues values = new ContentValues();

        int ruleID = getRuleId(rule);

//        deleteRule(rule);

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
//            values.put(DatabaseHelper.COLUMN_ANSWER_RECEIVER, rule.getAutomaticallyAnswer().getReceiver());
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
        values.put(DatabaseHelper.COLUMN_RULE_ACTIVE, (rule.isActive())? 1 : 0);
        values.put(DatabaseHelper.COLUMN_ALARM_EVERY_TIME, (rule.isAlarmEveryTime())? 1 : 0);
        if(!isConnected) {
            open();
        }
        Rule newRule = null;
        if(ruleID == -1) {
            long insertId = database.insert(DatabaseHelper.TABLE_RULES, "",
                    values);

            Cursor cursor = database.query(DatabaseHelper.TABLE_RULES,
                    allColumnsRule, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newRule = cursorToRule(cursor);
            cursor.close();
            close();
        }else {
            database.update(DatabaseHelper.TABLE_RULES, values, DatabaseHelper.COLUMN_ID + "=" + ruleID, null);

            Cursor cursor = database.query(DatabaseHelper.TABLE_RULES,
                    allColumnsRule, DatabaseHelper.COLUMN_ID + " = " + ruleID, null,
                    null, null, null);
            cursor.moveToFirst();
            newRule = cursorToRule(cursor);
            cursor.close();
            close();
        }

        if(!isConnected){
            open();
        }

        database.delete(DatabaseHelper.TABLE_MESSAGE_RECEIVER_FOR_RULE, DatabaseHelper.COLUMN_RULE_FOREIGN_KEY + " = ?", new String[]{getRuleId(rule) + ""});

        for (String receiver : rule.getAutomaticallyAnswer().getReceivers()){
            ContentValues receiverValues = new ContentValues();
            receiverValues.put(DatabaseHelper.COLUMN_RULE_FOREIGN_KEY, getRuleId(rule));
            receiverValues.put(DatabaseHelper.COLUMN_RECEIVER, receiver);
            database.insert(DatabaseHelper.TABLE_MESSAGE_RECEIVER_FOR_RULE, "",
                    receiverValues);
        }




        Cursor cursor1 = database.query(DatabaseHelper.TABLE_MESSAGE_RECEIVER_FOR_RULE, allColumnsMessageReceiver, DatabaseHelper.COLUMN_RULE_FOREIGN_KEY + " = ?" , new String[]{getRuleId(newRule) + ""}, null, null, null, null);
        cursor1.moveToFirst();
        while (!cursor1.isAfterLast()){
            newRule.getAutomaticallyAnswer().addReceiver(cursorToRuleForReceiver(cursor1));
            cursor1.moveToNext();
        }
        cursor1.close();

        if(isConnected){
            close();
        }
        return newRule;
    }

    public void deleteRule(Rule rule){

        deleteAlarmTimesByRule(rule);

        if(!isConnected) {
            open();
        }

        database.delete(helper.TABLE_RULES, helper.COLUMN_RULE_NAME
                + " = ?" , new String[]{rule.getRuleName()});

        if(isConnected) {
            close();
        }
    }

    public List<Rule> getAllRules(){
        List<Rule> rules = new ArrayList<>();

        if(!isConnected) {
            open();
        }
        Cursor cursor = database.query(DatabaseHelper.TABLE_RULES, allColumnsRule, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Rule rule = cursorToRule(cursor);
            Cursor cursor1 = database.query(DatabaseHelper.TABLE_MESSAGE_RECEIVER_FOR_RULE, allColumnsMessageReceiver, DatabaseHelper.COLUMN_RULE_FOREIGN_KEY + " = ? ", new String[]{getRuleId(rule) + ""}, null, null, null, null);
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()){
                rule.getAutomaticallyAnswer().addReceiver(cursorToRuleForReceiver(cursor1));
                cursor1.moveToNext();
            }
            cursor1.close();
            rules.add(rule);
            cursor.moveToNext();
        }
        cursor.close();

        if(isConnected) {
            close();
        }

        return rules;
    }

    public DepartmentSettingsModel saveDepartment(DepartmentSettingsModel departmentSettings){


        deleteDepartment(departmentSettings);

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_DEPARTMENT_ADDRESS, departmentSettings.getAddress());

        if(!isConnected) {
            open();
        }

        long insertId = database.insert(DatabaseHelper.TABLE_DEPARTMENT_SETTINGS, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTMENT_SETTINGS,
                allColumnsDepartment, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DepartmentSettingsModel department = cursorToDepartment(cursor);
        cursor.close();

        if(isConnected) {
            close();
        }

        return department;
    }

    public List<DepartmentSettingsModel> getAllDepartments(){
        List<DepartmentSettingsModel> departments = new ArrayList<>();

        if(!isConnected) {
            open();
        }
        Cursor cursor = database.query(helper.TABLE_DEPARTMENT_SETTINGS, allColumnsDepartment, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DepartmentSettingsModel department = cursorToDepartment(cursor);
            departments.add(department);
            cursor.moveToNext();
        }
        cursor.close();

        if(isConnected) {
            close();
        }

        return departments;
    }

    public void deleteDepartment(DepartmentSettingsModel department){
        if(!isConnected) {
            open();
        }
        database.delete(helper.TABLE_DEPARTMENT_SETTINGS, helper.COLUMN_DEPARTMENT_ADDRESS
                + " = ?" , new String[]{department.getAddress()});
        if(isConnected) {
            close();
        }
    }

    public AlarmSettingsModel saveAlarmSetting(AlarmSettingsModel alarm){


        deleteAlarm();

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_ALARM_ACTIVE, alarm.isAlarmActivated());
        values.put(DatabaseHelper.COLUMN_ALARM_VOLUME, alarm.getAlarmVolume());
        values.put(DatabaseHelper.COLUMN_VIBRATION_ACTIVE, alarm.isVibrationActivated());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_ACTIVE, alarm.isNotificationLightActivated());
        values.put(DatabaseHelper.COLUMN_NOTIFICATION_LIGHT_COLOR, alarm.getNotificationLightColor());
        values.put(DatabaseHelper.COLUMN_MUTE_ALARM, alarm.isMuteAlarmActivated());
        values.put(DatabaseHelper.COLUMN_REPEAT_NUMBER, alarm.getRepeatAlarm());
        if(!isConnected) {
            open();
        }
        long insertId = database.insert(DatabaseHelper.TABLE_ALARM_SETTINGS, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARM_SETTINGS,
                allColumnsAlarm, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AlarmSettingsModel newAlarm = cursorToAlarm(cursor);
        cursor.close();
        if(isConnected) {
            close();
        }
        return newAlarm;
    }

    public Message saveMessage(Message message){
        if(!isConnected) {
            open();
        }

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_SENDER_MESSAGE, message.getSender());
        values.put(DatabaseHelper.COLUMN_MESSAGE, message.getMessage());
        values.put(DatabaseHelper.COLUMN_TIME_STAMP, message.getTimeStamp());
        values.put(DatabaseHelper.COLUMN_DAY, message.getDay());
        values.put(DatabaseHelper.COLUMN_MONTH, message.getMonth());
        values.put(DatabaseHelper.COLUMN_YEAR, message.getYear());
        values.put(DatabaseHelper.COLUMN_MATCHING_RULE_NAME, message.getMatchingRuleName());
        values.put(DatabaseHelper.COLUMN_DAY_NAME, message.getDayName());

        long insertId = database.insert(DatabaseHelper.TABLE_MESSAGES, "",
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_MESSAGES,
                allColumnsMessage, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Message newMessage = cursorToMessage(cursor);
        cursor.close();
        if(isConnected) {
            close();
        }
        return newMessage;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();

        if(!isConnected) {
            open();
        }
        Cursor cursor = database.query(helper.TABLE_MESSAGES, allColumnsMessage, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        if(isConnected) {
            close();
        }
        return messages;
    }

    public List<Message> getAllMessagesForYear(int year){
        List<Message> messages = new ArrayList<>();

        if(!isConnected) {
            open();
        }
        Cursor cursor = database.query(helper.TABLE_MESSAGES, allColumnsMessage, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            if(year == message.getYear()) {
                messages.add(message);
            }
            cursor.moveToNext();
        }
        cursor.close();
        if(isConnected) {
            close();
        }
        return messages;
    }

    public AlarmSettingsModel getAlarm(){

        if(!isConnected) {
            open();
        }
        Cursor cursor = database.query(helper.TABLE_ALARM_SETTINGS, allColumnsAlarm, null, null, null, null, null);

        cursor.moveToFirst();
        AlarmSettingsModel alarm = cursorToAlarm(cursor);

        cursor.close();
        if(isConnected) {
            close();
        }
        return alarm;
    }

    public void deleteAlarm(){
        if(!isConnected) {
            open();
        }
        database.delete(helper.TABLE_ALARM_SETTINGS, null , null);
        if(isConnected) {
            close();
        }
    }

    public List<AlarmTimeModel> getAlarmTimes(Rule rule){
        List<AlarmTimeModel> alarmTimes = new ArrayList<>();

        if(!isConnected) {
            open();
        }

        Cursor cursor = database.query(helper.TABLE_ALARM_TIMES, allCollumnsAllarmTimes, DatabaseHelper.COLUMN_RULE_FOREIGN_KEY + " = " + getRuleId(rule), null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            alarmTimes.add(cursorToAlarmTime(cursor));
            cursor.moveToNext();
        }

        cursor.close();

        if(isConnected) {
            close();
        }

        return alarmTimes;
    }

    public AlarmTimeModel saveAlarmTime(AlarmTimeModel alarmTime, Rule rule){
        ContentValues values = new ContentValues();

        if(!isConnected) {
            open();
        }

        values.put(DatabaseHelper.COLUMN_RULE_FOREIGN_KEY, getRuleId(rule));
        values.put(DatabaseHelper.COLUMN_DAYS, AlarmTimeModel.daysToInt(alarmTime.getDay()));
        values.put(DatabaseHelper.COLUMN_START_TIME_MINUTES, alarmTime.getStartTimeMinutes());
        values.put(DatabaseHelper.COLUMN_START_TIME_HOURS, alarmTime.getStartTimeHours());
        values.put(DatabaseHelper.COULMN_END_TIME_MINUTES, alarmTime.getEndTimeMinutes());
        values.put(DatabaseHelper.COULMN_END_TIME_HOURS, alarmTime.getEndTimeHours());
        long insertId = database.insert(DatabaseHelper.TABLE_ALARM_TIMES, "",
                values);

        Cursor cursor = database.query(DatabaseHelper.TABLE_ALARM_TIMES, allCollumnsAllarmTimes, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AlarmTimeModel newAlarmTime = cursorToAlarmTime(cursor);
        if(isConnected) {
            close();
        }
        return newAlarmTime;
    }

    public void deleteAlarmTime(AlarmTimeModel alarmTime){
        if(!isConnected) {
            open();
        }
        database.delete(DatabaseHelper.TABLE_ALARM_TIMES, DatabaseHelper.COLUMN_ID + " = ?", new String[]{Integer.toString(alarmTime.getId())});
        if(isConnected) {
            close();
        }
    }

    public void deleteAlarmTimesByRule(Rule rule){
        if(!isConnected) {
            open();
        }
        database.delete(DatabaseHelper.TABLE_ALARM_TIMES, DatabaseHelper.COLUMN_RULE_FOREIGN_KEY + " = ?", new String[]{getRuleId(rule) + ""});
        if(isConnected) {
            close();
        }
    }

    public int getRuleId(Rule rule){
        if(!isConnected) {
            open();
        }
        Cursor cursorRule = database.query(helper.TABLE_RULES, new String[]{helper.COLUMN_ID}, helper.COLUMN_RULE_NAME + " = ? ", new String[]{rule.getRuleName()}, null, null, null);

        cursorRule.moveToFirst();
        int ruleID = cursorToRuleID(cursorRule);
        cursorRule.close();

        return ruleID;
    }

    public Message cursorToMessage(Cursor cursor){
        Message message = new Message();

        message.setSender(cursor.getString(1));
        message.setMessage(cursor.getString(2));
        message.setTimeStamp(cursor.getLong(3));
        message.setDay(cursor.getInt(4));
        message.setMonth(cursor.getInt(5));
        message.setYear(cursor.getInt(6));
        message.setMatchingRuleName(cursor.getString(7));
        message.setDayName(cursor.getString(8));

        return message;
    }

    public String cursorToRuleForReceiver(Cursor cursor){

        return cursor.getString(2);
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
        List<String> receivers = new ArrayList<>();
        if(cursor.getString(8) != null && !cursor.getString(8).isEmpty()){
            receivers.add(cursor.getString(8));
        }
        rule.setAutomaticallyAnswer(new AnswerBundle(receivers, cursor.getString(9), cursor.getInt(10)));
        rule.setMessageToPostOnTwitter(cursor.getString(11));
        rule.setNavigationTarget(cursor.getString(12));
        rule.setReadThisMessage(cursor.getInt(13) == 1);
        rule.setReadOtherMessages(cursor.getInt(14) == 1);
        rule.setAddMessageToTwitterPost(cursor.getInt(15) == 1);
        rule.setActivateLight(cursor.getInt(16) == 1);
        rule.setActivateLightOnlyWhenDark(cursor.getInt(17) == 1);
        rule.setLightTime(cursor.getInt(18));
        rule.setActive(cursor.getInt(19) == 1);
        rule.setAlarmEveryTime(cursor.getInt(20) == 1);

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
            alarmSettingsModel.setNotificationLightActivated(cursor.getInt(4) == 1);
            alarmSettingsModel.setNotificationLightColor(cursor.getInt(5));
            alarmSettingsModel.setMuteAlarmActivated((cursor.getInt(6) == 1));
            alarmSettingsModel.setRepeatAlarm(cursor.getInt(7));
        }catch (IndexOutOfBoundsException e){
            return null;
        }

        return  alarmSettingsModel;
    }

    public AlarmTimeModel cursorToAlarmTime(Cursor cursor){
        AlarmTimeModel alarmTimeModel = new AlarmTimeModel();

        try {
            alarmTimeModel.setId(cursor.getInt(0));
            alarmTimeModel.setDay(AlarmTimeModel.intToDays(cursor.getInt(2)));
            alarmTimeModel.setStartTimeMinutes(cursor.getInt(3));
            alarmTimeModel.setStartTimeHours(cursor.getInt(4));
            alarmTimeModel.setEndTimeMinutes(cursor.getInt(5));
            alarmTimeModel.setEndTimeHours(cursor.getInt(6));
        }catch (IndexOutOfBoundsException e){
            alarmTimeModel = null;
        }finally {
            return alarmTimeModel;
        }
    }

    public int cursorToRuleID(Cursor cursor){

        int id = 0;

        try {
            id = cursor.getInt(0);
        }catch (IndexOutOfBoundsException e){
            id = -1;
        }finally {
            return id;
        }
    }
}
