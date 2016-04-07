package rieger.alarmsmsapp.control.factory;

import android.content.Context;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import rieger.alarmsmsapp.model.RuleType;
import rieger.alarmsmsapp.model.Sound;
import rieger.alarmsmsapp.model.rules.AnswerBundle;
import rieger.alarmsmsapp.model.rules.EMailRule;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * Created by sebastian on 05.04.16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CreateContextForResource.class})
public class RuleCreatorTest extends AndroidTestCase{

    String ruleName = "Testname";

    Rule smsRule;
    Rule emailRule;

    @Mock
    Sound sound;

    @Mock
    AppConstants appConstants;

    @Mock
    Context context;

    @Mock
    AnswerBundle answerBundle;

    @Before
    public void createInitialRule(){

        PowerMockito.mockStatic(CreateContextForResource.class);
        PowerMockito.when(CreateContextForResource.getContext()).thenReturn(context);

        assertThat(smsRule, is(nullValue()));
        assertThat(emailRule, is(nullValue()));

        smsRule = RuleCreator.createRule(ruleName, RuleType.SMS_RULE);
        emailRule = RuleCreator.createRule(ruleName, RuleType.EMAIL_RULE);

        assertThat(smsRule, is(notNullValue()));
        assertThat(emailRule, is(notNullValue()));
    }

    @Test
    public void createRule_NameTest(){
        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getRuleName(), is(ruleName));
    }

    @Test
    public void createRule_Type_SMS_Test(){
        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule, instanceOf(SMSRule.class));
    }

    @Test
    public void createRule_Type_Mail_Test(){
        assertThat(emailRule, is(notNullValue()));

        assertThat(emailRule, instanceOf(EMailRule.class));
    }

    @Test
    public void changeSender_Test(){
        String indentifier = "Test";

        RuleCreator.changeSender(smsRule, indentifier);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getSender(), is(indentifier));
    }

    @Test
    public void changeWords_Test(){
        String occuredWords = "Taucht auf";
        String notOccuredWords = "Taucht nicht auf";

        RuleCreator.changeWords(smsRule, occuredWords, notOccuredWords);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getOccurredWords(), is(occuredWords));
        assertThat(smsRule.getNotOccurredWords(), is(notOccuredWords));
    }

    @Test
    public void changeSound_Test(){

        RuleCreator.changeAlarmSound(smsRule, sound);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getAlarmSound(), is(sound));
    }

    @Test
    public void changeAutomaticallyAnswer_Test(){

        RuleCreator.changeAutomaticallyAnswer(smsRule, answerBundle);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getAutomaticallyAnswer(), is(answerBundle));
    }

    @Test
    public void changeFacebookPost_Test(){

        String message = "Test";

        boolean addMessage = true;

        RuleCreator.changeFacebookPost(smsRule, message, addMessage);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getMessageToPostOnTwitter(), is(message));

        assertThat(smsRule.isAddMessageToTwitterPost(), is(addMessage));
    }

    @Test
    public void changeNavigationTarget_Test(){

        String target = "Test";

        RuleCreator.changeNavigationTarget(smsRule, target);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.getNavigationTarget(), is(target));
    }

    @Test
    public void changeReadingSettings_Test(){

        boolean readThis = true;

        boolean readOther = true;

        RuleCreator.changeReadingSettings(smsRule, readThis, readOther);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.isReadThisMessage(), is(readThis));

        assertThat(smsRule.isReadOtherMessages(), is(readOther));
    }

    @Test
    public void changeActive_Test(){

        boolean active = true;

        RuleCreator.changeActive(smsRule, active);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.isActive(), is(active));
    }

    @Test
    public void changeLightSettings_Test(){

        boolean activeLight = true;

        int lightTime = 100;

        boolean onlyDark = true;

        RuleCreator.changeLightSettings(smsRule, activeLight, lightTime, onlyDark);

        assertThat(smsRule, is(notNullValue()));

        assertThat(smsRule.isActivateLight(), is(activeLight));

        assertThat(smsRule.getLightTime(), is(lightTime));

        assertThat(smsRule.isActivateLightOnlyWhenDark(), is(onlyDark));
    }


}
