package rieger.alarmsmsapp.control.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sebastian on 14.08.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private final String LOG_TAG = getClass().getSimpleName();

    public static final String DATABASE_NAME = "alarm_sms_app";

    public static final int DATABASE_VERSION = 1;

    //TABLE RULES
    public static final String TABLE_RULES = "table_rule";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RULE_NAME = "rule_name";
    public static final String COLUMN_SENDER = "sender";
    public static final String COLUMN_OCCURRED_WORDS = "occurred_words";
    public static final String COLUMN_NOT_OCCURRED_WORDS = "not_occurred_words";
    public static final String COLUMN_SOUND_NAME = "sound_name";
    public static final String COLUMN_SOUND_ID = "sound_id";
    public static final String COLUMN_INTERNAL_SOUND = "internal_sound";
    public static final String COLUMN_ANSWER_RECEIVER = "answer_receiver";
    public static final String COLUMN_ANSWER_MESSAGE = "answer_message";
    public static final String COLUMN_ANSWER_DISTANCE = "answer_distance";
    public static final String COLUMN_TWITTER_MESSAGE = "twitter_message";
    public static final String COLUMN_NAVIGATION_TARGET = "navigation_target";
    public static final String COLUMN_READ_THIS_MESSAGE = "read_this_message";
    public static final String COLUMN_READ_OTHER_MESSAGES = "read_other_messages";
    public static final String COLUMN_ADD_MESSAGE_TO_TWITTER = "add_message_to_twitter";
    public static final String COLUMN_ACTIVATE_LIGHT = "activate_light";
    public static final String COLUMN_ACTIVATE_LIGHT_ONLY_WHEN_DARK = "activate_light_only_when_dark";
    public static final String COLUMN_LIGHT_TIME = "light_time";
    //END TABLE RULES


    //TABLE DEPARTMENT
    public static final String TABLE_DEPARTMENT_SETTINGS = "department_settings";

    public static final String COLUMN_DEPARTMENT_ADDRESS = "department";
    //END TABLE DEPARTMENT


    //TABLE ALARM SETTINGS
    public static final String TABLE_ALARM_SETTINGS = "alarm_settings";

    public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
    public static final String COLUMN_ALARM_VOLUME = "alarm_volume";
    public static final String COLUMN_VIBRATION_ACTIVE = "vibration_active";
    public static final String COLUMN_NOTIFICATION_LIGHT_ACTIVE = "notification_light_active";
    public static final String COLUMN_NOTIFICATION_LIGHT_COLOR = "notification_light_color";
    public static final String COLUMN_MUTE_ALARM = "mute_alarm";
    public static final String COLUMN_REPEAT_NUMBER = "repeat_number";
    //END TABLE ALARM SETTINGS

    //TABLE MESSAGES
    public static final String TABLE_MESSAGES = "table_messages";

    public static final String COLUMN_SENDER_MESSAGE = "sender";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_TIME_STAMP = "time_stamp";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MATCHING_RULE_NAME = "rule_name";
    public static final String COLUMN_DAY_NAME = "day_name";
    //END TABLE MESSAGES

    public static final String PRIMARY_KEY = " integer primary key autoincrement, ";
    public static final String TEXT = " text, ";
    public static final String INT = " integer not null, ";
    public static final String BOOL = " integer not null, ";

    public static final String CREATE_TABLE_RULES = "create table "
                + TABLE_RULES + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_RULE_NAME + TEXT
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
                + COLUMN_LIGHT_TIME + " integer not null);";

    public static final String CREATE_TABLE_DEPARTMENTS = "create table "
                + TABLE_DEPARTMENT_SETTINGS + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_DEPARTMENT_ADDRESS + " text not null);";

    public static final String CREATE_TABLE_ALARMS = "create table "
                + TABLE_ALARM_SETTINGS + "("
                + COLUMN_ID + PRIMARY_KEY
                + COLUMN_ALARM_ACTIVE + BOOL
                + COLUMN_ALARM_VOLUME + INT
                + COLUMN_VIBRATION_ACTIVE + BOOL
                + COLUMN_NOTIFICATION_LIGHT_ACTIVE + BOOL
                + COLUMN_NOTIFICATION_LIGHT_COLOR + INT
                + COLUMN_MUTE_ALARM + BOOL
                + COLUMN_REPEAT_NUMBER + " integer not null);";

    public static final String CREATE_TABLE_MESSAGES = "create table "
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


    /**
     * constructor
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RULES);
        db.execSQL(CREATE_TABLE_DEPARTMENTS);
        db.execSQL(CREATE_TABLE_ALARMS);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}