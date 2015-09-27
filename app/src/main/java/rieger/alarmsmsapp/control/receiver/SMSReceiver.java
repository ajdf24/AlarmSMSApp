package rieger.alarmsmsapp.control.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

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

public class SMSReceiver extends BroadcastReceiver {

    private List<Rule> smsRules;

    private AlarmSettingsModel alarmSettings;

    private String messageBody;

    private String messageSource;

    private boolean createAlarm;

    private List<Rule> matchingRules = new ArrayList<>();

    private DepartmentSettingsModel departmentSettings;

    private MessageReader messageReader = new MessageReader();

    private int resourceIdForNotificationIcon;


    public SMSReceiver(){

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

            messageReader.readOtherMessages(createAlarm, messageBody);
        }

	}



    /**
     * This method starts the settings of a rule.
     */
    private void doRuleSettings() {

        ScreenWorker.unlockScreen();

        ScreenWorker.turnScreenOn();

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

            messageReader.readMessage(messageBody, matchingRules);
        }

        AlarmSoundPlayer.playAlarmSound(alarmSettings, matchingRules);

        if (alarmSettings.isVibrationActivated()){
            NotificationCreator.createFreeNotification(resourceIdForNotificationIcon, R.string.notification_title, messageBody, alarmSettings.getNotificationLightColor(), 100, 100, new long[] {1000, 1000, 1000, 1000, 1000, 1000});
        }else{
            NotificationCreator.createFreeNotification(resourceIdForNotificationIcon, R.string.notification_title, messageBody, alarmSettings.getNotificationLightColor(), 100, 100, null);
        }


        AnswerSender.sendAnswerAsSMS(matchingRules, departmentSettings);

        CreateNavigationIntent.startNavigation(matchingRules);


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

            String strMessage = "";

            for (Object smsExtra : smsExtras) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) smsExtra);


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
                if (messageSource.equals(rule.getSender())){
                    matchingRules.add(rule);
                    foundMatchingRule = true;
                }
            }

        }

        if (foundMatchingRule){
            return MatchWordChecker.checkIfWordsMatch(messageBody, matchingRules);
        }

        return foundMatchingRule;
    }

    public void testRule(){

    }
}
