package rieger.alarmsmsapp.model.rules;

import android.content.res.Resources;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeModel {

    private int id;
    private Days day;
    private int startTimeMinutes = -1;
    private int endTimeMinutes = -1;
    private int startTimeHours = -1;
    private int endTimeHours = -1;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean checkDayForAlarmTime(int dayNumber){

        switch (day){
            case SUNDAY:
                if(dayNumber == 1){
                    return true;
                }
                break;
            case MONDAY:
                if(dayNumber == 2){
                    return true;
                }
                break;
            case TUESDAY:
                if(dayNumber == 3){
                    return true;
                }
                break;
            case WEDNESDAY:
                if(dayNumber == 4){
                    return true;
                }
                break;
            case THURSDAY:
                if(dayNumber == 5){
                    return true;
                }
                break;
            case FRIDAY:
                if(dayNumber == 6){
                    return true;
                }
                break;
            case SATURDAY:
                if(dayNumber == 7){
                    return true;
                }
                break;
            case MONDAY_TO_SUNDAY:
                return true;
            case MONDAY_TO_FRIDAY:
                if(dayNumber >= 2 && dayNumber <= 6){
                    return true;
                }
                break;
            case FRIDAY_TO_SUNDAY:
                if(dayNumber >= 6){
                    return true;
                }
                if(dayNumber == 1){
                    return true;
                }
                break;
            case SATURDAY_TO_SUNDAY:
                if(dayNumber == 7){
                    return true;
                }
                if(dayNumber == 1){
                    return true;
                }
                break;
        }

        return false;
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

    public static String getStringForDay(Days days){
        Resources res = CreateContextForResource.getContext().getResources();
        String[] daysArray = res.getStringArray(R.array.days_array);

        String day = "";

        switch (days){
            case SUNDAY:
                day = daysArray[0];
                break;
            case MONDAY:
                day = daysArray[1];
                break;
            case TUESDAY:
                day = daysArray[2];
                break;
            case WEDNESDAY:
                day = daysArray[3];
                break;
            case THURSDAY:
                day = daysArray[4];
                break;
            case FRIDAY:
                day = daysArray[5];
                break;
            case SATURDAY:
                day = daysArray[6];
                break;
            case MONDAY_TO_SUNDAY:
                day = daysArray[7];
                break;
            case MONDAY_TO_FRIDAY:
                day = daysArray[8];
                break;
            case FRIDAY_TO_SUNDAY:
                day = daysArray[9];
                break;
            case SATURDAY_TO_SUNDAY:
                day = daysArray[10];
                break;
        }
        return day;
    }

}
