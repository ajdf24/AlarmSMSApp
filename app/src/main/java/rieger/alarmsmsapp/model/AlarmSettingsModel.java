package rieger.alarmsmsapp.model;

import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * A model class, which contains all settings for the alarm.
 *
 * Created by sebastian on 02.03.15.
 */
public class AlarmSettingsModel {

    private boolean isAlarmActivated = true;

    private int alarmVolume = 90;

    private boolean isVibrationActivated;

    private boolean isNotificationLightActivated;

    private int notificationLightColor;

    private boolean isMuteAlarmActivated = true;

    private int repeatAlarm = 1;

    /**
     *
     * @return the actual state
     */
    public boolean isAlarmActivated() {
        return isAlarmActivated;
    }

    /**
     *
     * @return the actual state
     */
    public int getAlarmVolume() {
        return alarmVolume;
    }

    /**
     *
     * @return the actual state
     */
    public boolean isVibrationActivated() {
        return isVibrationActivated;
    }

    /**
     *
     * @return the actual state
     */
    public boolean isNotificationLightActivated() {
        return isNotificationLightActivated;
    }

    /**
     *
     * @return the actual state
     */
    public int getNotificationLightColor() {
        return notificationLightColor;
    }

    /**
     *
     * @return the actual state
     */
    public boolean isMuteAlarmActivated() {
        return isMuteAlarmActivated;
    }

    /**
     *
     * @param isAlarmActivated the state
     */
    public void setAlarmActivated(boolean isAlarmActivated) {
        this.isAlarmActivated = isAlarmActivated;
    }

    /**
     *
     * @param alarmVolume the state
     */
    public void setAlarmVolume(int alarmVolume) {
        this.alarmVolume = alarmVolume;
    }

    /**
     *
     * @param isVibrationActivated the state
     */
    public void setVibrationActivated(boolean isVibrationActivated) {
        this.isVibrationActivated = isVibrationActivated;
    }

    /**
     *
     * @param isNotificationLightActivated the state
     */
    public void setNotificationLightActivated(boolean isNotificationLightActivated) {
        this.isNotificationLightActivated = isNotificationLightActivated;
    }

    /**
     *
     * @param notificationLightColor the state
     */
    public void setNotificationLightColor(int notificationLightColor) {
        this.notificationLightColor = notificationLightColor;
    }

    /**
     *
     * @param isMuteAlarmActivated
     */
    public void setMuteAlarmActivated(boolean isMuteAlarmActivated) {
        this.isMuteAlarmActivated = isMuteAlarmActivated;
    }

    public int getRepeatAlarm() {
        return repeatAlarm;
    }

    public void setRepeatAlarm(int repeatAlarm) {
        this.repeatAlarm = repeatAlarm;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#equals()
         */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmSettingsModel that = (AlarmSettingsModel) o;

        if (alarmVolume != that.alarmVolume) return false;
        if (isAlarmActivated != that.isAlarmActivated) return false;
        if (isMuteAlarmActivated != that.isMuteAlarmActivated) return false;
        if (isNotificationLightActivated != that.isNotificationLightActivated) return false;
        if (isVibrationActivated != that.isVibrationActivated) return false;
        return notificationLightColor == that.notificationLightColor;

    }

    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        int result = (isAlarmActivated ? 1 : 0);
        result = 31 * result + alarmVolume;
        result = 31 * result + (isVibrationActivated ? 1 : 0);
        result = 31 * result + (isNotificationLightActivated ? 1 : 0);
        result = 31 * result + notificationLightColor;
        result = 31 * result + (isMuteAlarmActivated ? 1 : 0);
        return result;
    }

    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return "AlarmSettings{" +
                "isAlarmActivated=" + isAlarmActivated +
                ", alarmVolume=" + alarmVolume +
                ", isVibrationActivated=" + isVibrationActivated +
                ", isNotificationLightActivated=" + isNotificationLightActivated +
                ", notificationLightColor=" + notificationLightColor +
                ", isMuteAlarmActivated=" + isMuteAlarmActivated +
                '}';
    }
}
