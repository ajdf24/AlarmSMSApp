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
	String BUNDLE_CONTEXT_Notification_Observer = "Notification Observer";

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

}
