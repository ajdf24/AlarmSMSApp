package rieger.alarmsmsapp.control.observer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by Sebastian on 27.03.2016.
 */
public class MessageObserver {

    /**
     * This method saves a message to the file system.
     * If the file is already exists, so the new state is saved over the old one.
     * The method saves instances of {@link rieger.alarmsmsapp.model.Message}.
     * @param message the instance of a {@link rieger.alarmsmsapp.model.Message} which should be saved.
     */
    public static void saveMessageToFileSystem(Message message){
        ObjectMapper mapper = new ObjectMapper();

        File smsRuleDirectory = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MESSAGES, Context.MODE_PRIVATE);
           File messageFile = new File(smsRuleDirectory, message.getTimeStamp() + "");
            try {
                mapper.writeValue(messageFile, message);
            } catch (IOException e) {
                Log.e(AppConstants.DEBUG_TAG, "Error while writing rule to file system", e);
            }


    }

    /**
     * This method reads all messages files from the file system and convert these to instances of the class
     * {@link rieger.alarmsmsapp.model.Message}.
     * @return a list of all messages which are currently saved in the file system.
     */
    public static List<Message> readAllMessagesFromFileSystem() {

        File messageFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MESSAGES, Context.MODE_PRIVATE);
        File[] listOfMessageFiles = messageFolder.listFiles();

        ObjectMapper mapper = new ObjectMapper();

        List<Message> messageList = new ArrayList<Message>();

        for (File fileWithMessage : listOfMessageFiles) {

            if (fileWithMessage.isFile()) {
                try {
                    messageList.add(mapper.readValue(fileWithMessage, Message.class));
                } catch (IOException e) {
                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
                    Toast.makeText(CreateContextForResource.getContext(), CreateContextForResource.getStringFromID(R.string.can_not_read_rule), Toast.LENGTH_LONG).show();
                }
            }
        }

        return messageList;
    }

//    /**
//     * This method delete a rule from the file system. If the rule not exists so this method gives a
//     * log message but throw not an exception.
//     * @param rule the rule which should be saved
//     */
//    public static void deleteRuleFromFilesystem(Rule rule){
//
//        if (rule instanceof SMSRule) {
//
//            String ruleFilePath = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE) + "/" + rule.getRuleName();
//
//            File ruleFile = new File( ruleFilePath );
//            if(ruleFile.delete()){
//            }else{
//                Log.e(AppConstants.DEBUG_TAG, "Delete operation is failed.");
//            }
//        }
//        if (rule instanceof EMailRule) {
//            String ruleFilePath = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_MAIL_RULES, Context.MODE_PRIVATE) + "/" + rule.getRuleName();
//
//            File ruleFile = new File( ruleFilePath );
//            if(ruleFile.delete()){
//            }else{
//                Log.e(AppConstants.DEBUG_TAG, "Delete operation is failed.");
//            }
//        }
//    }

//    /**
//     * This method reads all rule files from the file system and convert these to instances of the class
//     * {@link rieger.alarmsmsapp.model.rules.Rule}.
//     * @return a list of all rules which are currently saved in the file system.
//     */
//    public static Uri getUriFromSMSRule(String ruleName) {
//
//        File smsFolder = CreateContextForResource.getContext().getDir(AppConstants.StringsForObserver.DIRECTORY_NAME_SMS_RULES, Context.MODE_PRIVATE);
//        File[] listOfSMSFiles = smsFolder.listFiles();
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        List<Rule> ruleList = new ArrayList<Rule>();
//
//        for (File fileWithRule : listOfSMSFiles) {
//
//            if (fileWithRule.isFile()) {
//                try {
//                    ruleList.add(mapper.readValue(fileWithRule, SMSRule.class));
//                    if(ruleName.equals(fileWithRule.getName())){
//
//                        return Uri.fromFile(fileWithRule);
//                    }
//                } catch (IOException e) {
//                    Log.e(AppConstants.DEBUG_TAG, "Can not read Files.", e);
//                }
//            }
//        }
//        return null;
//    }
}
