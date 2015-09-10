package rieger.alarmsmsapp.util.socialnetworks;

/**
 * This class contains different methods for creating dynamic Strings,
 * which could be used in the code.
 *
 * Created by sebastian on 23.03.15.
 */
public class StringCreator {

    /**
     * This method create a String, which can be used as character counter.
     * This use case is possible e.g. in Twitter posts.
     * @param maximalCharacters the maximal characters
     * @param remainingCharacters the remaining characters
     * @return return a String in the format <code>remainingCharacters/maximalCharacters</code>
     */
    public static String getCharacterCounterString(int maximalCharacters, int remainingCharacters){
        return remainingCharacters + "/" + maximalCharacters;
    }
}
