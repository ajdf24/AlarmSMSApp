package rieger.alarmsmsapp.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.observer.AlarmSettingsObserver;
import rieger.alarmsmsapp.control.observer.DepartmentObserver;
import rieger.alarmsmsapp.control.widget.DynamicImageView;
import rieger.alarmsmsapp.model.SettingsNotFoundException;
import rieger.alarmsmsapp.view.Fragments.AlarmSettingsFragment;
import rieger.alarmsmsapp.view.Fragments.WelcomeFragment;

/**
 * This class is the start activity, which checks the permissions and the settings if the first use.
 * Created by sebastian on 19.08.15.
 */
public class StartActivity extends AppCompatActivity implements WelcomeFragment.OnFragmentInteractionListener, AlarmSettingsFragment.OnFragmentInteractionListener{

    private static final String LOG_TAG = StartActivity.class.getSimpleName();

    @Bind(R.id.start_activity_button_next)
    Button buttonNext;


    int clickCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_activity);

//        ButterKnife.bind(this);
//
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
//
//
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        final WelcomeFragment hello = new WelcomeFragment();
//        fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
//        fragmentTransaction.commit();
//
//        buttonNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (clickCounter == 0) {
//
//                    final DynamicImageView imageView = (DynamicImageView) hello.getView().findViewById(R.id.view2);
//                    final TextView mainText = (TextView) hello.getView().findViewById(R.id.textView13);
//                    final TextView mainText2 = (TextView) hello.getView().findViewById(R.id.textView2);
//
//                    TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
//                    animation.setDuration(500);
//                    animation.setFillAfter(false);
//
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            imageView.setVisibility(View.GONE);
//                            mainText.setText("AlarmSMS nutzt globale Alarmeinstellungen, welche für jede Alarmierung gelten.");
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//
//                    imageView.startAnimation(animation);
//
//
//                    mainText.startAnimation(animation);
//                    mainText2.startAnimation(animation);
//
//                }
//                if (clickCounter == 1) {
//
//
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//
//                    ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//
//                    AlarmSettingsFragment test = new AlarmSettingsFragment();
//
//                    ft.replace(R.id.fragment_container, test, "fragment");
//// Start the animated transition.
//                    ft.commit();
//                }
//                clickCounter++;
//            }
//        });

        redirectForFirstUse();
    }
    /**
     * This method redirect the user if there are no settings are found.
     */
    private  void redirectForFirstUse() {
        try {
            AlarmSettingsObserver.readSettings();
        }catch (SettingsNotFoundException e) {
                Log.i(this.getClass().getSimpleName(), "Alarm Settings not set. Start Alarm Settings");
                startActivity(new Intent(this, AlarmSettings.class));
                return;
        }

        try {
            DepartmentObserver.readSettings();
        }catch (SettingsNotFoundException e){
            Log.i(this.getClass().getSimpleName(), "Department Settings not set. Start Alarm Settings");
            startActivity(new Intent(this, DepartmentSettings.class));
            return;
        }

        startActivity(new Intent(this, RuleSelection.class));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
