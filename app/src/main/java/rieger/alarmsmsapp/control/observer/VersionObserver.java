package rieger.alarmsmsapp.control.observer;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.IOException;

import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.model.Version;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 27.09.15.
 */
public class VersionObserver {

    public static final String LOG_TAG = "VersionObserver";

    /**
     * This method saves a settings to the file system.
     * If the file is already exists, so the new state is saved over the old one.
     * The method saves instances of {@link rieger.alarmsmsapp.model.AlarmSettingsModel}
     * in different folders.
     * @param version the instance of a {@link rieger.alarmsmsapp.model.AlarmSettingsModel} which should be saved.
     */
    public static void saveSettings(Version version){
        ObjectMapper mapper = new ObjectMapper();

        File versionDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_Version, Context.MODE_PRIVATE);
        File settingsFile = new File(versionDirectory, AppConstants.StringsForObserver.DIRECTORY_NAME_Version);
        try {
            mapper.writeValue(settingsFile, version);
        } catch (IOException e) {
            FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Error while writing settings to file system.");
            FirebaseCrash.report(e);
        }
    }

    /**
     * This method reads the alarm settings from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.AlarmSettingsModel}.
     * @return the alarm settings.
     */
    public static Version readSettings() throws SettingsNotFoundException {

        File versionFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_Version, Context.MODE_PRIVATE);
        File []listOfSettingFiles = versionFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        for (File fileWithSettings : listOfSettingFiles) {

            if (fileWithSettings.isFile()) {
                try {
                    return mapper.readValue(fileWithSettings, Version.class);
                } catch (IOException e) {
                    FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Can not read Files.");
                    FirebaseCrash.report(e);
                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
                }
            }
        }
        if (listOfSettingFiles.length == 0){
            throw new SettingsNotFoundException();
        }
        return null;
    }
}
