import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.model.rules.SMSRule;

/**
 * Created by sebastian on 16.08.16.
 */
public class AlarmTimeModel extends AndroidTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
    }

    public void testConvertIntToDays(){

        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(0), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SUNDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(1), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(2), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.TUESDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(3), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.WEDNESDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(4), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.THURSDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(5), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.FRIDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(6), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SATURDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(7), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY_TO_SUNDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(8), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY_TO_FRIDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(9), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.FRIDAY_TO_SUNDAY);
        assertEquals(rieger.alarmsmsapp.model.rules.AlarmTimeModel.intToDays(10), rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SATURDAY_TO_SUNDAY);
    }

    public void testConvertDaysToInt(){

        assertEquals(0, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SUNDAY));
        assertEquals(1, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY));
        assertEquals(2, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.TUESDAY));
        assertEquals(3, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.WEDNESDAY));
        assertEquals(4, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.THURSDAY));
        assertEquals(5, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.FRIDAY));
        assertEquals(6, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SATURDAY));
        assertEquals(7, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY_TO_SUNDAY));
        assertEquals(8, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.MONDAY_TO_FRIDAY));
        assertEquals(9, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.FRIDAY_TO_SUNDAY));
        assertEquals(10, rieger.alarmsmsapp.model.rules.AlarmTimeModel.daysToInt(rieger.alarmsmsapp.model.rules.AlarmTimeModel.Days.SATURDAY_TO_SUNDAY));
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}
