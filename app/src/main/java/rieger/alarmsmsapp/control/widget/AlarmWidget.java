package rieger.alarmsmsapp.control.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.model.AlarmSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.AlarmSettings;

/**
 * Controler class for the alarm widget.
 * Created by sebastian on 25.07.15.
 */
public class AlarmWidget extends AppWidgetProvider {
    private static RemoteViews remoteViews;
    private static ComponentName watchWidget;

    static AlarmSettingsModel alarmSettingsModel = null;

    /**
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget_activate_alarm );
        watchWidget = new ComponentName( context, AlarmWidget.class );

        Intent intentClick = new Intent(context,AlarmWidget.class);
        intentClick.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ""+appWidgetIds[0]);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[0],intentClick, 0);

        remoteViews.setOnClickPendingIntent(R.id.img_btn, pendingIntent);
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        updateWidget();
    }

    /**
     *  This method is called when a event on the widget occurs.
     *  First stepp is to load the alarmsettingsmodell.
     *  After that it is checked if something must be done.
     *  If the button was pressed the settings will be updated and the widget will get a update to.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        loadAlarmSettings();

        if (intent.getAction()==null) {
            Bundle extras = intent.getExtras();
            if(extras!=null) {

                remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget_activate_alarm );

                if(alarmSettingsModel.isAlarmActivated()){
                    remoteViews.setTextViewText(R.id.widget_textview, CreateContextForResource.getStringFromID(R.string.widget_text_inactiv));

                    alarmSettingsModel.setAlarmActivated(false);
                    alarmSettingsModel.notifyObserver();

                }else{
                    remoteViews.setTextViewText(R.id.widget_textview, CreateContextForResource.getStringFromID(R.string.widget_text_activ));

                    alarmSettingsModel.setAlarmActivated(true);
                    alarmSettingsModel.notifyObserver();
                }

                watchWidget = new ComponentName( context, AlarmWidget.class );

                (AppWidgetManager.getInstance(context)).updateAppWidget( watchWidget, remoteViews );
                //Toast.makeText(context, "Clicked "+status, 2000).show();
            }
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        System.out.println("");
    }

    /**
     * This method updates the View of the Widget.
     */
    public static void updateWidget(){

        loadAlarmSettings();
        if (alarmSettingsModel != null) {
            if (alarmSettingsModel.isAlarmActivated()) {
                remoteViews = new RemoteViews(CreateContextForResource.getContext().getPackageName(), R.layout.widget_activate_alarm);
                remoteViews.setTextViewText(R.id.widget_textview, CreateContextForResource.getStringFromID(R.string.widget_text_activ));
            } else {
                remoteViews = new RemoteViews(CreateContextForResource.getContext().getPackageName(), R.layout.widget_activate_alarm);
                remoteViews.setTextViewText(R.id.widget_textview, CreateContextForResource.getStringFromID(R.string.widget_text_inactiv));
            }
            try {
                (AppWidgetManager.getInstance(CreateContextForResource.getContext())).updateAppWidget(watchWidget, remoteViews);
            } catch (NullPointerException e) {
                Log.e("AlarmWidget.java", e.getMessage());
                //Es muss noch erforscht werden, warum hier eine NPE auftritt wenn die App neu startet, und des trotz fangen der NPE geht!
            }
        }
    }

    private static void loadAlarmSettings(){
        try {
            alarmSettingsModel = AlarmSettingsObserver.readSettings();
        } catch (SettingsNotFoundException e) {
            Log.e("AlarmWidget", "Alarm Settings not found.");
        }
        if(alarmSettingsModel == null){
            CreateContextForResource.getContext().startActivity(new Intent(CreateContextForResource.getContext(), AlarmSettings.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
