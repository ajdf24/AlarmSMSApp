package rieger.alarmsmsapp.util.standard;

import java.io.Serializable;

/**
 * Interface, which is called when a Notification is in action.
 * Created by sebastian on 23.03.16.
 */
public interface NotificationObserver extends Serializable {

    /**
     * Called when the notification is closed.
     */
    void notificationDismiss();
}
