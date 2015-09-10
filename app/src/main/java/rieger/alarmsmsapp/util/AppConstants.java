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
	public final static String BUNDLE_CONTEXT_RULE = "Rule";

    /**
     * A {@link String} which contains a tag for all messages which created with the class {@link android.util.Log}
     */
	public final static String DEBUG_TAG = "AlarmSMS";

    /**
     * The Google API key, which is necessary for the access of the API
     */
	public static final String API_KEY = "AIzaSyDKTWvIOGBf_zwmf4yfRDDu-dMqPMLUO4s";

    /**
     * The current maximal legth of a tweet
     */
    public static final int MAXIMAL_TWITTER_LENGTH = 140;

    /**
     *  The name of the KeyguardLock
     */
    public static final String KEYGUARD_LOCK_NAME = "AlarmSMSAppLock_Keyguard";

    /**
     *  The name of the PowerManagementLock
     */
    public static final String POWERMANAGEMENT_LOCK_NAME = "AlarmSMSAppLock_PowerManagement";

	/**
	 * Strings, which are used to create a {@link URL} for the PlacesAPI-Request.
	 * @author sebastian
	 *
	 */
	public interface PalcesAPIStrings{

		public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

		public static final String TYPE_AUTOCOMPLETE = "/autocomplete";

		public static final String OUT_JSON = "/json";

	}

    /**
     * Stings, which contains the directory names in which the observers write and read.
     */
    public interface StringsForObserver{

        /**
         * The directory in which are the alarm settings are saved.
         */
        public static final String DIRECTORY_NAME_SETTINGS = "AlarmSettings";

        /**
         * The directory in which are the department settings are saved.
         */
        public static final String DIRECTORY_NAME_DEPARTMENT = "DepartmentSettings";

        /**
         * The directory in which are the SMSRules are saved.
         */
        public static final String DIRECTORY_NAME_SMS_RULES = "SMSRules";

        /**
         * The directory in which are the EMailRules are saved.
         */
        public static final String DIRECTORY_NAME_MAIL_RULES = "EMAILRules";
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
