package rieger.alarmsmsapp.control.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

import junit.framework.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.control.observer.RuleObserver;
import rieger.alarmsmsapp.control.receiver.worker.AlarmSoundPlayer;
import rieger.alarmsmsapp.control.receiver.worker.AnswerSender;
import rieger.alarmsmsapp.control.receiver.worker.CreateNavigationIntent;
import rieger.alarmsmsapp.control.receiver.worker.MatchWordChecker;
import rieger.alarmsmsapp.control.receiver.worker.MessageReader;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.socialnetworks.twitter.CreateTweet;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.util.standard.NotificationCreator;
import rieger.alarmsmsapp.util.standard.ScreenWorker;
import rieger.alarmsmsapp.view.LightActivity;

public class SMSReceiver extends BroadcastReceiver implements SensorEventListener{

    private final String LOG_TAG = getClass().getSimpleName();

    private List<Rule> smsRules;

    private AlarmSettingsModel alarmSettings;

    private String messageBody;

    private String messageSource;

    private boolean createAlarm;

    private List<Rule> matchingRules = new ArrayList<>();

    private DepartmentSettingsModel departmentSettings;

    private MessageReader messageReader = new MessageReader();

    private int resourceIdForNotificationIcon;

    private SensorManager mSensorManager;

    private Sensor mLight;

    private boolean isTest = false;

    private float currentLuxValue = -1;

    private FirebaseAnalytics mFirebaseAnalytics;

//    public SMSReceiver(){
//        mSensorManager = (SensorManager)CreateContextForResource.getContext().getSystemService(Context.SENSOR_SERVICE);
//        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//
//    }

	@Override
	public void onReceive(Context context, Intent intent) {

        mSensorManager = (SensorManager)CreateContextForResource.getContext().getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

	    Log.i(LOG_TAG, "SMS empfangen");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        resourceIdForNotificationIcon = R.drawable.ic_notification;

        readRules(context);

        readSettings(context);

        readSMSFromIntent(intent);

        createAlarm = checkIfAlarmMustBeCreate();

        if (createAlarm){
            doRuleSettings();

            mFirebaseAnalytics.logEvent("created_alarm", null);

            saveMessage(context, messageSource, messageBody);
        }

        boolean isPhoneSilent = false;

        AudioManager audio = (AudioManager) CreateContextForResource.getContext().getSystemService(Context.AUDIO_SERVICE);
        switch( audio.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                isPhoneSilent = false;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                isPhoneSilent = true;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                isPhoneSilent = true;
                break;
        }

        if (!isPhoneSilent || alarmSettings.isMuteAlarmActivated()) {
            try{
                messageReader.readOtherMessages(createAlarm, messageBody);
            }catch (NullPointerException e){
                FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Can read other messages.");
                FirebaseCrash.report(e);
            }
        }


	}

    private void saveMessage(Context context, String messageSource, String messageBody) {
        DateFormat day = new SimpleDateFormat("dd");
        DateFormat month = new SimpleDateFormat("MM");
        DateFormat year = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

        Message message = new Message();
        message.setSender(messageSource);
        message.setMessage(messageBody);
        message.setTimeStamp(System.currentTimeMillis());
        message.setDay(Integer.parseInt(day.format(currentDate)));
        message.setMonth(Integer.parseInt(month.format(currentDate)));
        message.setYear(Integer.parseInt(year.format(currentDate)));
        message.setDayName(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(currentDate));
        message.setMatchingRuleName(matchingRules.get(0).getRuleName());

        DataSource db = new DataSource(context);
        db.saveMessage(message);
    }

    /**
     * This method starts the settings of a rule.
     */
    private void doRuleSettings() {

        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        for (Rule rule : matchingRules){
            if (rule.isAddMessageToTwitterPost()){
                CreateTweet.tweetWithApp(rule.getMessageToPostOnTwitter() + " \"" + messageBody + "\"");
            }else{
                CreateTweet.tweetWithApp(rule.getMessageToPostOnTwitter());
            }

//            if(rule.isActivateLight()){
//
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
//                intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtras(bundle);
//                intent.setClass(CreateContextForResource.getContext(), LightActivity.class);
//
//                CreateContextForResource.getContext().startActivity(intent);
//            }

        }

        boolean isPhoneSilent = false;

        AudioManager audio = (AudioManager) CreateContextForResource.getContext().getSystemService(Context.AUDIO_SERVICE);
        switch( audio.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                isPhoneSilent = false;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                isPhoneSilent = true;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                isPhoneSilent = true;
                break;
        }

        if (!isPhoneSilent || alarmSettings.isMuteAlarmActivated()) {
            try {
                messageReader.readMessage(messageBody, matchingRules);
            }catch (NullPointerException e){
                FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Can read other messages.");
                FirebaseCrash.report(e);
            }
        }

        AlarmSoundPlayer.playAlarmSound(alarmSettings, matchingRules);

        if (alarmSettings.isVibrationActivated()){
            NotificationCreator.createFreeNotification(resourceIdForNotificationIcon, R.string.notification_title, messageBody, alarmSettings.getNotificationLightColor(), 100, 100, new long[] {1000, 1000, 1000, 1000, 1000, 1000}, null);
        }else{
            NotificationCreator.createFreeNotification(resourceIdForNotificationIcon, R.string.notification_title, messageBody, alarmSettings.getNotificationLightColor(), 100, 100, null, null);
        }

        AnswerSender.sendAnswerAsSMS(matchingRules, departmentSettings);

        CreateNavigationIntent.startNavigation(matchingRules);


    }

    /**
     * checks if the light activity have to be started
     * @param matchingRules the matching rules
     */
    private void startLight(List<Rule> matchingRules){
        for (Rule rule : matchingRules){
            if(rule.isActivateLight()){
                if(rule.isActivateLightOnlyWhenDark() && currentLuxValue <= 1.0) {
                    startLightActivity(rule);
                }else {
                    if(!rule.isActivateLightOnlyWhenDark()){
                        startLightActivity(rule);
                    }
                }
            }
        }

    }

    /**
     * start the light Activity
     */
    private void startLightActivity(Rule rule){
        Intent lightIntent = new Intent();

        Bundle bundle = new Bundle();

        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
        lightIntent.putExtras(bundle);
        lightIntent.addFlags(
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
            WindowManager.LayoutParams.FLAG_FULLSCREEN );
        lightIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        lightIntent.setClass(CreateContextForResource.getContext(), LightActivity.class);

        KeyguardManager km = (KeyguardManager) CreateContextForResource.getContext().getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) CreateContextForResource.getContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();


        CreateContextForResource.getContext().startActivity(lightIntent);
    }

    /**
     * This method reads all sms rules.
     */
    private void readRules(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreateContextForResource.getContext());
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, true)) {
            smsRules = RuleObserver.readAllSMSRulesFromFileSystem();
        }else{
            DataSource db = new DataSource(context);
            smsRules = db.getAllRules();
        }
    }

    private static void createFakeSms(Context context, String sender,
                                      String body) {
        byte[] pdu = null;
        byte[] scBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD("0000000000");
        byte[] senderBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD(sender);
        int lsmcs = scBytes.length;
        byte[] dateBytes = new byte[7];
        Calendar calendar = new GregorianCalendar();
        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(lsmcs);
            bo.write(scBytes);
            bo.write(0x04);
            bo.write((byte) sender.length());
            bo.write(senderBytes);
            bo.write(0x00);
            bo.write(0x00); // encoding: 0 for default 7bit
            bo.write(dateBytes);
            try {
                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
                        "stringToGsm7BitPacked", new Class[] { String.class });
                stringToGsm7BitPacked.setAccessible(true);
                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,
                        body);
                bo.write(bodybytes);
            } catch (Exception e) {
            }

            pdu = bo.toByteArray();
        } catch (IOException e) {
        }

        Intent intent = new Intent();
        intent.setClassName("com.android.mms",
                "com.android.mms.transaction.SmsReceiverService");
        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
        intent.putExtra("pdus", new Object[] { pdu });
        intent.putExtra("format", "3gpp");
        context.startService(intent);
    }

    private static byte reverseByte(byte b) {
        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
    }

    /**
     * This method reads the alarm settings.
     * <note>will be changed in v3.0</note>
     */
    private void readSettings(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreateContextForResource.getContext());
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_START, true)) {
            try {
                alarmSettings = AlarmSettingsObserver.readSettings();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }
            try {
                departmentSettings = DepartmentObserver.readSettings();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            DataSource db = new DataSource(context);
            alarmSettings = db.getAlarm();

            departmentSettings = db.getAllDepartments().get(db.getAllDepartments().size()-1);
        }

        if(alarmSettings == null){
            alarmSettings = new AlarmSettingsModel();
        }
    }

    /**
     * This method reads the sms from the intent.
     * @param intent a intent with a sms
     */
    private void readSMSFromIntent(Intent intent){

        Bundle bundle = intent.getExtras();

        if (bundle!=null){
            Object[] smsExtras = (Object[]) bundle.get("pdus");
            String format = (String)bundle.get("format");
            String strMessage = "";

            for (Object smsExtra : smsExtras) {
                SmsMessage smsMessage;
                if (Build.VERSION.SDK_INT < 23){
                    smsMessage = SmsMessage.createFromPdu((byte[]) smsExtra);
                }else {
                    smsMessage = SmsMessage.createFromPdu((byte[]) smsExtra, format);
                }

                messageBody = smsMessage.getMessageBody();
                messageSource = smsMessage.getOriginatingAddress();

                strMessage += "SMS from " + messageSource + " : " + messageBody;
                Log.i(AppConstants.DEBUG_TAG, strMessage);
            }

        }
    }

    /**
     * This method checks all sms rules for a match.
     * If match where found the list <code>matchingRules</code> contains the matching rules.
     * @return <code>true</code> if a rule matches
     */
    private synchronized boolean checkIfAlarmMustBeCreate(){

        if (!alarmSettings.isAlarmActivated()){
            return false;
        }

        boolean foundMatchingRule= false;

        int currentDay =  Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currentHour =  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute =  Calendar.getInstance().get(Calendar.MINUTE);
        DataSource db = new DataSource(CreateContextForResource.getContext());

        for(Rule rule : smsRules){
            if (rule.isActive()){



                if(rule.getSender() == null || messageSource == null){
                    continue;
                }
                if (messageSource.equals(rule.getSender())){
                    if(!rule.isAlarmEveryTime()){

                        List<AlarmTimeModel> times = db.getAlarmTimes(rule);
                        for(AlarmTimeModel alarmTime : db.getAlarmTimes(rule)){
                            if(alarmTime.checkDayForAlarmTime(currentDay)){
                                if(alarmTime.getStartTimeHours() < currentHour && alarmTime.getEndTimeHours() > currentHour ){
                                    matchingRules.add(rule);
                                    foundMatchingRule = true;
                                    break;
                                }else {
                                    if(alarmTime.getStartTimeHours() == currentHour){
                                        if(alarmTime.getStartTimeMinutes() <= currentMinute){
                                            matchingRules.add(rule);
                                            foundMatchingRule = true;
                                            break;
                                        }else {
                                            showWrongTimeToast();
                                        }
                                    }else {
                                        showWrongTimeToast();
                                    }
                                    if(alarmTime.getEndTimeHours() == currentHour){
                                        if(alarmTime.getEndTimeMinutes() >= currentMinute){
                                            matchingRules.add(rule);
                                            foundMatchingRule = true;
                                            break;
                                        }else {
                                            showWrongTimeToast();
                                        }
                                    }else {
                                        showWrongTimeToast();
                                    }
                                }
                            }else {
                                showWrongTimeToast();
                            }
                        }
                    }else {
                        matchingRules.add(rule);
                        foundMatchingRule = true;
                    }
                    continue;
                    //Teilnummer erkennen mit *
                }if(rule.getSender().contains("*")){
                    String firstPart = rule.getSender().split("\\*")[0];
                    if(messageSource.startsWith(firstPart)){
                        if(!rule.isAlarmEveryTime()){
                            List<AlarmTimeModel> times = db.getAlarmTimes(rule);
                            for(AlarmTimeModel alarmTime : db.getAlarmTimes(rule)){
                                if(alarmTime.checkDayForAlarmTime(currentDay)){
                                    if(alarmTime.getStartTimeHours() < currentHour && alarmTime.getEndTimeHours() > currentHour ){
                                        matchingRules.add(rule);
                                        foundMatchingRule = true;
                                        break;
                                    }else {
                                        if(alarmTime.getStartTimeHours() == currentHour){
                                            if(alarmTime.getStartTimeMinutes() <= currentMinute){
                                                matchingRules.add(rule);
                                                foundMatchingRule = true;
                                                break;
                                            }
                                        }
                                        if(alarmTime.getEndTimeHours() == currentHour){
                                            if(alarmTime.getEndTimeMinutes() >= currentMinute){
                                                matchingRules.add(rule);
                                                foundMatchingRule = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            matchingRules.add(rule);
                            foundMatchingRule = true;
                        }
                    }
                }

            }

        }

        if (foundMatchingRule){
            return MatchWordChecker.checkIfWordsMatch(messageBody, matchingRules);
        }

        return foundMatchingRule;
    }

    private void showWrongTimeToast(){
        if(isTest){
            Toast.makeText(CreateContextForResource.getContext(), "Regel gefunden, aber falsche Zeit!", Toast.LENGTH_SHORT).show();
            isTest = false;
        }
    }

    public void testReceiver(Context context, String number, String message) {

        isTest = true;

        resourceIdForNotificationIcon = R.drawable.ic_notification;

        readRules(context);

        readSettings(context);

        messageBody = message;
        messageSource = number;

        createAlarm = checkIfAlarmMustBeCreate();

        if (createAlarm){
            doRuleSettings();
        }else{
            Toast.makeText(CreateContextForResource.getContext(),R.string.toast_no_rule_match,Toast.LENGTH_LONG).show();
        }

        boolean isPhoneSilent = false;

        AudioManager audio = (AudioManager) CreateContextForResource.getContext().getSystemService(Context.AUDIO_SERVICE);
        switch( audio.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                isPhoneSilent = false;
                break;
            case AudioManager.RINGER_MODE_SILENT:
                isPhoneSilent = true;
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                isPhoneSilent = true;
                break;
        }

        if (!isPhoneSilent || alarmSettings.isMuteAlarmActivated()) {
            try {
                messageReader.readOtherMessages(createAlarm, messageBody);
            }catch (NullPointerException e){

            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT){

            currentLuxValue = event.values[0];

            startLight(matchingRules);

            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_LIGHT){
        }
    }
}
