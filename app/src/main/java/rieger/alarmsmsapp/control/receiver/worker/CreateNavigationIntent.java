package rieger.alarmsmsapp.control.receiver.worker;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.view.WindowManager;

import java.util.List;

import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class contains methods which create and start a navigation intent.
 * Created by sebastian on 14.03.15.
 */
public class CreateNavigationIntent {

    /**
     * This method creates a new intent and starts a navigation with the given target if it was set.
     */
    public static void startNavigation(List<Rule> matchingRules) {

        for (Rule rule : matchingRules) {
            if (rule.getNavigationTarget() != null && !rule.getNavigationTarget().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + rule.getNavigationTarget()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                CreateContextForResource.getContext().startActivity(intent);
            }
        }
    }
}
