package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.model.Sound;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for choosing a sound.
 */
public class SoundSelection extends AppCompatActivity {

    private static final String LOG_TAG = SoundSelection.class.getSimpleName();

	private Rule rule;

	private List<Sound> soundList = new ArrayList<Sound>();

	private Sound selectedSound;

	@Bind(R.id.activity_sound_selection_listView)
	ListView listView;


	@Bind(R.id.activity_sound_selection_button_save_sound)
	Button save;

	@Bind(R.id.activity_sound_selection_button_quit)
	Button quit;

	private MediaPlayer mediaPlayer = new MediaPlayer();

    /**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_selection);

		rule = BundleHandler.getRuleFromBundle(this);

		getSoundsFromSystem();

		initializeGUI();

		initializeActiveElements();

	}

    /**
     * Overrides the method and returns the name of the activity,
     * which is necessary for the {@link rieger.alarmsmsapp.view.ruleactivitys.RuleSettings} activity.
     * @return the name of the activity
     */
	@Override
	public String toString() {
		return CreateContextForResource.getStringFromID(R.string.title_activity_sound_selection);
	}

	/**
	 * Adds all Resources from {@link R} raw to the <code>soundList</code>
	 */
	private void getSoundsFromSystem(){

		soundList.add(new Sound("Motorola BMD Schleife 1","bmd_ton1", true));
		soundList.add(new Sound("Motorola BMD Schleife 2","bmd_ton2", true));
		soundList.add(new Sound("E57 Sirene","e57_sirene", true));
		soundList.add(new Sound("Skyfire Schleife 1","firestorm_ton1", true));
		soundList.add(new Sound("Skyfire Schleife 2","firestorm_ton2", true));
		soundList.add(new Sound("Quattro 96 Schleife 1","q96_schleife1", true));
		soundList.add(new Sound("Quattro 96 Schleife 2","q96_schleife2", true));
		soundList.add(new Sound("Quattro 96 Schleife 3","q96_schleife3", true));
		soundList.add(new Sound("Quattro 96 Schleife 4","q96_schleife4", true));
		soundList.add(new Sound("Quattro 98 Schleife 1","quattro_98_ton1", true));
		soundList.add(new Sound("Quattro 98 Schleife 2","quattro_98_ton2", true));

		File[] files = new File("/system/media/audio/ringtones/").listFiles();

		if(files != null) {
			for (File file : files) {
				if (file.isFile()) {
					String name = file.getName();
					int pos = name.lastIndexOf(".");
					if (pos > 0) {
						name = name.substring(0, pos);
					}
					soundList.add(new Sound(name, "/system/media/audio/ringtones/" + file.getName(), false));

				}
			}
		}

	}

	/**
	 * Create a {@link ListAdapter} for the <code>listView</code>.
	 * The ResourceId must be called implicit because the IDs changed after a restart of the app.
	 */
	private void createListAdapter() {
		ListAdapter adapter = new ArrayAdapter<Sound>( getApplicationContext(), R.layout.list_item_rule_settings, soundList);


		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public synchronized void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

                if (currentVolume == 0){
                    Toast.makeText(CreateContextForResource.getContext(),CreateContextForResource.getStringFromID(R.string.activity_sound_selection_volume_toast),Toast.LENGTH_SHORT).show();
                }


				selectedSound = (Sound) listView.getAdapter().getItem(arg2);

				if(mediaPlayer.isPlaying()){
					mediaPlayer.stop();
				}

				Resources resources = getResources();
				if(selectedSound.isInternalSound()){
					mediaPlayer = MediaPlayer.create(SoundSelection.this, resources.getIdentifier(selectedSound.getIdForSound(), "raw", "rieger.alarmsmsapp"));
				}else{
					mediaPlayer = MediaPlayer.create(SoundSelection.this, Uri.parse(selectedSound.getIdForSound()));
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
				if(!selectedSound.isInternalSound()){
					Thread thread = new Thread(){
						@Override
						public void run(){
							long currentTime = System.currentTimeMillis();
							while(currentTime+mediaPlayer.getDuration()>System.currentTimeMillis()){

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

	/**
	 * Initialize all GUI elements.
	 */
	private void initializeGUI() {
		ButterKnife.bind(this);
	}

	/**
	 * Initialize all listeners for the GUI elements.
	 */
	private void initializeActiveElements() {
		createListAdapter();

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mediaPlayer.stop();
				RuleCreator.changeAlarmSound(rule, selectedSound );

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

				intent.putExtras(bundle);
				intent.setClass(SoundSelection.this, RuleSettings.class);
				startActivity(intent);
			}
		});

		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

                mediaPlayer.stop();

				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);

				intent.putExtras(bundle);
				intent.setClass(SoundSelection.this, RuleSettings.class);
				startActivity(intent);
			}
		});

		mediaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mediaPlayer.stop();
				return true;
			}
		});
	}

}
