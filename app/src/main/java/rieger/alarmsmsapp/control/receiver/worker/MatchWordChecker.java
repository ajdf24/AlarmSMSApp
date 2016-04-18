package rieger.alarmsmsapp.control.receiver.worker;

import java.util.ArrayList;
import java.util.List;

import rieger.alarmsmsapp.model.rules.Rule;

/**
 * This class contains methods for checking the match words of {@link rieger.alarmsmsapp.model.rules.Rule}.
 * Created by sebastian on 14.03.15.
 */
public class MatchWordChecker {
    /**
     * Checks the words of the rules from the list <code>matchingRules</code>.
     * The method removes not matching
     * @return <code>true</code> if a rule matches.
     */
    public static boolean checkIfWordsMatch(String messageBody, List<Rule> matchingRules){

        boolean containsAllOccurredWords = true;
        boolean dontContainsAllOccurredWords = true;

        for (final Rule rule : matchingRules){
            if (rule.getOccurredWords() != null && !rule.getOccurredWords().isEmpty()) {
                String occurredWords[] = rule.getOccurredWords().split(" ");
                for (String occurredWord : occurredWords) {
                    if (!messageBody.toLowerCase().contains(occurredWord.toLowerCase())) {
                        containsAllOccurredWords = false;
                    }
                }
            }
        }

        if (containsAllOccurredWords){
            for (final Rule rule : matchingRules){
                if (rule.getNotOccurredWords() != null && !rule.getNotOccurredWords().isEmpty()) {

                    String notOccurredWords[] = rule.getNotOccurredWords().split(" ");
                    for (String notOccurredWord : notOccurredWords) {
                        if (messageBody.toLowerCase().contains(notOccurredWord.toLowerCase())) {
                            dontContainsAllOccurredWords = false;
                        }
                    }
                }
            }
        }else{
            return false;
        }

        return dontContainsAllOccurredWords;

    }
}
