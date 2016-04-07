package rieger.alarmsmsapp.view.ruleactivities;

import android.test.ActivityInstrumentationTestCase2;

import javax.inject.Inject;

public class InjectedBaseActivityTest extends ActivityInstrumentationTestCase2 {

    public InjectedBaseActivityTest(){
        super(null);
    }

    @Inject
    public InjectedBaseActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
    }

}