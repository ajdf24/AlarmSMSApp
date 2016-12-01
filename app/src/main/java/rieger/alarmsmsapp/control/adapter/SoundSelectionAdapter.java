package rieger.alarmsmsapp.control.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.viewholder.AlarmTimeViewHolder;
import rieger.alarmsmsapp.control.viewholder.SoundSelectionViewHolder;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.Sound;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 13.10.16.
 */

public class SoundSelectionAdapter extends RecyclerView.Adapter<SoundSelectionViewHolder>{

    private final String LOG_TAG = getClass().getSimpleName();

    List<Sound> soundList;

    Rule rule;

    View itemView;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    public SoundSelectionAdapter(List<Sound> soundList, Rule rule) {
        this.soundList = soundList;
        this.rule = rule;
    }

    @Override
    public SoundSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sound, parent, false);
        return new SoundSelectionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SoundSelectionViewHolder holder, final int position) {
        holder.getSoundName().setText(soundList.get(position).getName());
        if(rule.getAlarmSoundUri().equals(soundList.get(position))){
            holder.getIsActive().setVisibility(View.VISIBLE);
        }else {
            holder.getIsActive().setVisibility(View.INVISIBLE);
        }
        holder.getSoundName().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SoundSelectionAdapter.this.notifyDataSetChanged();

                holder.getIsActive().setVisibility(View.VISIBLE);
                AudioManager audio = (AudioManager) itemView.getContext().getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

                if (currentVolume == 0) {
                    Toast.makeText(CreateContextForResource.getContext(), CreateContextForResource.getStringFromID(R.string.activity_sound_selection_volume_toast), Toast.LENGTH_SHORT).show();
                }


                Sound selectedSound = (Sound) soundList.get(position);

                rule.setAlarmSound(selectedSound);
                rule.notifyObserver();

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }

                Resources resources = itemView.getResources();
                if (selectedSound.isInternalSound()) {
                    mediaPlayer = MediaPlayer.create(itemView.getContext(), resources.getIdentifier(selectedSound.getIdForSound(), "raw", "rieger.alarmsmsapp"));
                } else {
                    mediaPlayer = MediaPlayer.create(itemView.getContext(), Uri.parse(selectedSound.getIdForSound()));
                    mediaPlayer.setLooping(false);
                }

                mediaPlayer.start();

				/*
				 * This Thread is a workaround, because there is a Bug in Android.
				 * The ANDROID_LOOP flag in a ogg-File like the ringtones dose override the method setLooping(false)
				 * of the MediaPlayer class.
				 *
				 * The Thread is only for not blocking the GUI.
				 */
                if (!selectedSound.isInternalSound()) {
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            long currentTime = System.currentTimeMillis();
                            while (currentTime + mediaPlayer.getDuration() > System.currentTimeMillis()) {

                            }
                            mediaPlayer.stop();
                            Thread.currentThread().interrupt();
                            return;
                        }
                    };
                    thread.start();
                }
                //End of the Workaround
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public void onSelectItem(){
    }

    public void stopMediaPlayer(){
        mediaPlayer.stop();
    }
}
