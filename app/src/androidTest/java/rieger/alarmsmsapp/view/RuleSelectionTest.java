//package rieger.alarmsmsapp.view;
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
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
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
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class RuleSelectionTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
//        @Override
//        protected void beforeActivityLaunched() {
//
//            clearSharedPrefs(InstrumentationRegistry.getTargetContext());
//            super.beforeActivityLaunched();
//        }
//    };
//
//    @Test
//    public void ruleSelectionTest() {
//        ViewInteraction floatingActionButton = onView(
//                allOf(withId(R.id.fab), isDisplayed()));
//        floatingActionButton.perform(click());
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.showcase_button), isDisplayed()));
//        appCompatButton.perform(click());
//
//        ViewInteraction appCompatEditText = onView(
//                withId(R.id.activity_create_new_rule_editText_rule_name));
//        appCompatEditText.perform(scrollTo(), replaceText("test"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(R.id.showcase_button), isDisplayed()));
//        appCompatButton2.perform(click());
//
//        ViewInteraction floatingActionButton2 = onView(
//                allOf(withId(R.id.activity_create_new_rule_button_save_rule_name), isDisplayed()));
//        floatingActionButton2.perform(click());
//
//        ViewInteraction appCompatTextView = onView(
//                allOf(withId(R.id.list_item_rule_name),
//                        withParent(childAtPosition(
//                                withId(R.id.activity_rule_selection_listView),
//                                0)),
//                        isDisplayed()));
//        appCompatTextView.perform(click());
//
//        ViewInteraction appCompatButton3 = onView(
//                allOf(withId(R.id.showcase_button), isDisplayed()));
//        appCompatButton3.perform(click());
//
//        ViewInteraction appCompatButton4 = onView(
//                allOf(withId(R.id.showcase_button), isDisplayed()));
//        appCompatButton4.perform(click());
//
//        ViewInteraction appCompatTextView2 = onView(
//                allOf(withText("Alarmeinstellung"), isDisplayed()));
//        appCompatTextView2.perform(click());
//
//        ViewInteraction appCompatTextView3 = onView(
//                allOf(withText("Auslöser"), isDisplayed()));
//        appCompatTextView3.perform(click());
//
//    }
//
//    private static Matcher<View> childAtPosition(
//            final Matcher<View> parentMatcher, final int position) {
//
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("Child at position " + position + " in parent ");
//                parentMatcher.describeTo(description);
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//                ViewParent parent = view.getParent();
//                return parent instanceof ViewGroup && parentMatcher.matches(parent)
//                        && view.equals(((ViewGroup) parent).getChildAt(position));
//            }
//        };
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
//}
