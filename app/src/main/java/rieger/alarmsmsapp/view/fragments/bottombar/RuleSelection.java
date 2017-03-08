package rieger.alarmsmsapp.view.fragments.bottombar;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.adapter.RuleSelectionAdapter;
import rieger.alarmsmsapp.control.callback.RuleSelected;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.listener.OpenRuleSettingsListener;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.view.ruleactivitys.CreateNewRule;
import rieger.alarmsmsapp.view.ruleactivitys.SoundSelection;

/**
 * Created by sebastian on 08.03.17.
 */

public class RuleSelection extends Fragment {


    @Bind(R.id.fab)
    FloatingActionButton createNewRule;

    @Bind(R.id.activity_rule_selection_listView)
    RecyclerView ruleView;

    @Bind(R.id.adView3)
    AdView adView;

    @Bind(R.id.fragment_rule_selection)
    CoordinatorLayout relativeLayout;

    private View view;

    private List<Rule> ruleList;

    private RuleSelectionAdapter ruleSelectionAdapter;

    private static RuleSelected callback;

    public RuleSelection() {
    }

    public static RuleSelection newInstance(RuleSelected ruleSelected) {

        callback = ruleSelected;
        Bundle args = new Bundle();

        RuleSelection fragment = new RuleSelection();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataSource db = new DataSource(getActivity());
        ruleList = db.getAllRules();

        sortRuleList();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_rule_selection, container, false);

        ButterKnife.bind(this, view);

        initializeGUI();

        initializeActiveElements();

        checkAlarmSounds();

        return view;
    }

    private void initializeGUI() {

        ruleSelectionAdapter = new RuleSelectionAdapter(callback, ruleList, new OpenRuleSettingsListener(getActivity(), ruleView, ruleList));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ruleView.setLayoutManager(linearLayoutManager);

        ruleView.setAdapter(ruleSelectionAdapter);

        createAdd();
    }

    private void initializeActiveElements(){

        createNewRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateNewRule.class));
            }
        });
    }

    private void createAdd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        if(adView != null){
            adView.loadAd(adRequest);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (adView != null)
            adView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adView != null)
            adView.destroy();
    }

    /**
     * Checks all rules if a Sound is set.
     * If a rule without sound is found the method show a Snackbar, which shows the information.
     */
    private void checkAlarmSounds() {

        for (final Rule rule : ruleList) {
            if (rule.getAlarmSoundUri().getIdForSound().isEmpty()) {
                Snackbar snackbar = Snackbar
                        .make(relativeLayout, getString(R.string.activity_rule_selection_snackbar_label, rule.getRuleName()), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.activity_rule_selection_snackbar_button, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();

                        bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE,
                                rule);
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(), SoundSelection.class);
                        startActivity(intent);
                    }
                });

                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);

                snackbar.show();
                break;
            }
        }
    }

    /**
     * This Method sorts the ruleList by name.
     */
    private void sortRuleList() {
        //Sorting
        Collections.sort(ruleList, new Comparator<Rule>() {
            @Override
            public int compare(Rule rule1, Rule rule2) {

                return rule1.getRuleName().compareToIgnoreCase(rule2.getRuleName());
            }
        });
    }
}
