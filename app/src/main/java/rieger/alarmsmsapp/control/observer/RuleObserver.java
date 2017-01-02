package rieger.alarmsmsapp.control.observer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.rules.EMailRule;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class is inspired by the Observer-Pattern,
 * but it saves instances of a Rule in the file system and not in memory.
 * It is necessary, because the programmer has under android no control when the app is closed
 * and also he has no control over the start point of the app.
 *
 * So all methods are static and can called from everywhere.
 *
 * This class saves the instances of the class {@link Rule} in a JSON based file.
 * Also the class can remove or change a file from a {@link rieger.alarmsmsapp.model.rules.Rule}.
 * @author sebastian
 *
 */

public class RuleObserver {

	public static final String LOG_TAG = "RuleObserver";

    /**
     * This method saves a rule to the file system.
     * If the file is already exists, so the new state is saved over the old one.
     * The method saves instances of {@link rieger.alarmsmsapp.model.rules.EMailRule} and {@link rieger.alarmsmsapp.model.rules.SMSRule}
     * in different folders.
     * @param rule the instance of a {@link rieger.alarmsmsapp.model.rules.Rule} which should be saved.
     */
	public static void saveRuleToFileSystem(Rule rule){
		ObjectMapper mapper = new ObjectMapper();

		if(rule instanceof SMSRule){
			File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
			File smsRuleFile = new File(smsRuleDirectory, rule.getRuleName());
			try {
				mapper.writeValue(smsRuleFile, rule);
			} catch (IOException e) {
				Log.e(AppConstants.DEBUG_TAG, "Error while writing rule to file system",  e);
			}
		}
		if (rule instanceof EMailRule) {
			File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MAIL_RULES, Context.MODE_PRIVATE);
			File smsRuleFile = new File(smsRuleDirectory, rule.getRuleName());
			try {
				mapper.writeValue(smsRuleFile, rule);
			} catch (IOException e) {
				Log.e(AppConstants.DEBUG_TAG, "Error while writing rule to file system",  e);
			}
		}

	}

	public static void saveRule(Rule rule, Context context){
		DataSource db = new DataSource(context);
		db.saveRule(rule);
	}

    public static void saveSMSRuleToFileSystem(String rule){
        ObjectMapper mapper = new ObjectMapper();


            File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
            String splitedRule[] = rule.split("\"ruleName\":\"");
            String ruleName[] = splitedRule[1].split("\",\"");
            File smsRuleFile = new File(smsRuleDirectory, ruleName[0]);
            try {
                mapper.writeValue(smsRuleFile, mapper.readValue(rule, SMSRule.class));
            } catch (IOException e) {
                Log.e(AppConstants.DEBUG_TAG, "Error while writing rule to file system",  e);
            }


    }

    /**
     * This method reads all rule files from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.rules.Rule}.
     * @return a list of all rules which are currently saved in the file system.
     */
	public static List<Rule> readAllRulesFromFileSystem() {

		File smsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
		File[] listOfSMSFiles = smsFolder.listFiles();

		File MailFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MAIL_RULES, Context.MODE_PRIVATE);
		File[] listOfMailFiles = MailFolder.listFiles();

		ObjectMapper mapper = new ObjectMapper();

		List<Rule> ruleList = new ArrayList<Rule>();

		for (File fileWithRule : listOfSMSFiles) {

			if (fileWithRule.isFile()) {
				try {
					ruleList.add(mapper.readValue(fileWithRule, SMSRule.class));
				} catch (IOException e) {
					FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Can not read Files.");
					FirebaseCrash.report(e);
                    Toast.makeText(CreateContextForResource.getContext(),CreateContextForResource.getStringFromID(R.string.can_not_read_rule), Toast.LENGTH_LONG).show();
				}
			}
		}
		for (File fileWithRule : listOfMailFiles) {

			if (fileWithRule.isFile()) {
				try {
					ruleList.add(mapper.readValue(fileWithRule, EMailRule.class));
				} catch (IOException e) {
					FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Can not read Files.");
					FirebaseCrash.report(e);
                    Toast.makeText(CreateContextForResource.getContext(),CreateContextForResource.getStringFromID(R.string.can_not_read_rule), Toast.LENGTH_LONG).show();
				}
			}
		}

		return ruleList;
	}

	public static List<Rule> readAllRules(Context context){
		DataSource db = new DataSource(context);
		return db.getAllRules();
	}

    /**
     * This method delete a rule from the file system. If the rule not exists so this method gives a
     * log message but throw not an exception.
     * @param rule the rule which should be saved
     */
	public static void deleteRuleFromFilesystem(Rule rule){

		if (rule instanceof SMSRule) {

			String ruleFilePath = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE) + "/" + rule.getRuleName();

			File ruleFile = new File( ruleFilePath );
			if(ruleFile.delete()){
			}else{
				FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Delete operation is failed.");
			}
		}
		if (rule instanceof EMailRule) {
			String ruleFilePath = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MAIL_RULES, Context.MODE_PRIVATE) + "/" + rule.getRuleName();

			File ruleFile = new File( ruleFilePath );
			if(ruleFile.delete()){
			}else{
				FirebaseCrash.logcat(Log.ERROR, LOG_TAG, "Delete operation is failed.");
				Log.e(AppConstants.DEBUG_TAG, "Delete operation is failed.");
			}
		}
	}

	public static void deleteRule(Rule rule, Context context){
		DataSource db = new DataSource(context);
		db.deleteRule(rule);
	}

    /**
     * This method reads all sms rule files from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.rules.Rule}.
     * @return a list of all sms rules which are currently saved in the file system.
     */
    public static List<Rule> readAllSMSRulesFromFileSystem() {

        File smsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
        File[] listOfSMSFiles = smsFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        List<Rule> ruleList = new ArrayList<Rule>();

        for (File fileWithRule : listOfSMSFiles) {

            if (fileWithRule.isFile()) {
                try {
                    ruleList.add(mapper.readValue(fileWithRule, SMSRule.class));
                } catch (IOException e) {
                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
                }
            }
        }
        return ruleList;
    }

    /**
     * This method reads all rule files from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.rules.Rule}.
     * @return a list of all rules which are currently saved in the file system.
     */
    public static Uri getUriFromSMSRule(String ruleName) {

        File smsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
        File[] listOfSMSFiles = smsFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        List<Rule> ruleList = new ArrayList<Rule>();

        for (File fileWithRule : listOfSMSFiles) {

            if (fileWithRule.isFile()) {
                try {
                    ruleList.add(mapper.readValue(fileWithRule, SMSRule.class));
                    if(ruleName.equals(fileWithRule.getName())){

                        return Uri.fromFile(fileWithRule);
                    }
                } catch (IOException e) {
                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
                }
            }
        }
        return null;
    }
}
