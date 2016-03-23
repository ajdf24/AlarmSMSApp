package rieger.alarmsmsapp.control.receiver.worker;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.Sound;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class contains methods for playing a alarm sound.
 * Created by sebastian on 14.03.15.
 */
public class AlarmSoundPlayer {

    /**
     * This method plays the alarm sound from the rule.
     */
    public static void playAlarmSound(final AlarmSettingsModel alarmSettings, List<Rule> matchingRules) {

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

            Resources resources = CreateContextForResource.getContext().getResources();

            for (Rule rule : matchingRules) {
                final MediaPlayer mediaPlayer;

                final AudioManager audioManager = (AudioManager) CreateContextForResource.getContext().getSystemService(Context.AUDIO_SERVICE);
                final int maxVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 100.0 * alarmSettings.getAlarmVolume()), 0);

                Sound selectedSound = rule.getAlarmSound();
                if (selectedSound != null) {
                    if (selectedSound.isInternalSound()) {
                        mediaPlayer = MediaPlayer.create(CreateContextForResource.getContext(), resources.getIdentifier(selectedSound.getIdForSound(), "raw", "rieger.alarmsmsapp"));
                    } else {
                        mediaPlayer = MediaPlayer.create(CreateContextForResource.getContext(), Uri.parse(selectedSound.getIdForSound()));
                        mediaPlayer.setLooping(false);
                    }
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        int currentCount = 0;

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (currentCount < (alarmSettings.getRepeatAlarm() - 1)) {
                                currentCount++;
                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            }
                        }
                    });

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            long currentTime = System.currentTimeMillis();
                            while (currentTime + mediaPlayer.getDuration() * alarmSettings.getRepeatAlarm() > System.currentTimeMillis()) {

                            }
                            mediaPlayer.stop();
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
                            Thread.currentThread().interrupt();
                        }
                    };
                    thread.start();
                }else{
                    Toast.makeText(CreateContextForResource.getContext().getApplicationContext(), CreateContextForResource.getStringFromID(R.string.receiver_no_sound_selected), Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
