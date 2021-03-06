package rieger.alarmsmsapp.util.standard;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import rieger.alarmsmsapp.util.AppConstants;

/**
 * This utility class contains methods, which create a notification for the android system.
 * <br>
 * <b>Note</b>: you can create other Notification methods if they needed,
 * but please use for this the method <code>createFreeNotification</code>
 * Created by sebastian on 14.03.15.
 */
public class NotificationCreator {

    /**
     * This method creates a standard notification with the light color <code>{@link Color#WHITE}</code>,
     * with a light frequency of 0.1 minutes and without a icon,
     * @param contentTitle the title of the notification as {@link String}
     * @param contentText the text of the notification as {@link String}
     */
    public static void createSimpleNotification(String contentTitle, String contentText){
        createFreeNotification(0,contentTitle,contentText,Color.WHITE, 6000, 6000, null, null);
    }

    /**
     * This method creates a notification on which all settings can be set by the programmer.
     * @param icon the resource id of the notification icon
     * @param contentTitle the title of the notification as resource id
     * @param contentText the text of the notification as resource id
     * @param lightColor the color of the light, set like <code>{@link Color#RED}/code>
     * @param timeLightOn the time in ms which the light is on
     * @param timeLightOff the time in ms which the light is off
     * @param vibrate array with the vibration interval
     */
    public static void createFreeNotification(int icon, int contentTitle, int contentText, int lightColor, int timeLightOn, int timeLightOff, long[] vibrate) {
        NotificationManager notificationManager = (NotificationManager) CreateContextForResource.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(CreateContextForResource.getContext().getApplicationContext())
                        .setSmallIcon(icon)
                        .setContentTitle(CreateContextForResource.getStringFromID(contentTitle))
                        .setContentText(CreateContextForResource.getStringFromID(contentText))
                        .setLights(lightColor, timeLightOn, timeLightOff)
                        .setVibrate(vibrate);

        notificationManager.notify(0, builder.build());

    }

    /**
     * This method creates a notification on which all settings can be set by the programmer.
     * @param icon the resource id of the notification icon
     * @param contentTitle the title of the notification as {@link String}
     * @param contentText the text of the notification as {@link String}
     * @param lightColor the color of the light, set like <code>{@link Color#RED}/code>
     * @param timeLightOn the time in ms which the light is on
     * @param timeLightOff the time in ms which the light is off
     * @param vibrate array with the vibration interval
     */
    public static void createFreeNotification(int icon, String contentTitle, String contentText, int lightColor, int timeLightOn, int timeLightOff, long[] vibrate, NotificationObserver observer) {
        NotificationManager notificationManager = (NotificationManager) CreateContextForResource.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(CreateContextForResource.getContext(), NotificationBroadcastReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_Notification_Observer, observer);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateContextForResource.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(CreateContextForResource.getContext().getApplicationContext())

                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setLights(lightColor, timeLightOn, timeLightOff)
                        .setVibrate(vibrate)
                        .setDeleteIntent(pendingIntent);
        if (icon != 0){
            builder.setSmallIcon(icon);
        }

        notificationManager.notify(0, builder.build());

    }

    /**
     * This method creates a notification on which all settings can be set by the programmer.
     * @param icon the resource id of the notification icon
     * @param contentTitle the title of the notification as {@link String}
     * @param contentText the text of the notification as {@link String}
     * @param lightColor the color of the light, set like <code>{@link Color#RED}/code>
     * @param timeLightOn the time in ms which the light is on
     * @param timeLightOff the time in ms which the light is off
     * @param vibrate array with the vibration interval
     * @param observer the Observer which is called
     */
    public static void createFreeNotification(int icon, int contentTitle, String contentText, int lightColor, int timeLightOn, int timeLightOff, long[] vibrate, NotificationObserver observer) {
        NotificationManager notificationManager = (NotificationManager) CreateContextForResource.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(CreateContextForResource.getContext(), NotificationBroadcastReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_Notification_Observer, observer);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateContextForResource.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(CreateContextForResource.getContext().getApplicationContext())
                        .setSmallIcon(icon)
                        .setContentTitle(CreateContextForResource.getStringFromID(contentTitle))
                        .setContentText(contentText)
                        .setLights(lightColor, timeLightOn, timeLightOff)
                        .setVibrate(vibrate)
                        .setDeleteIntent(pendingIntent);


        notificationManager.notify(0, builder.build());

    }

    /**
     * This method creates a notification on which all settings can be set by the programmer.
     * @param icon the resource id of the notification icon
     * @param contentTitle the title of the notification as {@link String}
     * @param contentText the text of the notification as {@link String}
     * @param lightColor the color of the light, set like <code>{@link Color#RED}/code>
     * @param timeLightOn the time in ms which the light is on
     * @param timeLightOff the time in ms which the light is off
     * @param vibrate array with the vibration interval
     * @param observer the Observer which is called
     */
    public static void createFreeNotification(int icon, String contentTitle, int contentText, int lightColor, int timeLightOn, int timeLightOff, long[] vibrate, NotificationObserver observer) {
        NotificationManager notificationManager = (NotificationManager) CreateContextForResource.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(CreateContextForResource.getContext().getApplicationContext())
                        .setSmallIcon(icon)
                        .setContentTitle(contentTitle)
                        .setContentText(CreateContextForResource.getStringFromID(contentText))
                        .setLights(lightColor, timeLightOn, timeLightOff)
                        .setVibrate(vibrate);

        notificationManager.notify(0, builder.build());

    }

}
