package rieger.alarmsmsapp.util;

import java.net.URL;

/**
 * Collection of different constants in the app.
 * @author sebastian
 *
 */
public interface AppConstants {

    /**
     * A {@link String} which contains the context for putting a {@link rieger.alarmsmsapp.model.rules.Rule}
     * in a {@link android.os.Bundle}
     */
    String BUNDLE_CONTEXT_RULE = "Rule";
	String BUNDLE_CONTEXT_NUMBER = "Number";
	String BUNDLE_CONTEXT_MESSAGE = "Message";
	String BUNDLE_CONTEXT_NOTIFICATION_OBSERVER = "Notification Observer";
    String BUNDLE_CONTEXT_SERIALIZED_MESSAGE = "Serialized Message";

    /**
     * A {@link String} which contains a tag for all messages which created with the class {@link android.util.Log}
     */
    String DEBUG_TAG = "AlarmSMS";

    /**
     * The Google API key, which is necessary for the access of the API
     */
    String API_KEY = "AIzaSyDKTWvIOGBf_zwmf4yfRDDu-dMqPMLUO4s";

    /**
     * The current maximal legth of a tweet
     */
    int MAXIMAL_TWITTER_LENGTH = 140;

    /**
     *  The name of the KeyguardLock
     */
    String KEYGUARD_LOCK_NAME = "AlarmSMSAppLock_Keyguard";

    /**
     *  The name of the PowerManagementLock
     */
    String POWERMANAGEMENT_LOCK_NAME = "AlarmSMSAppLock_PowerManagement";

    String BUNDLE_SETTINGS_TAB_NUMBER = "settings tab";



    interface CallBacks{
        String TIMEFIELED_FROM = "FROM";

        String TIMEFIELD_TO = "TO";

        String REMOVE_TIME_CALLBACK = "remove time callback";

        String SHOW_SHOW_CASE_VIEW_NEW_ALARM_TIME = "show case view new alarm time";
    }

	/**
	 * Strings, which are used to create a {@link URL} for the PlacesAPI-Request.
	 * @author sebastian
	 *
	 */
    interface PalcesAPIStrings{

		String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

		String TYPE_AUTOCOMPLETE = "/autocomplete";

		String OUT_JSON = "/json";

	}

    /**
     * Stings, which contains the directory names in which the observers write and read.
     */
    interface StringsForObserver{

        /**
         * The directory in which are the alarm settings are saved.
         */
        String DIRECTORY_NAME_SETTINGS = "AlarmSettings";

        /**
         * The directory in which are the version are saved.
         */
        String DIRECTORY_NAME_Version = "Version";

        /**
         * The directory in which are the department settings are saved.
         */
        String DIRECTORY_NAME_DEPARTMENT = "DepartmentSettings";

        /**
         * The directory in which are the SMSRules are saved.
         */
        String DIRECTORY_NAME_SMS_RULES = "SMSRules";

        /**
         * The directory in which are the EMailRules are saved.
         */
        String DIRECTORY_NAME_MAIL_RULES = "EMAILRules";

        /**
         * The directory in which are the messages are saved.
         */
        String DIRECTORY_NAME_MESSAGES = "Messages";
    }

    /**
     * Ints, which are the ids for all permissions.
     */
    interface PermissionsIDs{
        int PERMISSION_ID_FOR_ALL = 0;
        int PERMISSION_ID_FOR_SMS = 1;
        int PERMISSION_ID_FOR_CONTACTS = 2;
        int PERMISSION_ID_FOR_LOCATION = 3;
        int PERMISSION_ID_FOR_STORAGE = 4;
    }

    interface SharedPreferencesKeys{
        String FIRST_START = "First Start";
        String FIRST_SHOW_CREATE_RULE = "First Create Rule";
        String FIRST_SHOW_CREATE_RULE_SAVE = "First Create Rule Save";
        String FIRST_SHOW_SETTINGS = "First show settings";
        String FIRST_SHOW_RULES = "First show rules";
        String FIRST_SHOW_ALARM_TIME_LIST = "First show alarm time list";
    }

    interface Fragments{
        String WELCOME_FRAGMENT = "WelcomeFragment";
        String ALARM_SETTINGS_FRAGMENT = "AlarmSettingsFragment";
        String DEPARTMENT_EXPLANATION_FRAGMENT = "DepartmentExplanationFragment";
        String DEPARTMENT_FRAGMENT = "DepartmentFragment";
        String READY_FRAGMENT = "ReadyFragment";
    }

}
