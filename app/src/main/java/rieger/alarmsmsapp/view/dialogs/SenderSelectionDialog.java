package rieger.alarmsmsapp.view.dialogs;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SenderSelectionDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SenderSelectionDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SenderSelectionDialog extends DialogFragment {

    private static final String LOG_TAG = SenderSelectionDialog.class.getSimpleName();

    @Bind(R.id.dialog_help_sender_selection_button_got_it)
    Button buttonGotIt;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment sender_selection_dialog.
     */
    public static SenderSelectionDialog newInstance() {
        return new SenderSelectionDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_sender_selection_dialog, container, false);

        ButterKnife.bind(this, fragmentView);

        buttonGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Inflate the layout for this fragment
        return fragmentView;
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
}
