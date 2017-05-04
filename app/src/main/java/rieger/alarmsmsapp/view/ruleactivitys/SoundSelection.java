package rieger.alarmsmsapp.view.ruleactivitys;

import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.adapter.SoundSelectionAdapter;
import rieger.alarmsmsapp.control.eventbus.BusProvider;
import rieger.alarmsmsapp.control.services.SoundService;
import rieger.alarmsmsapp.model.rules.Sound;
import rieger.alarmsmsapp.model.events.SoundEvent;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.dialoghelper.DialogHelper;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This activity is for choosing a sound.
 */
public class SoundSelection extends AbstractRuleActivity {

    private static final String LOG_TAG = SoundSelection.class.getSimpleName();

	private Rule rule;

	@Bind(R.id.activity_sound_selection_listView)
	RecyclerView listView;


	@Bind(R.id.activity_sound_selection_button_save_sound)
	FloatingActionButton save;

	@Bind(R.id.activity_sound_selection_button_add_sound)
	FloatingActionButton addSound;

	private MediaPlayer mediaPlayer = new MediaPlayer();

	SoundSelectionAdapter adapter;

	/**
     * This method is like a constructor and
     * initialize all components of the activity.
     * @param savedInstanceState
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_sound_selection);
		super.onCreate(savedInstanceState);

		rule = BundleHandler.getRuleFromBundle(this);

		getSoundsFromSystem();

		initializeGUI();

		initializeActiveElements();

	}

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.register(this);
    }

    @Override
    protected void onStop() {
        BusProvider.unregister(this);
        super.onStop();
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

        SoundService.startLoadSounds(this);

	}

    /**
     * Create a {@link ListAdapter} for the <code>listView</code>.
     * The ResourceId must be called implicit because the IDs changed after a restart of the app.
     * @param event the event with the sound, which are loaded from the system
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createListAdapter(SoundEvent event){

		adapter = new SoundSelectionAdapter(event.getSoundList(), rule);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		listView.setLayoutManager(linearLayoutManager);
		listView.setAdapter(adapter);

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

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(SoundSelection.this);
				firebaseAnalytics.logEvent("Sound_Changed", null);

				adapter.stopMediaPlayer();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, rule);
				bundle.putInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER, 2);
				intent.putExtras(bundle);
				intent.setClass(SoundSelection.this, RuleSettings.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		addSound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent_upload = new Intent();
				intent_upload.setType("audio/*");
				intent_upload.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent_upload,1);
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

	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){

		if(requestCode == 1){

			if(resultCode == RESULT_OK){

				//the selected audio.
				Uri uri = data.getData();
				try {
					String fileName = getFileName(uri);
					File filePath = new File(getFilesDir(), "soundFolder");

					if(!filePath.exists()){
						filePath.mkdirs();
					}

					File dst = new File(filePath, fileName);


					createFileFromInputStream(getContentResolver().openInputStream(uri), dst.getAbsolutePath());

					Uri dstUri = Uri.fromFile(dst);

					Sound ownSound = new Sound();
					ownSound.setID(dstUri.toString());
					ownSound.setName(fileName.replaceFirst("[.][^.]+$", ""));
					ownSound.setOwnSound(true);

					adapter.addOwnSound(ownSound);


//					rule.setOwnSoundFilePath(dst.getAbsolutePath());
//					rule.notifyObserver();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		listView.scrollToPosition(0);
	}

	public String getFileName(Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}

	private static File createFileFromInputStream(InputStream inputStream, String fileName) {

		try{
			File f = new File(fileName);
			f.setWritable(true, true);

			OutputStream outputStream = new FileOutputStream(f);

			byte buffer[] = new byte[1024];
			int length = 0;

			while((length=inputStream.read(buffer)) > 0) {
				outputStream.write(buffer,0,length);
			}

			outputStream.close();
			inputStream.close();

			return f;
		}catch (IOException e) {
			Log.e("FileWriter", "can not write file");
		}

		return null;

	}

	@Override
	protected void onPause() {
		super.onPause();

		mediaPlayer.stop();
	}

	@Override
	protected void showHelpDialog() {
		DialogHelper.createHelpDialog(this, R.string.activity_sound_selection_help_dialog_title, R.string.activity_sound_selection_help_dialog_text, R.string.dialog_button_got_it);
	}
}
