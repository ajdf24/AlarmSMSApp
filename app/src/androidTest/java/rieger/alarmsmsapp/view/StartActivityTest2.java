package rieger.alarmsmsapp.view;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rieger.alarmsmsapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
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
public class StartActivityTest2 {

    @Rule
    public ActivityTestRule<StartActivity> mActivityTestRule = new ActivityTestRule<>(StartActivity.class);

    @Test
    public void startActivityTest2() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction switchCompat = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_alarm_activated), withText("Alarmierungen erhalten")));
        switchCompat.perform(scrollTo(), click());

        ViewInteraction switchCompat2 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_alarm_activated), withText("Alarmierungen erhalten")));
        switchCompat2.perform(scrollTo(), click());

        ViewInteraction switchCompat3 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_vibration_activated), withText("Mit Vibration alarmieren")));
        switchCompat3.perform(scrollTo(), click());

        ViewInteraction switchCompat4 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_vibration_activated), withText("Mit Vibration alarmieren")));
        switchCompat4.perform(scrollTo(), click());

        ViewInteraction switchCompat5 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_notification_light_activated), withText("Mit Notification Light alarmieren")));
        switchCompat5.perform(scrollTo(), click());

        ViewInteraction switchCompat6 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_notification_light_activated), withText("Mit Notification Light alarmieren")));
        switchCompat6.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner = onView(
                withId(R.id.activity_alarm_settings_spinner_for_notification_light_color));
        appCompatSpinner.perform(scrollTo(), click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("grün"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatSpinner2 = onView(
                withId(R.id.activity_alarm_settings_spinner_for_notification_light_color));
        appCompatSpinner2.perform(scrollTo(), click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("blau"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction switchCompat7 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_mute_alarm_activated), withText("Alarmierung auch erhalten wenn Telefon Lautlos")));
        switchCompat7.perform(scrollTo(), click());

        ViewInteraction switchCompat8 = onView(
                allOf(withId(R.id.activity_alarm_settings_switch_mute_alarm_activated), withText("Alarmierung auch erhalten wenn Telefon Lautlos")));
        switchCompat8.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.activity_alarm_settings_repeat_alarm), withText("1")));
        appCompatEditText.perform(scrollTo(), replaceText("2"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.activity_department_settings_autoCompleteTextView_department_location), isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("Test"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.start_activity_button_next), withText("Weiter"), isDisplayed()));
        appCompatButton6.perform(click());

    }

}
