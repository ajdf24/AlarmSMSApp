package rieger.alarmsmsapp.view.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.util.googleplaces.GooglePlacesAutocompleteAdapter;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.StartActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DepartmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DepartmentFragment extends Fragment implements OnItemClickListener{


    private static final String LOG_TAG = DepartmentFragment.class.getSimpleName();

    @Bind(R.id.activity_department_settings_autoCompleteTextView_department_location)
    AutoCompleteTextView autoCompView;

    private DepartmentSettingsModel departmentSettings;

    private OnFragmentInteractionListener mListener;

    public DepartmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        ButterKnife.bind(this, view);

        getDepartmentSettingsForGUI();

        initializeActiveElements();

        return view;
    }

    private void getDepartmentSettingsForGUI() {
        try {
            departmentSettings = DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){

            Log.e(LOG_TAG,"Settings not loaded");
        }

        if (departmentSettings != null) {
            autoCompView.setText(departmentSettings.getAddress());
        }
    }

    /**
     * This method initialize the active GUI elements with listeners.
     */
    private void initializeActiveElements() {

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(CreateContextForResource.getContext(), R.layout.list_item_for_autocomplete, GooglePlacesAutocompleteAdapter.AUTO_FILTER_BY_THE_CURRENT_COUNTRY));
        autoCompView.setOnItemClickListener(this);

    }

    public void saveData(){

        departmentSettings = new DepartmentSettingsModel();

        departmentSettings.setAddress(autoCompView.getText().toString());

        DepartmentObserver.saveSettings(departmentSettings);
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(CreateContextForResource.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
