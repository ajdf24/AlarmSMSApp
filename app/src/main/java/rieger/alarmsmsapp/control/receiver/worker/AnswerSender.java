package rieger.alarmsmsapp.control.receiver.worker;

import android.telephony.SmsManager;

import java.util.List;

import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.googleplaces.DistanceCalculator;

/**
 * This class contains methods to answer to a received message.
 * Created by sebastian on 14.03.15.
 */
public class AnswerSender {

    /**
     * This method send the automatically answer from the rule as SMS,
     * if the current distance bigger than the defined distance.
     * <br>
     *     <b>Note</b>: This method needs the permission <code>android.permission.SEND_SMS</code> in the manifest.
     *
     */
    public static void sendAnswerAsSMS(List<Rule> matchingRules, DepartmentSettingsModel departmentSettings) {


        float[] results;
        if (departmentSettings != null && departmentSettings.getAddress() !=null && !departmentSettings.getAddress().isEmpty()) {
            results = DistanceCalculator.calculateDistance(departmentSettings.getAddress());

            for (Rule rule : matchingRules) {
                if (rule.getMessage() != null && rule.getReceiver() != null && !rule.getReceiver().isEmpty() && results[0] / 1000 > rule.getDistance()) {

                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(rule.getReceiver(), null, rule.getMessage(), null, null);
                }
            }
        }
    }

}
