package rieger.alarmsmsapp.util;

import android.app.Activity;
import android.os.Bundle;

import rieger.alarmsmsapp.model.rules.Rule;

/**
 * Utility Class, which helps to get extras from a {@link Bundle}.
 *
 * @author sebastian
 *
 */
public class BundleHandler {

	/**
	 * Static method with which it is possible to get a rule from the Bundle.
	 *
	 * @param context the {@link Activity} from which the Method is called.
	 * @return the rule from the {@link Bundle} or null if there is no rule.
	 */
	public static Rule getRuleFromBundle(Activity context) {
		Bundle bundle = context.getIntent().getExtras();
		if (bundle != null) {
			return (Rule) bundle.getSerializable(AppConstants.BUNDLE_CONTEXT_RULE);
		}
		return null;
	}

}
