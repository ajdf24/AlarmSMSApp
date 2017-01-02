//package rieger.alarmsmsapp.view.ruleactivitys;
//
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import rieger.alarmsmsapp.R;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.action.ViewActions.scrollTo;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class CreateNewRuleTest {
//
//    @Rule
//    public ActivityTestRule<CreateNewRule> mActivityTestRule = new ActivityTestRule<CreateNewRule>(CreateNewRule.class){
//        @Override
//        protected void beforeActivityLaunched() {
//
//            clearSharedPrefs(InstrumentationRegistry.getTargetContext());
//            super.beforeActivityLaunched();
//        }
//    };
//
//    @Test
//    public void createNewRuleTest() {
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.showcase_button), isDisplayed()));
//        appCompatButton.perform(click());
//
//        ViewInteraction appCompatEditText = onView(
//                withId(R.id.activity_create_new_rule_editText_rule_name));
//        appCompatEditText.perform(scrollTo(), replaceText("t"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.showcase_button),  isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.activity_create_new_rule_editText_rule_name)));
//        appCompatEditText2.perform(scrollTo(), replaceText("test"), closeSoftKeyboard());
//
//        ViewInteraction floatingActionButton = onView(
//                allOf(withId(R.id.activity_create_new_rule_button_save_rule_name), isDisplayed()));
//        floatingActionButton.perform(click());
//
//    }
//
//    /**
//     * Clears everything in the SharedPreferences
//     */
//    private void clearSharedPrefs(Context context) {
//        SharedPreferences prefs =
//                PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.commit();
//    }
//
//}
