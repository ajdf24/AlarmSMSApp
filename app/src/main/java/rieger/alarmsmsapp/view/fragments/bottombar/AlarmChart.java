package rieger.alarmsmsapp.view.fragments.bottombar;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.util.standard.SpinnerUtil;
import rieger.alarmsmsapp.view.ListMessages;
import rieger.alarmsmsapp.view.ruleactivitys.CreateNewRule;
import rieger.alarmsmsapp.view.ruleactivitys.CreateRuleFromSMS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmChart.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmChart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmChart extends Fragment {

    private static final String LOG_TAG = AlarmChart.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    @Bind(R.id.adView)
    AdView adView;

    @Bind(R.id.activity_alarm_chart_save_chart)
    Button saveChart;

    @Bind(R.id.chart)
    BarChart chart;

    @Bind(R.id.activity_alarm_chart_progressBar)
    ProgressBar progressBar;

    @Bind(R.id.activity_alarm_chart_year)
    Spinner spinner;

    List<Message> messageList;

    private OnFragmentInteractionListener mListener;

    private View view;

    private boolean showList = false;

    public AlarmChart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmChart.
     */
    public static AlarmChart newInstance() {
        AlarmChart fragment = new AlarmChart();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_chart, container, false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.getContext());

        ButterKnife.bind(this, view);

        initializeActiveElements();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        showList = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        showList = true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initializeActiveElements() {

        createAdd();

        createYearSpinner();

        loadStatisticForYear(Calendar.getInstance().get(Calendar.YEAR));

        saveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE);
                }
                chart.saveToGallery("Alarm_Chart_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".jpg", 100);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    AlarmChart.this.getActivity().sendBroadcast(mediaScanIntent);
                }
                else
                {
                    AlarmChart.this.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
                Toast.makeText(CreateContextForResource.getContext(), R.string.saved_satistic, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAdd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        if(adView != null){
            adView.loadAd(adRequest);
        }
    }

    private void createYearSpinner(){
        List<String> years = new ArrayList<>();

        for(int year = 2016; year <= Calendar.getInstance().get(Calendar.YEAR); year++){
            years.add(year + "");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, years);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(SpinnerUtil.getIndex(spinner, Calendar.getInstance().get(Calendar.YEAR) + ""));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loadStatisticForYear(2016 + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                loadStatisticForYear(2016 + i);
//            }
//        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
//                        .setTarget(new ViewTarget(view.findViewById(R.id.activity_alarm_chart_year)))
//                        .setContentTitle(R.string.showcase_rule_settings_trigger_title)
//                        .setContentText(R.string.showcase_rule_settings_trigger_text)
//                        .hideOnTouchOutside()
//                        .setStyle(R.style.CustomShowcaseTheme)
//                        .build();
//            }
//        }, 600);


    }

    private void loadStatisticForYear(int year){
        DataSource db = new DataSource(view.getContext());
        messageList = db.getAllMessagesForYear(year);

        LoadStatisticTask task = new LoadStatisticTask();
        task.execute(messageList);
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
            for(int month = 1; month < 13 ; month++){
                int numberOfMessagesPerMonth = 0;
                for(Message message : (ArrayList<Message>) objects[0]){
                    if(message.getMonth() -1  == month){
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

            mFirebaseAnalytics.logEvent("show_statistics", null);

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
                        if(message.getMonth() == (e.getXIndex() + 1)){
                            bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_SERIALIZED_MESSAGE + currentMessageNumber, message );
                            currentMessageNumber++;
                        }
                    }

                    intent.putExtras(bundle);
                    intent.setClass(getActivity(), ListMessages.class);

                    if(showList){
                        startActivity(intent);
                        showList = false;
                    }
                    Log.d("Chart", "Entry: " + e.getXIndex());

                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

    }
}
