package rieger.alarmsmsapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * This class represents a message, which was received by the app.
 * The saved Message can be used for statistical things and other.
 *
 * Created by Sebastian on 27.03.2016.
 */
public class Message implements Serializable{

    /**
     * the sender of the message
     */
    private String sender;

    /**
     * the message content
     */
    private String message;

    /**
     * the time stamp on which the message was received
     */
    private long timeStamp;

    /**
     * the day the message was received
     */
    private int day;

    /**
     * the month the message was received
     */
    private int month;

    /**
     * the year the message was received
     */
    private int year;

    /**
     * the rule with which the message matches
     */
    private String matchingRuleName;

    private String dayName = "";

    public Message() {
    }

    public Message(@NonNull String sender, @NonNull String message, @NonNull long timeStamp, @NonNull int day, @NonNull int month, @NonNull int year, @NonNull String matchingRuleName) {
        this.sender = sender;
        this.message = message;
        this.timeStamp = timeStamp;
        this.day = day;
        this.month = month;
        this.year = year;
        this.matchingRuleName = matchingRuleName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(@NonNull String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(@NonNull long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getDay() {
        return day;
    }

    public void setDay(@NonNull int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(@NonNull int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(@NonNull int year) {
        this.year = year;
    }

    public String getMatchingRuleName() {
        return matchingRuleName;
    }

    public void setMatchingRuleName(String matchingRuleName) {
        this.matchingRuleName = matchingRuleName;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        if (getTimeStamp() != message1.getTimeStamp()) return false;
        if (getDay() != message1.getDay()) return false;
        if (getMonth() != message1.getMonth()) return false;
        if (getYear() != message1.getYear()) return false;
        if (!getSender().equals(message1.getSender())) return false;
        if (!getMessage().equals(message1.getMessage())) return false;
        return getMatchingRuleName().equals(message1.getMatchingRuleName());

    }

    @Override
    public int hashCode() {
        int result = getSender().hashCode();
        result = 31 * result + getMessage().hashCode();
        result = 31 * result + (int) (getTimeStamp() ^ (getTimeStamp() >>> 32));
        result = 31 * result + getDay();
        result = 31 * result + getMonth();
        result = 31 * result + getYear();
        result = 31 * result + getMatchingRuleName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", matchingRuleName='" + matchingRuleName + '\'' +
                '}';
    }
}
