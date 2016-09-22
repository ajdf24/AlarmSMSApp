package rieger.alarmsmsapp.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rieger.alarmsmsapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StartActivityTest {

    @Rule
    public ActivityTestRule<StartActivity> mActivityTestRule = new ActivityTestRule<StartActivity>(StartActivity.class) {
        @Override
        protected void beforeActivityLaunched() {

            clearSharedPrefs(InstrumentationRegistry.getTargetContext());
            super.beforeActivityLaunched();
        }
    };

    @Test
    public void startActivityTest() {

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction switchCompat = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_alarm_activated)));
        switchCompat.perform(scrollTo(), click());

        ViewInteraction switchCompat2 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_alarm_activated)));
        switchCompat2.perform(scrollTo(), click());

        ViewInteraction switchCompat3 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_vibration_activated)));
        switchCompat3.perform(scrollTo(), click());

        ViewInteraction switchCompat4 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_vibration_activated)));
        switchCompat4.perform(scrollTo(), click());

        ViewInteraction switchCompat5 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_vibration_activated)));
        switchCompat5.perform(scrollTo(), click());

        ViewInteraction switchCompat6 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_notification_light_activated)));
        switchCompat6.perform(scrollTo(), click());

        ViewInteraction switchCompat7 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_mute_alarm_activated)));
        switchCompat7.perform(scrollTo(), click());

        ViewInteraction switchCompat8 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_mute_alarm_activated)));
        switchCompat8.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.activity_alarm_settings_repeat_alarm)));
        appCompatEditText.perform(scrollTo(), replaceText("3"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.activity_department_settings_autoCompleteTextView_department_location), isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("blub"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.start_activity_button_next), isDisplayed()));
        appCompatButton6.perform(click());

    }

    /**
     * Clears everything in the SharedPreferences
     */
    private void clearSharedPrefs(Context context) {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

}
