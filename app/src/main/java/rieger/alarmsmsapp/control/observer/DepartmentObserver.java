package rieger.alarmsmsapp.control.observer;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 04.03.15.
 */
public class DepartmentObserver {

    /**
     * This method saves a department to the file system.
     * If the file is already exists, so the new state is saved over the old one.
     * The method saves instances of {@link rieger.alarmsmsapp.model.AlarmSettingsModel}
     * in different folders.
     * @param settings the instance of a {@link rieger.alarmsmsapp.model.AlarmSettingsModel} which should be saved.
     */
    public static void saveSettings(DepartmentSettingsModel settings){
        ObjectMapper mapper = new ObjectMapper();

        File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_DEPARTMENT, Context.MODE_PRIVATE);
        File settingsFile = new File(smsRuleDirectory, AppConstants.StringsForObserver.DIRECTORY_NAME_DEPARTMENT);
        try {
            mapper.writeValue(settingsFile, settings);
        } catch (IOException e) {
            Log.e(AppConstants.DEBUG_TAG, "Error while writing department to file system", e);
        }
    }

    /**
     * This method reads the department from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.AlarmSettingsModel}.
     * @return the department settings.
     */
    public static DepartmentSettingsModel readSettings() throws SettingsNotFoundException{

        File settingsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_DEPARTMENT, Context.MODE_PRIVATE);
        File []listOfDepartmentsFiles = settingsFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        for (File fileWithDepartments : listOfDepartmentsFiles) {

            if (fileWithDepartments.isFile()) {
                try {
                    return mapper.readValue(fileWithDepartments, DepartmentSettingsModel.class);
                } catch (IOException e) {
                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
                }
            }
        }
        if (listOfDepartmentsFiles.length == 0){
            throw new SettingsNotFoundException();
        }
        return null;
    }
}
