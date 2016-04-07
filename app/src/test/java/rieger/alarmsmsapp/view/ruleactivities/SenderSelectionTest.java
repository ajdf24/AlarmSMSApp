package rieger.alarmsmsapp.view.ruleactivities;

import android.app.Activity;
import android.content.Context;
import android.support.test.internal.runner.junit4.AndroidJUnit4Builder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.ruleactivitys.SenderSelection;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.click;


/**
 * Created by sebastian on 05.04.16.
 */
//@RunWith(AndroidJUnit4.class)
@PrepareForTest({CreateContextForResource.class})
public class SenderSelectionTest extends InjectedBaseActivityTest{

//    public SenderSelectionTest() {
//    }

    @Rule
    public ActivityTestRule<SenderSelection> activityRule =
            new ActivityTestRule<>(SenderSelection.class);

    @Mock
    Context context;

    public SenderSelectionTest(Class activityClass) {
        super(activityClass);
    }

    @Before
    public void before_Test(){
        PowerMockito.mockStatic(CreateContextForResource.class);
        PowerMockito.when(CreateContextForResource.getContext()).thenReturn(context);

    }

    @Test
    public void senderSave_Test(){

        onView(withId(R.id.activity_sender_selection_editText_for_sender_information))
                .perform(typeText("HELLO "), closeSoftKeyboard());

        onView(withId(R.id.activity_sender_selection_button_save_sender)).perform(click());
    }
}
