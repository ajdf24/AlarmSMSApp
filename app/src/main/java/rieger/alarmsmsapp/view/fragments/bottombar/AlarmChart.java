package rieger.alarmsmsapp.view.fragments.bottombar;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

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
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.observer.MessageObserver;
import rieger.alarmsmsapp.model.Message;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.ListMessages;

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

    @Bind(R.id.adView)
    AdView adView;

    @Bind(R.id.activity_alarm_chart_save_chart)
    Button saveChart;

    @Bind(R.id.chart)
    BarChart chart;

    @Bind(R.id.activity_alarm_chart_progressBar)
    ProgressBar progressBar;

    List<Message> messageList;

    private OnFragmentInteractionListener mListener;

    private View view;

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
        View view = inflater.inflate(R.layout.fragment_alarm_chart, container, false);

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        AdRequest adRequest = new AdRequest.Builder().build();
        if(adView != null){
            adView.loadAd(adRequest);
        }

        DataSource db = new DataSource(view.getContext());
        messageList = db.getAllMessages();

        LoadStatisticTask task = new LoadStatisticTask();
        task.execute(messageList);

        saveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE);
                }
                chart.saveToGallery("Alarm_Chart_" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) + ".jpg", 100);
            }
        });
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
                    intent.setClass(getActivity(), ListMessages.class);
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
