package rieger.alarmsmsapp.control.observer;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class is inspired by the Observer-Pattern,
 * but it saves instances of the AlarmSettings in the file system and not in memory.
 * It is necessary, because the programmer has under android no control when the app is closed
 * and also he has no control over the start point of the app.
 *
 * So all methods are static and can called from everywhere.
 *
 * This class saves the instances of the class {@link rieger.alarmsmsapp.model.AlarmSettingsModel} in a JSON based file.
 * Also the class can change a file from a {@link rieger.alarmsmsapp.model.AlarmSettingsModel}.
 *
 * Created by sebastian on 02.03.15.
 */
public class AlarmSettingsObserver {

    /**
     * This method saves a settings to the file system.
     * If the file is already exists, so the new state is saved over the old one.
     * The method saves instances of {@link rieger.alarmsmsapp.model.AlarmSettingsModel}
     * in different folders.
     * @param settings the instance of a {@link rieger.alarmsmsapp.model.AlarmSettingsModel} which should be saved.
     */
    public static void saveSettings(AlarmSettingsModel settings){
        ObjectMapper mapper = new ObjectMapper();

        File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SETTINGS, Context.MODE_PRIVATE);
        File settingsFile = new File(smsRuleDirectory, AppConstants.StringsForObserver.DIRECTORY_NAME_SETTINGS);
        try {
            mapper.writeValue(settingsFile, settings);
        } catch (IOException e) {
        Log.e(AppConstants.DEBUG_TAG, "Error while writing settings to file system", e);
        }
    }

    /**
     * This method reads the alarm settings from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.AlarmSettingsModel}.
     * @return the alarm settings.
     */
    public static AlarmSettingsModel readSettings() throws SettingsNotFoundException {

        File settingsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SETTINGS, Context.MODE_PRIVATE);
        File []listOfSettingFiles = settingsFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        for (File fileWithSettings : listOfSettingFiles) {

            if (fileWithSettings.isFile()) {
                try {
                    return mapper.readValue(fileWithSettings, AlarmSettingsModel.class);
                } catch (IOException e) {
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
