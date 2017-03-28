//import android.content.Context;
//import android.content.Intent;
//import android.telephony.PhoneNumberUtils;
//import android.test.AndroidTestCase;
//import android.test.RenamingDelegatingContext;
//
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//
//import rieger.alarmsmsapp.control.database.DataSource;
//import rieger.alarmsmsapp.control.receiver.SMSReceiver;
//import rieger.alarmsmsapp.model.DepartmentSettingsModel;
//
///**
// * Created by sebastian on 26.03.17.
// */
//public class SMSReceiverTests extends AndroidTestCase {
//
//    RenamingDelegatingContext context = null;
//
//    private DepartmentSettingsModel department;
//    private DataSource db;
//
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        context = new RenamingDelegatingContext(getContext(), "test_");
//
//        db = new DataSource(context);
//
//
//        department = new DepartmentSettingsModel();
//        department.setAddress("Adresse Test Department");
//        db.saveDepartment(department);
//
//    }
//
//    public void testCalledReceiver() {
//        createFakeSms(context, "1234", "TEST");
//    }
//
//    private static void createFakeSms(Context context, String sender,
//                                      String body) {
//        byte[] pdu = null;
//        byte[] scBytes = PhoneNumberUtils
//                .networkPortionToCalledPartyBCD("0000000000");
//        byte[] senderBytes = PhoneNumberUtils
//                .networkPortionToCalledPartyBCD(sender);
//        int lsmcs = scBytes.length;
//        byte[] dateBytes = new byte[7];
//        Calendar calendar = new GregorianCalendar();
//        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
//        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
//        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
//        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
//        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
//        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
//        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
//                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
//        try {
//            ByteArrayOutputStream bo = new ByteArrayOutputStream();
//            bo.write(lsmcs);
//            bo.write(scBytes);
//            bo.write(0x04);
//            bo.write((byte) sender.length());
//            bo.write(senderBytes);
//            bo.write(0x00);
//            bo.write(0x00); // encoding: 0 for default 7bit
//            bo.write(dateBytes);
//            try {
//                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
//                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
//                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
//                        "stringToGsm7BitPacked", new Class[] { String.class });
//                stringToGsm7BitPacked.setAccessible(true);
//                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null,
//                        body);
//                bo.write(bodybytes);
//            } catch (Exception e) {
//            }
//
//            pdu = bo.toByteArray();
//        } catch (IOException e) {
//        }
//
//        Intent intent = new Intent();
//        intent.setClassName("rieger.alarmsmsapp.control.receiver",
//                "rieger.alarmsmsapp.control.receiver.SMSReceiver");
//        intent.setAction("android.provider.Telephony.SMS_RECEIVED");
//        intent.putExtra("pdus", new Object[] { pdu });
//        intent.putExtra("format", "3gpp");
//        SMSReceiver s = new SMSReceiver();
//        s.onReceive(context, intent);
//    }
//
//    private static byte reverseByte(byte b) {
//        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
//    }
//
//}
