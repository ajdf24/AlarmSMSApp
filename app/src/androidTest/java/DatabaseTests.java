import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.util.List;

import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.rules.Sound;
import rieger.alarmsmsapp.model.rules.AnswerBundle;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.model.rules.SMSRule;

/**
 * Created by sebastian on 14.08.16.
 */
public class DatabaseTests extends AndroidTestCase {

    private DataSource db;
    private Rule rule;
    private DepartmentSettingsModel department;
    private AlarmSettingsModel alarm;
    private Message message;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

        db = new DataSource(context);

        department = new DepartmentSettingsModel();
        department.setAddress("Adresse Test Department");

        rule = new SMSRule();
        rule.setRuleName("TEST");

        alarm = new AlarmSettingsModel();
        alarm.setAlarmActivated(true);

        message = new Message();
        message.setSender("TESTSender");
    }

    public void testAddEntry() {
        assertEquals(rule, db.createRule(rule));
    }

    public void testEmptyRules() {
        List<Rule> rules = db.getAllRules();
        assertEquals(rules.size(), 0);
    }

    public void testDeleteRule() {
        db.createRule(rule);
        db.deleteRule(rule);

        List<Rule> rules = db.getAllRules();
        assertEquals(rules.size(), 0);
    }

    public void testNotSameRule() {

        assertNotSame(rule, db.createRule(rule));
    }

    public void testCorrectRuleName() {
        assertEquals(rule, db.createRule(rule));
    }

    public void testCorrectRuleSender() {
        rule.setSender("+1234*");
        assertEquals(rule.getRuleName(), db.createRule(rule).getRuleName());
    }

    public void testCorrectOccurredWords() {
        rule.setOccurredWords("Das Ist ein Test!");
        assertEquals(rule.getOccurredWords(), db.createRule(rule).getOccurredWords());
    }

    public void testCorrectNotOccurredWord() {
        rule.setNotOccurredWords("Dat ist ein anderer Test!");
        assertEquals(rule.getNotOccurredWords(), db.createRule(rule).getNotOccurredWords());
    }

    public void testCorrectSound() {
        String name = "Name";
        String uri = "uri://Test";
        boolean isInternalFalse = false;
        Sound sound = new Sound(name, uri, isInternalFalse);
        rule.setAlarmSoundUri(sound);

        assertEquals(rule.getAlarmSoundUri(), db.createRule(rule).getAlarmSoundUri());

        boolean isInternalTrue = true;
        sound = new Sound(name, uri, isInternalTrue);
        rule.setAlarmSoundUri(sound);

        assertEquals(rule.getAlarmSoundUri(), db.createRule(rule).getAlarmSoundUri());
    }

    public void testCorrectAutoAnswer() {

        String receiver = "+5543**";
        String message = "Das ist eine Nachricht";
        int distance = 500;

        AnswerBundle answerBundle = new AnswerBundle(receiver, message, distance);

        rule.setAutomaticallyAnswer(answerBundle);

        assertEquals(rule.getAutomaticallyAnswer(), db.createRule(rule).getAutomaticallyAnswer());
    }

    public void testCorrectTwitterMessage() {

        String message = "Message für Twitter";

        rule.setMessageToPostOnTwitter(message);

        assertEquals(rule.getMessageToPostOnTwitter(), db.createRule(rule).getMessageToPostOnTwitter());
    }

    public void testCorrectNavigationTarget() {

        String target = "Das ist, kein valides Target, aber was solls";

        rule.setNavigationTarget(target);

        assertEquals(rule.getNavigationTarget(), db.createRule(rule).getNavigationTarget());
    }

    public void testCorrectReadThisMessage() {

        boolean isNo = false;

        rule.setReadThisMessage(isNo);

        assertEquals(rule.isReadThisMessage(), db.createRule(rule).isReadThisMessage());


        boolean isYes = true;

        rule.setReadThisMessage(isYes);

        assertEquals(rule.isReadThisMessage(), db.createRule(rule).isReadThisMessage());
    }

    public void testCorrectReadOtherMessages() {

        boolean isNo = false;

        rule.setReadOtherMessages(isNo);

        assertEquals(rule.isReadOtherMessages(), db.createRule(rule).isReadOtherMessages());


        boolean isYes = true;

        rule.setReadOtherMessages(isYes);

        assertEquals(rule.isReadOtherMessages(), db.createRule(rule).isReadOtherMessages());
    }

    public void testCorrectAddMessageToTwitterPost() {

        boolean isNo = false;

        rule.setAddMessageToTwitterPost(isNo);

        assertEquals(rule.isAddMessageToTwitterPost(), db.createRule(rule).isAddMessageToTwitterPost());


        boolean isYes = true;

        rule.setAddMessageToTwitterPost(isYes);

        assertEquals(rule.isAddMessageToTwitterPost(), db.createRule(rule).isAddMessageToTwitterPost());
    }

    public void testCorrectActiveLight() {

        boolean isNo = false;

        rule.setActivateLight(isNo);

        assertEquals(rule.isActivateLight(), db.createRule(rule).isActivateLight());


        boolean isYes = true;

        rule.setActivateLight(isYes);

        assertEquals(rule.isActivateLight(), db.createRule(rule).isActivateLight());
    }

    public void testCorrectLightOnlyWhenDark() {

        boolean isNo = false;

        rule.setActivateLightOnlyWhenDark(isNo);

        assertEquals(rule.isActivateLightOnlyWhenDark(), db.createRule(rule).isActivateLightOnlyWhenDark());


        boolean isYes = true;

        rule.setActivateLightOnlyWhenDark(isYes);

        assertEquals(rule.isActivateLightOnlyWhenDark(), db.createRule(rule).isActivateLightOnlyWhenDark());
    }

    public void testCorrectLightTime() {

        int lightTime = 500;

        rule.setLightTime(lightTime);

        assertEquals(rule.getLightTime(), db.createRule(rule).getLightTime());
    }

    public void testAddDepartment() {

        assertEquals(department.getAddress(), db.createDepartment(department).getAddress());
    }

    public void testEmptyDepartments() {

        assertEquals(db.getAllDepartments().size(), 0);
    }

    public void testDeleteDepartment() {

        db.createDepartment(department);

        db.deleteDepartment(department);

        assertEquals(db.getAllDepartments().size(), 0);
    }

    public void testNotSameDepartment() {

        assertNotSame(department, db.createDepartment(department));
    }


    public void testAddAlarm() {

        assertNotNull(db.createAlarm(alarm));
    }

    public void testNotSameAlarm() {

        assertNotSame(alarm, db.createAlarm(alarm));
    }

    public void testDeleteAlarm() {

        db.createAlarm(alarm);
        db.deleteAlarm();

        assertNull(db.getAlarm());
    }

    public void testEmptyTableRules(){

        assertEquals(db.getAllRules().size(), 0);
    }

    public void testEmptyTableDepartments(){

        assertEquals(db.getAllDepartments().size(), 0);
    }

    public void testAddMessage(){

        db.createMessage(message);

        assertEquals(db.getAllMessages().size(), 1);
    }

    public void testEmptyTable(){

        assertEquals(db.getAllMessages().size(), 0);
    }

    public void testNotSameMessage(){

        assertNotSame(message, db.createMessage(message));
    }

    public void testMessageAddCorrectSender(){

        message.setSender("Blubber");

        assertEquals(message.getSender(), db.createMessage(message).getSender());
    }

    public void testMessageAddCorrectMessage(){

        message.setMessage("Test Nachricht");

        assertEquals(message.getMessage(), db.createMessage(message).getMessage());
    }

    public void testMessageAddCorrectTimeStamp(){

        message.setTimeStamp(1234567890);

        assertEquals(message.getTimeStamp(), db.createMessage(message).getTimeStamp());
    }

    public void testMessageAddCorrectDay(){

        message.setDay(1);

        assertEquals(message.getDay(), db.createMessage(message).getDay());
    }

    public void testMessageAddCorrectMonth(){

        message.setMonth(5);

        assertEquals(message.getMonth(), db.createMessage(message).getMonth());
    }

    public void testMessageAddCorrectYear(){

        message.setYear(2016);

        assertEquals(message.getYear(), db.createMessage(message).getYear());
    }

    public void testMessageAddCorrectMatchingRuleName(){

        message.setMatchingRuleName("Matching Rule NAME");

        assertEquals(message.getMatchingRuleName(), db.createMessage(message).getMatchingRuleName());
    }

    public void testMessageAddCorrectMatchingDayName(){

        message.setDayName("Mittwoch");

        assertEquals(message.getDayName(), db.createMessage(message).getDayName());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
