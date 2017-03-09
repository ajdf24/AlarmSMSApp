package rieger.alarmsmsapp.view.fragments.settings;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.model.DepartmentSettingsModel;
import rieger.alarmsmsapp.util.googleplaces.GooglePlacesAutocompleteAdapter;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;


/**
 * This fragment sets the Department and save it to the database
 */
public class DepartmentFragment extends Fragment implements OnItemClickListener{

    /**
     * log tag
     */
    private static final String LOG_TAG = DepartmentFragment.class.getSimpleName();

    @Bind(R.id.activity_department_settings_autoCompleteTextView_department_location)
    AutoCompleteTextView autoCompView;

    private DepartmentSettingsModel departmentSettings;

    private OnFragmentInteractionListener listener;

    private View view;

    /**
     * constructor
     */
    public DepartmentFragment() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_department, container, false);

        ButterKnife.bind(this, view);

        getDepartmentSettingsForGUI();

        initializeActiveElements();

        return view;
    }

    /**
     * {@inheritDoc}
     */
    private void getDepartmentSettingsForGUI() {

        try {
            DataSource db = new DataSource(view.getContext());
            departmentSettings = db.getAllDepartments().get(db.getAllDepartments().size()-1);
        }catch (ArrayIndexOutOfBoundsException e){
            departmentSettings = new DepartmentSettingsModel();
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

    /**
     * save the department to the database
     */
    public void saveData(){
        if(view != null) {
            departmentSettings = new DepartmentSettingsModel();

            departmentSettings.setAddress(autoCompView.getText().toString());

            DataSource db = new DataSource(view.getContext());
            db.saveDepartment(departmentSettings);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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

    /**
     * Show the selected item form the given places as toast
     *
     * @param adapterView adapter for select
     * @param view the current view
     * @param position the clicked position
     * @param id the row id of the item that was clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(CreateContextForResource.getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
