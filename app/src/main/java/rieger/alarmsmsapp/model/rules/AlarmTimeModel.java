package rieger.alarmsmsapp.model.rules;

/**
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeModel {

    private Days day;
    private int startTimeMinutes;
    private int endTimeMinutes;
    private int startTimeHours;
    private int endTimeHours;

    public Days getDay() {
        return day;
    }

    public void setDay(Days day) {
        this.day = day;
    }

    public int getStartTimeMinutes() {
        return startTimeMinutes;
    }

    public void setStartTimeMinutes(int startTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
    }

    public int getEndTimeMinutes() {
        return endTimeMinutes;
    }

    public void setEndTimeMinutes(int endTimeMinutes) {
        this.endTimeMinutes = endTimeMinutes;
    }

    public int getStartTimeHours() {
        return startTimeHours;
    }

    public void setStartTimeHours(int startTimeHours) {
        this.startTimeHours = startTimeHours;
    }

    public int getEndTimeHours() {
        return endTimeHours;
    }

    public void setEndTimeHours(int endTimeHours) {
        this.endTimeHours = endTimeHours;
    }

    public enum Days {
        MONDAY_TO_SUNDAY, MONDAY_TO_FRIDAY, FRIDAY_TO_SUNDAY, SATURDAY_TO_SUNDAY,
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY;
    }

    public static int daysToInt(Days day){
        int dayInt = -1;

        switch (day){
            case SUNDAY:
                dayInt = 0;
                break;
            case MONDAY:
                dayInt = 1;
                break;
            case TUESDAY:
                dayInt = 2;
                break;
            case WEDNESDAY:
                dayInt = 3;
                break;
            case THURSDAY:
                dayInt = 4;
                break;
            case FRIDAY:
                dayInt = 5;
                break;
            case SATURDAY:
                dayInt = 6;
                break;
            case MONDAY_TO_SUNDAY:
                dayInt = 7;
                break;
            case MONDAY_TO_FRIDAY:
                dayInt = 8;
                break;
            case FRIDAY_TO_SUNDAY:
                dayInt = 9;
                break;
            case SATURDAY_TO_SUNDAY:
                dayInt = 10;
                break;
        }
        return dayInt;
    }

    public static Days intToDays(int day){
        Days dayInt = null;

        switch (day){
            case 0:
                dayInt = Days.SUNDAY;
                break;
            case 1:
                dayInt = Days.MONDAY;
                break;
            case 2:
                dayInt = Days.TUESDAY;
                break;
            case 3:
                dayInt = Days.WEDNESDAY;
                break;
            case 4:
                dayInt = Days.THURSDAY;
                break;
            case 5:
                dayInt = Days.FRIDAY;
                break;
            case 6:
                dayInt = Days.SATURDAY;
                break;
            case 7:
                dayInt = Days.MONDAY_TO_SUNDAY;
                break;
            case 8:
                dayInt = Days.MONDAY_TO_FRIDAY;
                break;
            case 9:
                dayInt = Days.FRIDAY_TO_SUNDAY;
                break;
            case 10:
                dayInt = Days.SATURDAY_TO_SUNDAY;
                break;
        }
        return dayInt;
    }

    public static String timeToString(int hourOfDay, int minute){
        StringBuffer timeString = new StringBuffer();
        if(hourOfDay < 10){
            timeString.append("0" + hourOfDay) ;
        }else {
            timeString.append(hourOfDay);
        }
        timeString.append(":");
        if(minute < 10){
            timeString.append("0" + minute);
        }else {
            timeString.append(minute);
        }
        return timeString.toString();
    }

}
