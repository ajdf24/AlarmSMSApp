package rieger.alarmsmsapp.util.standard;


import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import rieger.alarmsmsapp.util.AppConstants;

/**
 * Helper class for getting string resources without {@link Context}.
 * <br>
 * This class creates a static {@link Context} with which it is possible
 * to get string resources without a {@link android.app.Activity} or a {@link android.app.Service}.
 *
 * <b>Note</b>: This class must be defined in the manifest.
 * @author sebastian
 *
 */
public class CreateContextForResource extends Application {
	private static Context context;

	/**
	 * This method create a context from it self.
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
	}

	/**
	 * Get the {@link Context} from this class.
	 * @return the {@link Context} from this class
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * This method fetch a ID from {@link android.R} and return the string.
	 * @param id a id from {@link android.R}
	 * @return the defined string in the current language or <code>null</code> if there is no string.
	 */
	public static String getStringFromID(int id) {
		Resources resources = context.getResources();
		try{

		return resources.getString(id);

		}catch(NotFoundException e){
			Log.e(AppConstants.DEBUG_TAG, "Resource not found", e);
		}
		return null;
	}

}
