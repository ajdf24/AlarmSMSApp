//import android.app.Instrumentation;
//import android.content.Intent;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.AndroidTestCase;
//import android.test.InstrumentationTestCase;
//import android.test.RenamingDelegatingContext;
//import android.test.mock.MockContext;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import rieger.alarmsmsapp.control.database.DataSource;
//import rieger.alarmsmsapp.control.receiver.worker2.Alarming;
//import rieger.alarmsmsapp.model.AlarmSettingsModel;
//import rieger.alarmsmsapp.model.rules.Rule;
//import rieger.alarmsmsapp.model.rules.SMSRule;
//
//
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.notNullValue;
//
//
///**
// * Created by sebastian on 28.03.17.
// */
//@RunWith(AndroidJUnit4.class)
//public class AlarmingTest {
//
//
//    DataSource dataSource;
////
////    @Mock
////    SQLiteOpenHelper helper;
//
//    SMSRule rule = new SMSRule();
//
//    List<Rule> ruleList = new ArrayList<>();
//
//    @Before
//    public void setUp() throws Exception {
//        rule.setRuleName("Test");
//
//        ruleList.add(rule);
//        dataSource = new DataSource(InstrumentationRegistry.getContext());
//
////        when(helper.getWritableDatabase()).thenReturn(SQLiteDatabase.openDatabase("test.db", null, 0));
//
//    }
//
//    @Test
//    public void Blub(){
//
////        when(dataSource.getAllRules()).thenReturn(ruleList);
//
//        Alarming alarming = new Alarming();
//
//    }
//
//}