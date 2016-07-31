package rieger.alarmsmsapp.view;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.view.fragments.bottombar.RuleSelection;

public class MainActivity extends AppCompatActivity implements RuleSelection.OnFragmentInteractionListener {

    private BottomBar mBottomBar;

    private RuleSelection ruleSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);

                    ruleSelection = new RuleSelection();

                    ft.replace(R.id.fragment_container, ruleSelection, "RuleSelectionFragment");
                    // Start the animated transition.
                    ft.commit();
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
}
