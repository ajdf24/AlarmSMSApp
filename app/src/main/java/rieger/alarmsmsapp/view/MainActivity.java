package rieger.alarmsmsapp.view;

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.fragments.bottombar.RuleSelection;
import rieger.alarmsmsapp.view.fragments.settings.AlarmSettingsFragment;
import rieger.alarmsmsapp.view.fragments.settings.DepartmentFragment;
import rieger.alarmsmsapp.view.fragments.bottombar.AlarmChart;

public class MainActivity extends AppCompatActivity implements
                                                    RuleSelection.OnFragmentInteractionListener,
                                                    AlarmSettingsFragment.OnFragmentInteractionListener,
                                                    DepartmentFragment.OnFragmentInteractionListener,
                                                    AlarmChart.OnFragmentInteractionListener{

    @Bind(R.id.activity_main_coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    private BottomBar mBottomBar;

    private RuleSelection ruleSelection;
    private AlarmSettingsFragment alarmSettings;
    private DepartmentFragment departmentSettings;
    private AlarmChart alarmChart;

    private boolean isFirstStart = true;
    private int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    if(isFirstStart){
                        isFirstStart = false;
                    }else {
                        setAnimation(ft, currentFragment, 0);
                    }

                    ruleSelection = new RuleSelection();

                    ft.replace(R.id.fragment_container, ruleSelection, "RuleSelectionFragment");
                    // Start the animated transition.
                    ft.commit();
                    currentFragment = 0;
                }
                if(menuItemId == R.id.bottomBarItemTwo){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    setAnimation(ft, currentFragment, 1);

                    alarmSettings = new AlarmSettingsFragment();

                    ft.replace(R.id.fragment_container, alarmSettings, "AlarmSettingsFragment");
                    // Start the animated transition.
                    ft.commit();
                    currentFragment = 1;
                }
                if(menuItemId == R.id.bottomBarItemThree){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    setAnimation(ft, currentFragment, 2);

                    departmentSettings = new DepartmentFragment();

                    ft.replace(R.id.fragment_container, departmentSettings, "DepartmentSettingsFragment");
                    // Start the animated transition.
                    ft.commit();
                    currentFragment = 2;
                }
                if(menuItemId == R.id.bottomBarItemFour){
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    setAnimation(ft, currentFragment, 3);

                    alarmChart = new AlarmChart();

                    ft.replace(R.id.fragment_container, alarmChart, "AlarmChartFragment");
                    // Start the animated transition.
                    ft.commit();
                    currentFragment = 3;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    // The user reselected item number one, scroll your content to top.
                }
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.my_primary));
        mBottomBar.mapColorForTab(1, 0xFF5D4037);
        mBottomBar.mapColorForTab(2, "#7B1FA2");
        mBottomBar.mapColorForTab(3, "#FF5252");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void setAnimation(FragmentTransaction fragmentTransaction, int from, int to){
        if(to == 0){
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
        }
        if(to == 1){
            if(from == 0){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            }
            if(from > 1){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        }
        if(to == 2){
            if(from < 2) {
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
            }
            if(from > 2){
                fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        }
        if(to == 3){
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case AppConstants.PermissionsIDs.PERMISSION_ID_FOR_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, R.string.toast_permission_storage_denied, Snackbar.LENGTH_LONG);

                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                break;
        }
    }
}
