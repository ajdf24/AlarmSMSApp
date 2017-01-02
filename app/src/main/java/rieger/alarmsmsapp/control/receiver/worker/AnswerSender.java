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
        if (departmentSettings != null && departmentSettings.getAddress() != null && !departmentSettings.getAddress().isEmpty()) {
            results = DistanceCalculator.calculateDistance(departmentSettings.getAddress());
            if(results[0] == 0.0){
                return;
            }
            for (Rule rule : matchingRules) {
                System.out.println("muss");
                if (rule.getMessage() != null && rule.getReceivers() != null && results[0] / 1000 > rule.getDistance()) {
                    System.out.println("Hier");
                    for (String receiver : rule.getReceivers()) {
                        System.out.println("test");
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(receiver, null, rule.getMessage(), null, null);
                    }
                }
            }
        }
    }

}
