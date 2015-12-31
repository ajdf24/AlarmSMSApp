package rieger.alarmsmsapp.control.receiver.worker;

/**
 * This class is a singelton and contains the timestamp when the last alarm message was received.
 *
 * Created by sebastian on 10.03.15.
 */
public class ReadingOutTimestamp {

    private static ReadingOutTimestamp instance = null;

    private long readingStartTime = 0;

    /**
     * This method create and return the instance of the singelton.
     * @return a instance of {@link rieger.alarmsmsapp.control.receiver.worker.ReadingOutTimestamp}
     */
    public static ReadingOutTimestamp getInstance() {
        if(instance == null) {
            instance = new ReadingOutTimestamp();
        }
        return instance;
    }

    /**
     * This method gives the current timestamp.
     * @return the current timestamp
     */
    public long getReadingStartTime() {
        return readingStartTime;
    }

    /**
     * This method sets the actual timestamp.
     * @param readingStartTime the current timestamp as long value
     */
    public void setReadingStartTime(long readingStartTime) {
        this.readingStartTime = readingStartTime;
    }
}
