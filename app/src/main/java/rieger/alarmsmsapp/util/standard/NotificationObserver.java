package rieger.alarmsmsapp.util.standard;

import java.io.Serializable;

/**
 * Created by sebastian on 23.03.16.
 */
public interface NotificationObserver extends Serializable {

    void notificationDismiss();
}
