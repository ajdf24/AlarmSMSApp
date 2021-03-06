package rieger.alarmsmsapp.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.R;
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
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.socialnetworks.twitter.CreateTweet;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.util.standard.NotificationCreator;
import rieger.alarmsmsapp.util.standard.ScreenWorker;
import rieger.alarmsmsapp.view.LightActivity;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;

public class SMSReceiver extends BroadcastReceiver implements SensorEventListener{

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

    private float currentLuxValue = -1;

    public SMSReceiver(){
        mSensorManager = (SensorManager)CreateContextForResource.getContext().getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    }

	@Override
	public void onReceive(Context context, Intent intent) {

        resourceIdForNotificationIcon = R.drawable.ic_notification;

        readRules();

        readSettings();

        readSMSFromIntent(intent);

        createAlarm = checkIfAlarmMustBeCreate();

        if (createAlarm){
            doRuleSettings();
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

            }
        }

	}

    /**
     * This method starts the settings of a rule.
     */
    private void doRuleSettings() {

        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);

        ScreenWorker.unlockScreen();

        ScreenWorker.turnScreenOn();

//        Intent intent = new Intent();
//        intent.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(CreateContextForResource.getContext(), LightActivity.class);
//
//        CreateContextForResource.getContext().startActivity(intent);

        for (Rule rule : matchingRules){
            if (rule.isAddMessageToTwitterPost()){
                CreateTweet.tweetWithApp(rule.getMessageToPostOnTwitter() + " \"" + messageBody + "\"");
            }else{
                CreateTweet.tweetWithApp(rule.getMessageToPostOnTwitter());
            }

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
        lightIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        lightIntent.setClass(CreateContextForResource.getContext(), LightActivity.class);
        CreateContextForResource.getContext().startActivity(lightIntent);
    }

    /**
     * This method reads all sms rules.
     */
    private void readRules(){
        smsRules = RuleObserver.readAllSMSRulesFromFileSystem();
    }

    /**
     * This method reads the alarm settings.
     */
    private void readSettings(){
        try {
            alarmSettings = AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.e(this.getClass().getSimpleName(), "Alarm Settings not found.");
        }

        try {
            departmentSettings = DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.e(this.getClass().getSimpleName(), "Department Settings not found.");
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

        for(Rule rule : smsRules){
            if (rule.isActive()){
                if(rule.getSender() == null || messageSource == null){
                    continue;
                }
                if (messageSource.equals(rule.getSender())){
                    matchingRules.add(rule);
                    foundMatchingRule = true;
                    continue;
                    //Teilnummer erkennen mit *
                }if(rule.getSender().contains("*")){
                    String firstPart = rule.getSender().split("\\*")[0];
                    if(messageSource.startsWith(firstPart)){
                        matchingRules.add(rule);
                        foundMatchingRule = true;
                    }
                }

            }

        }

        if (foundMatchingRule){
            return MatchWordChecker.checkIfWordsMatch(messageBody, matchingRules);
        }

        return foundMatchingRule;
    }

    public void testReceiver(String number, String message) {

        resourceIdForNotificationIcon = R.drawable.ic_notification;

        readRules();

        readSettings();

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

            Log.i("Sensor Changed", "onSensor Change :" + event.values[0]);

            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_LIGHT){
            Log.i("Sensor Changed", "Accuracy :" + accuracy);
        }
    }
}
