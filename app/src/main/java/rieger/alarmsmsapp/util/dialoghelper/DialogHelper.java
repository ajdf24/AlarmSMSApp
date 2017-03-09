package rieger.alarmsmsapp.util.dialoghelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


/**
 * Helper class for creating dialogs
 * Created by sebastian on 09.03.17.
 */

public class DialogHelper {

    public static void createHelpDialog(@NonNull Context context, int title, int text, int buttonText){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(buttonText, null);

        AlertDialog helpDialog = alertDialogBuilder.create();
        helpDialog.show();
    }

    public static void createHelpDialog(@NonNull Context context, @NonNull String title, @NonNull String text, @NonNull String buttonText){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title)
                .setMessage(text)
                .setPositiveButton(buttonText, null);

        AlertDialog helpDialog = alertDialogBuilder.create();
        helpDialog.show();
    }
}
