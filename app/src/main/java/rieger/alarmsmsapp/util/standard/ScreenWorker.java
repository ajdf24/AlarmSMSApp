package rieger.alarmsmsapp.util.standard;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

import java.io.IOException;

import rieger.alarmsmsapp.util.AppConstants;

/**
 * This class contains different methods for the work with the screen.
 *
 * Created by sebastian on 23.03.15.
 */
public class ScreenWorker {

    /**
     * This method unlock the scree with the lock <code>AppConstants#KEYGUARD_LOCK_NAME</code>
     * <b>Note</b>: Need the permission <code><uses-permission android:name="android.permission.DISABLE_KEYGUARD"/></code>
     */
    public static void unlockScreen(){
        //TODO: Hier muss was gemacht werden!!!
        KeyguardManager keyguardManager = (KeyguardManager) CreateContextForResource.getContext()
                .getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock keyguardLock = keyguardManager
                .newKeyguardLock(AppConstants.KEYGUARD_LOCK_NAME);
        keyguardLock.disableKeyguard();
    }

    /**
     * This method unlock the scree with the lock <code>AppConstants#POWERMANAGEMENT_LOCK_NAME</code>
     * <b>Note</b>: Need the permission <code><uses-permission android:name="android.permission.WAKE_LOCK" /></code>
     */
    public static void turnScreenOn(){
        PowerManager powerManager = (PowerManager) CreateContextForResource.getContext()
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, AppConstants.POWERMANAGEMENT_LOCK_NAME);
        wakeLock.acquire();
    }
}
