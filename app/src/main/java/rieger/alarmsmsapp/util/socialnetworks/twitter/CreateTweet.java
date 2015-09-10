package rieger.alarmsmsapp.util.socialnetworks.twitter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class contains methods for creating a tweet on twitter.
 * Created by sebastian on 23.03.15.
 */
public class CreateTweet {

    /**
     * This method create a intent for posting a tweet on twitter by using the twitter app.
     * @param tweetMessage message which should be posted
     */
    public static void tweetWithApp(String tweetMessage){
        if(tweetMessage != null) {
            Intent tweetIntent = new Intent(Intent.ACTION_SEND);
            tweetIntent.putExtra(Intent.EXTRA_TEXT, tweetMessage);
            tweetIntent.setType("text/plain");
            tweetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            PackageManager packManager = CreateContextForResource.getContext().getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {
                if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                    tweetIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                CreateContextForResource.getContext().startActivity(tweetIntent);
            } else {
                Toast.makeText(CreateContextForResource.getContext(), CreateContextForResource.getStringFromID(R.string.twitter_not_found), Toast.LENGTH_LONG).show();
            }
        }
    }
}
