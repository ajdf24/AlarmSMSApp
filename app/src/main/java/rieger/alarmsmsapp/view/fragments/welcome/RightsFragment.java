package rieger.alarmsmsapp.view.fragments.welcome;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RightsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RightsFragment extends Fragment {

    @Bind(R.id.fragment_rights_text)
    TextView rightsText;

    View view;

    public RightsFragment() {
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
        view = inflater.inflate(R.layout.fragment_rights, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void changeTest(int number){
        if(number == 5){
            rightsText.setText(R.string.rights_fragment_text_click_5);
            rightsText.setGravity(Gravity.CENTER);
        }
        if(number == 6){
            rightsText.setText(R.string.rights_fragment_text_click_6);
            rightsText.setGravity(Gravity.CENTER);
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
