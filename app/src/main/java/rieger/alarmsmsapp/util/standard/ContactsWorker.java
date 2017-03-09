package rieger.alarmsmsapp.util.standard;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;

import rieger.alarmsmsapp.util.AppConstants;

/**
 * This is a helper class for dealing with contacts from the android system.
 * Created by Sebastian on 29.03.2016.
 */
public class ContactsWorker {

    /**
     * Get the name for a number from the android contacts.
     * @param context the context in which this method is called
     * @param phoneNumber the number which you want to look for a number
     * @return the name as String or <code>null</code> if no name was found
     */
    public static String getContactName(Activity context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
        }else {
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        }
        try {
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return null;
            }
            String contactName = null;
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return contactName;
        }catch (NullPointerException e){
            return null;
        }
    }

    public static Uri getContactImageUri(Activity context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = null;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_CONTACTS},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_CONTACTS);
        }else {
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        }
        try {
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI}, null, null, null);
            if (cursor == null) {
                return null;
            }
            String contactUriString = null;
            if (cursor.moveToFirst()) {
                contactUriString = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI));
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            return contactUriString != null ? Uri.parse(contactUriString) : null;
        }catch (NullPointerException e){
            return null;
        }
    }
}
