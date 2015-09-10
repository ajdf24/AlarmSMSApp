package rieger.alarmsmsapp.util.standard;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * This class checks the permissions of the new permission model, which is introduced in android 6.0 (SDK 23).
 * This class is backward compatible and uses methods of the library AppCompat. <br>
 * <b>Note:</b> if you use this class add the line <code>compile 'com.android.support:appcompat-v7:23.0.0'</code> under dependencies in the build.gradle file.
 *
 * Created by sebastian on 18.08.15.
 */
public class PermissionChecker {

    /**
     * This method checks if a permission is granted.
     * @param permission the permission which should be checked
     * @return <code>true</code> if the permission is granted und <code>false</code> otherwise
     */
    public static boolean checkPermission(String permission){
        if (ActivityCompat.checkSelfPermission(CreateContextForResource.getContext(), permission) ==
                PackageManager.PERMISSION_GRANTED ) {

            return true;
        }
        return false;
    }

//    public static boolean checkPermissions(String[] permissions, Activity activity){
//        for (String permission : permissions){
//            if (ActivityCompat.checkSelfPermission(CreateContextForResource.getContext(), permission) ==
//                    PackageManager.PERMISSION_GRANTED ) {
//
//                ActivityCompat.requestPermissions(activity, new String[]{permission},
//                        AppConstants.PermissionsIDs.PERMISSION_ID_FOR_ALL);
//            }
//        }
//    }

    /**
     * This method allows you to generate multiple permission requests.
     * <b>Note:</b>This method does not support a group of permissions.
     * Every permission need her own
     * @param activity the used activity
     * @param permissions the permissions which should be checked
     * @param requestCodes the request codes for permissions
     * @throws IllegalArgumentException if the arrays have different lengths
     */
    public static void permissionRequest(Activity activity, String permissions[], int requestCodes[] )throws IllegalArgumentException{
        if(permissions.length != requestCodes.length) {
            throw new IllegalArgumentException ("Error: permissions and requestCodes have different lengths");
        }
        for (int i = 0; i < permissions.length; i++){
            ActivityCompat.requestPermissions(activity, new String[]{permissions[i]}, requestCodes[i]);
        }
    }

    /**
     * This method allows you to generate multiple permission requests.
     * <b>Note:</b>This method does not support a group of permissions.
     * Every permission need her own
     * @param activity the used activity
     * @param permissions the permissions which should be checked
     * @param requestCodes the request codes for permissions
     */
    public static void permissionRequest(Activity activity, String permissions[], int requestCodes ){
        for (int i = 0; i < permissions.length; i++){
            System.out.println("CHECK");
            if (ActivityCompat.checkSelfPermission(CreateContextForResource.getContext(), permissions[i]) !=
                    PackageManager.PERMISSION_GRANTED ) {
                System.out.println("DENIED");
                ActivityCompat.requestPermissions(activity, new String[]{permissions[i]}, requestCodes);
            }
        }
    }
}
