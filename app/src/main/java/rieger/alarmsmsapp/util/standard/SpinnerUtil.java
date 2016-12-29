package rieger.alarmsmsapp.util.standard;

import android.widget.Spinner;

/**
 * Created by sebastian on 29.12.16.
 */
public class SpinnerUtil {

    /**
     * get a spinner index by a label string
     * @param spinner the spinner
     * @param myString a string which is used in the spinner
     * @return the spinner index
     */
    public static int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
