package rieger.alarmsmsapp.control.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * Busprovider, based on the eventbus of greenrobot
 * Created by sebastian on 31.03.16.
 */
public class BusProvider {

    private static final String LOG_TAG = BusProvider.class.getSimpleName();

    /**
     * Should be called in activities onStart() method
     *
     * @param _Object
     */
    public static void register(Object _Object) {
        EventBus.getDefault().register(_Object);
    }

    /**
     * Should be called in activities onStop() method
     *
     * @param _Object
     */
    public static void unregister(Object _Object) {
        EventBus.getDefault().unregister(_Object);
    }

    public static void post(Object _Event) {
        EventBus.getDefault().post(_Event);
    }
}
