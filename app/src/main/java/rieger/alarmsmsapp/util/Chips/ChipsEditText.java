package rieger.alarmsmsapp.util.Chips;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Created by sebastian on 30.12.16.
 */

public class ChipsEditText extends AppCompatEditText {

    private InputConnectionWrapperInterface mInputConnectionWrapperInterface;

    public ChipsEditText(Context context, InputConnectionWrapperInterface inputConnectionWrapperInterface) {
        super(context);
        this.mInputConnectionWrapperInterface = inputConnectionWrapperInterface;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (mInputConnectionWrapperInterface != null) {
            return mInputConnectionWrapperInterface.getInputConnection(super.onCreateInputConnection(outAttrs));
        }

        return super.onCreateInputConnection(outAttrs);
    }

    public interface InputConnectionWrapperInterface {
        InputConnection getInputConnection(InputConnection target);
    }
}