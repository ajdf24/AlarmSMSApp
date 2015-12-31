package rieger.alarmsmsapp.control.receiver.worker;

import android.speech.tts.TextToSpeech;

import java.util.List;
import java.util.Locale;

import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class contains methods,with which it is possible to read messages.
 * Created by sebastian on 14.03.15.
 */
public class MessageReader implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;

    private String messageBody;

    /**
     * This method checks if the message should be read out.
     * It also creates a instance of {@link rieger.alarmsmsapp.control.receiver.worker.ReadingOutTimestamp} which contains
     * the current time when the message was received.
     */
    public void readMessage(String messageBody,List<Rule> matchingRules) {

        this.messageBody = messageBody;

        for (Rule rule: matchingRules){
            if (rule.isReadThisMessage()){
                tts = new TextToSpeech(CreateContextForResource.getContext(), this);
            }
            if (rule.isReadOtherMessages()){
                ReadingOutTimestamp.getInstance().setReadingStartTime(System.currentTimeMillis());
            }
        }


    }

    /**
     * This method reads messages out, when the read other messages value was set in the rule.
     */
    public void readOtherMessages(boolean createAlarm, String messageBody) {
        if (!createAlarm && ReadingOutTimestamp.getInstance().getReadingStartTime()+300000>System.currentTimeMillis()){
            this.messageBody = messageBody;

            tts = new TextToSpeech(CreateContextForResource.getContext(), this);
        }
    }

    /**
     * This method read a message.
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}
     */
    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.getDefault());
        tts.speak(messageBody, TextToSpeech.QUEUE_FLUSH, null);

    }
}
