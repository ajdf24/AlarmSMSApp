package rieger.alarmsmsapp.control.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Helper class for the database
 * Created by sebastian on 14.08.16.
 */
class DatabaseHelper extends SQLiteOpenHelper{

    private final String LOG_TAG = getClass().getSimpleName();

    private static final String DATABASE_NAME = "alarm_sms_app";

    private static final int DATABASE_VERSION = 4;

    //TABLE RULES
    static final String TABLE_RULES = "table_rule";

    static final String COLUMN_ID = "_id";
    static final String COLUMN_RULE_ACTIVE = "rule_active";
    static final String COLUMN_RULE_NAME = "rule_name";
    static final String COLUMN_SENDER = "sender";
    static final String COLUMN_OCCURRED_WORDS = "occurred_words";
    static final String COLUMN_NOT_OCCURRED_WORDS = "not_occurred_words";
    static final String COLUMN_SOUND_NAME = "sound_name";
    static final String COLUMN_SOUND_ID = "sound_id";
    static final String COLUMN_INTERNAL_SOUND = "internal_sound";
    static final String COLUMN_ANSWER_RECEIVER = "answer_receiver";
    static final String COLUMN_ANSWER_MESSAGE = "answer_message";
    static final String COLUMN_ANSWER_DISTANCE = "answer_distance";
    static final String COLUMN_TWITTER_MESSAGE = "twitter_message";
    static final String COLUMN_NAVIGATION_TARGET = "navigation_target";
    static final String COLUMN_READ_THIS_MESSAGE = "read_this_message";
    static final String COLUMN_READ_OTHER_MESSAGES = "read_other_messages";
    static final String COLUMN_ADD_MESSAGE_TO_TWITTER = "add_message_to_twitter";
    static final String COLUMN_ACTIVATE_LIGHT = "activate_light";
    static final String COLUMN_ACTIVATE_LIGHT_ONLY_WHEN_DARK = "activate_light_only_when_dark";
    static final String COLUMN_LIGHT_TIME = "light_time";
    static final String COLUMN_ALARM_EVERY_TIME = "alarm_every_time";
    //END TABLE RULES


    //TABLE DEPARTMENT
    static final String TABLE_DEPARTMENT_SETTINGS = "department_settings";

    static final String COLUMN_DEPARTMENT_ADDRESS = "department";
    //END TABLE DEPARTMENT


    //TABLE ALARM SETTINGS
    static final String TABLE_ALARM_SETTINGS = "alarm_settings";

    static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    static final String COLUMN_ALARM_VOLUME = "alarm_volume";
    static final String COLUMN_VIBRATION_ACTIVE = "vibration_active";
    static final String COLUMN_NOTIFICATION_LIGHT_ACTIVE = "notification_light_active";
    static final String COLUMN_NOTIFICATION_LIGHT_COLOR = "notification_light_color";
    static final String COLUMN_MUTE_ALARM = "mute_alarm";
    static final String COLUMN_REPEAT_NUMBER = "repeat_number";
    //END TABLE ALARM SETTINGS

    //TABLE MESSAGES
    static final String TABLE_MESSAGES = "table_messages";

    static final String COLUMN_SENDER_MESSAGE = "sender";
    static final String COLUMN_MESSAGE = "message";
    static final String COLUMN_TIME_STAMP = "time_stamp";
    static final String COLUMN_DAY = "day";
    static final String COLUMN_MONTH = "month";
    static final String COLUMN_YEAR = "year";
    static final String COLUMN_MATCHING_RULE_NAME = "rule_name";
    static final String COLUMN_DAY_NAME = "day_name";
    //END TABLE MESSAGES

    //TABLE ALARMTIMES
    static final String TABLE_ALARM_TIMES = "table_alarmtimes";

    static final String COLUMN_RULE_FOREIGN_KEY = "rule_key";
    static final String COLUMN_DAYS = "days";
    static final String COLUMN_START_TIME_MINUTES = "start_time_minutes";
    static final String COLUMN_START_TIME_HOURS = "start_time_hours";
    static final String COLUMN_END_TIME_MINUTES = "end_time_minutes";
    static final String COLUMN_END_TIME_HOURS = "end_time_hours";
    //END TABLE ALARMTIMES

    //TABLE MESSAGE_RECEIVER_FOR_RULE
    static final String TABLE_MESSAGE_RECEIVER_FOR_RULE = "table_message_receiver_for_rule";

    static final String COLUMN_RECEIVER = "receiver";
    //END TABLE MESSAGE_RECEIVER_FOR_RULE

    private static final String PRIMARY_KEY = " integer primary key autoincrement, ";
    private static final String TEXT = " text, ";
    private static final String INT = " integer not null, ";
    private static final String BOOL = " integer not null, ";

    private static final String CREATE_TABLE_ALARM_TIMES = "create table "
                + TABLE_ALARM_TIMES + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_RULE_FOREIGN_KEY + INT
                + COLUMN_DAYS + INT
                + COLUMN_START_TIME_MINUTES + INT
                + COLUMN_START_TIME_HOURS + INT
                + COLUMN_END_TIME_MINUTES + INT
                + COLUMN_END_TIME_HOURS + " integer not null);";

    private static final String CREATE_TABLE_MESSAGE_RECEIVER_FOR_RULE = "create table "
                + TABLE_MESSAGE_RECEIVER_FOR_RULE + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_RULE_FOREIGN_KEY + INT
                + COLUMN_RECEIVER + " text);";

    private static final String CREATE_TABLE_RULES = "create table "
                + TABLE_RULES + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_RULE_NAME + TEXT
                + COLUMN_RULE_ACTIVE + INT
                + COLUMN_SENDER + TEXT
                + COLUMN_OCCURRED_WORDS + TEXT
                + COLUMN_NOT_OCCURRED_WORDS + TEXT
                + COLUMN_SOUND_NAME + TEXT
                + COLUMN_SOUND_ID + TEXT
                + COLUMN_INTERNAL_SOUND + BOOL
                + COLUMN_ANSWER_RECEIVER + TEXT
                + COLUMN_ANSWER_MESSAGE + TEXT
                + COLUMN_ANSWER_DISTANCE + INT
                + COLUMN_TWITTER_MESSAGE + TEXT
                + COLUMN_NAVIGATION_TARGET + TEXT
                + COLUMN_READ_THIS_MESSAGE + BOOL
                + COLUMN_READ_OTHER_MESSAGES + BOOL
                + COLUMN_ADD_MESSAGE_TO_TWITTER + BOOL
                + COLUMN_ACTIVATE_LIGHT + BOOL
                + COLUMN_ACTIVATE_LIGHT_ONLY_WHEN_DARK + BOOL
                + COLUMN_ALARM_EVERY_TIME + BOOL
                + COLUMN_LIGHT_TIME + " integer not null);";

    private static final String CREATE_TABLE_DEPARTMENTS = "create table "
                + TABLE_DEPARTMENT_SETTINGS + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_DEPARTMENT_ADDRESS + " text not null);";

    private static final String CREATE_TABLE_ALARMS = "create table "
                + TABLE_ALARM_SETTINGS + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_ALARM_ACTIVE + BOOL
                + COLUMN_ALARM_VOLUME + INT
                + COLUMN_VIBRATION_ACTIVE + BOOL
                + COLUMN_NOTIFICATION_LIGHT_ACTIVE + BOOL
                + COLUMN_NOTIFICATION_LIGHT_COLOR + INT
                + COLUMN_MUTE_ALARM + BOOL
                + COLUMN_REPEAT_NUMBER + " integer not null);";

    private static final String CREATE_TABLE_MESSAGES = "create table "
            + TABLE_MESSAGES + "("
            + COLUMN_ID + PRIMARY_KEY
            + COLUMN_SENDER_MESSAGE + TEXT
            + COLUMN_MESSAGE + TEXT
            + COLUMN_TIME_STAMP + INT
            + COLUMN_DAY + INT
            + COLUMN_MONTH + INT
            + COLUMN_YEAR + INT
            + COLUMN_MATCHING_RULE_NAME + TEXT
            + COLUMN_DAY_NAME + " text not null);";

    private static final String UPDATE_VERSION_3 = "ALTER TABLE " + TABLE_RULES + " ADD COLUMN " + COLUMN_ALARM_EVERY_TIME + " integer default 1;";


    /**
     * constructor
     * @param context current context
     */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RULES);
        db.execSQL(CREATE_TABLE_DEPARTMENTS);
        db.execSQL(CREATE_TABLE_ALARMS);
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_ALARM_TIMES);
        db.execSQL(CREATE_TABLE_MESSAGE_RECEIVER_FOR_RULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "New Database version detected");

        if(oldVersion == 1){
            if(newVersion >= 2){
                db.execSQL(CREATE_TABLE_ALARM_TIMES);
            }
            if(newVersion >= 3){
                db.execSQL(UPDATE_VERSION_3);
            }
            if(newVersion >= 4){
                updateToVersion4(db);
            }
        }

        if(oldVersion == 2){
            if(newVersion >= 3){
                db.execSQL(UPDATE_VERSION_3);
            }
            if(newVersion >= 4){
                updateToVersion4(db);
            }
        }

        if(oldVersion == 3){
            if(newVersion >= 4){
                updateToVersion4(db);
            }
        }

    }

    private void updateToVersion4(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_MESSAGE_RECEIVER_FOR_RULE);
    }
}
