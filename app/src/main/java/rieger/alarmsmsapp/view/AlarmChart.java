package rieger.alarmsmsapp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.MessageObserver;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

public class AlarmChart extends AppCompatActivity {

    private static final String LOG_TAG = AlarmChart.class.getSimpleName();

    @Bind(R.id.adView)
    AdView adView;

    @Bind(R.id.activity_alarm_chart_save_chart)
    Button saveChart;

    @Bind(R.id.chart)
    BarChart chart;

    @Bind(R.id.activity_alarm_chart)
    View layoutView;

    List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_chart);

        initializeGUI();

        initializeActiveElements();

    }

    private void initializeGUI() {
        ButterKnife.bind(this);
    }

    private void initializeActiveElements() {
        AdRequest adRequest = new AdRequest.Builder().build();
        if(adView != null){
            adView.loadAd(adRequest);
        }

        messageList = MessageObserver.readAllMessagesFromFileSystem();

        LoadStatisticTask task = new LoadStatisticTask();
        task.execute(messageList);

        saveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AlarmChart.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AlarmChart.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE);
                }
                chart.saveToGallery("Alarm_Chart_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".jpg", 100);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Snackbar snackbar = Snackbar
                            .make(layoutView, R.string.toast_permission_storage_denied, Snackbar.LENGTH_LONG);

                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Resume the AdView.
        if(adView != null)
            adView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Pause the AdView.
        if(adView != null)
            adView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Destroy the AdView.
        if(adView != null)
            adView.destroy();
    }

    private class LoadStatisticTask extends AsyncTask<Object, Integer, ArrayList<BarEntry>> {

        @Override
        protected ArrayList<BarEntry> doInBackground(Object... objects) {

            ArrayList<BarEntry> entries = new ArrayList<>();
            for(int month = 0; month < 12 ; month++){
                int numberOfMessagesPerMonth = 0;
                for(Message message : (ArrayList<Message>) objects[0]){
                    if(message.getMonth() == month){
                        numberOfMessagesPerMonth++;
                    }
                }
                entries.add(new BarEntry(numberOfMessagesPerMonth,month));
                numberOfMessagesPerMonth = 0;
            }
            return entries;
        }

        @Override
        protected void onPostExecute(ArrayList<BarEntry> result) {
            super.onPostExecute(result);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_alarm_chart_progressBar);

            progressBar.setVisibility(View.INVISIBLE);
            saveChart.setEnabled(true);

            BarDataSet dataSet = new BarDataSet(result, "");

            ArrayList<String> labels = new ArrayList<String>();
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_january));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_february));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_march));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_april));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_may));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_june));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_july));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_august));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_september));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_october));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_november));
            labels.add(CreateContextForResource.getStringFromID(R.string.general_string_december));

            chart.setVisibility(View.VISIBLE);

            BarData data = new BarData(labels, dataSet);
            chart.setData(data);

            chart.setDescription(CreateContextForResource.getStringFromID(R.string.activity_alarm_chart_chart_description));

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            chart.animateY(1000);

            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

                @Override
                public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    int currentMessageNumber = 0;
                    for(Message message : messageList){
                        if(message.getMonth() == e.getXIndex()){
                            bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_SERIALIZED_MESSAGE + currentMessageNumber, message );
                            currentMessageNumber++;
                        }
                    }

                    intent.putExtras(bundle);
                    intent.setClass(AlarmChart.this, ListMessages.class);
                    startActivity(intent);
                    Log.d("Chart", "Entry: " + e.getXIndex());

                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

    }

}
