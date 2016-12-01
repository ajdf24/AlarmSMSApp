package rieger.alarmsmsapp.view.fragments.bottombar;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.control.callback.ActionCallback;
import rieger.alarmsmsapp.control.database.DataSource;
import rieger.alarmsmsapp.control.factory.RuleCreator;
import rieger.alarmsmsapp.control.observer.RuleObserver;
import rieger.alarmsmsapp.model.rules.AlarmTimeModel;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.ruleactivitys.CreateNewRule;
import rieger.alarmsmsapp.view.ruleactivitys.RuleSettings;
import rieger.alarmsmsapp.view.ruleactivitys.SoundSelection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RuleSelection.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RuleSelection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RuleSelection extends Fragment {

    private static final String LOG_TAG = RuleSelection.class.getSimpleName();

    private List<Rule> ruleList;

    SharedPreferences prefs;

    @Bind(R.id.activity_rule_selection_listView)
    ListView listView;

    private RuleSelectionAdapter listAdapter;

    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;

    private View layoutView;

    private AdView adView;

    private OnFragmentInteractionListener mListener;

    public RuleSelection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param callback Parameter 1.
     * @return A new instance of fragment RuleSelection.
     */
    // TODO: Rename and change types and number of parameters
    public static RuleSelection newInstance(ActionCallback callback) {
        RuleSelection fragment = new RuleSelection();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutView = inflater.inflate(R.layout.fragment_rule_selection, container, false);

        ruleList = Collections.synchronizedList(new ArrayList<Rule>());

        DataSource dataSource = new DataSource(layoutView.getContext());

        ruleList = dataSource.getAllRules();

        initializeGUI();

        initializeActiveElements();

        sortRuleList();

        checkAlarmSounds();

        return layoutView;
    }

    public void onButtonPressed(Uri uri) {
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
    public void onResume() {
        super.onResume();

        prefs = PreferenceManager.getDefaultSharedPreferences(CreateContextForResource.getContext());
        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_RULES, true)) {

            final FrameLayout layout = (FrameLayout) layoutView.findViewById(R.id.fragment_rule_selection);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int width = layout.getMeasuredWidth();
                    int height = layout.getMeasuredHeight();
                    System.out.println(listView.isShown());

                    if (listView.getChildAt(0) != null) {
//                        ShowcaseView showcaseView = new ShowcaseView.Builder(getActivity())
//                                .setTarget(new ViewTarget(listView.getChildAt(0).findViewById(R.id.list_item_rule_name)))
//                                .setContentTitle(R.string.showcase_more_rule_options_title)
//                                .setStyle(R.style.CustomShowcaseTheme)
//                                .setContentText(R.string.showcase_more_rule_options_text)
//                                .hideOnTouchOutside()
//                                .blockAllTouches()
//                                .build();
//                        showcaseView.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_RULES, false);
                        editor.apply();

                    } else {
                        System.out.println("Element nicht vorhanden");
                    }
                }
            });
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Rule rule);
    }

    /**
     * Checks all rules if a Sound is set.
     * If a rule without sound is found the method show a Snackbar, which shows the information.
     */
    private void checkAlarmSounds() {

        for(final Rule rule : ruleList ){
            if(rule.getAlarmSound() == null){
                Snackbar snackbar = Snackbar
                        .make(layoutView, getString(R.string.activity_rule_selection_snackbar_label, rule.getRuleName()), Snackbar.LENGTH_INDEFINITE);
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

    /**
     * This method initialize the active GUI elements with listeners.
     */
    private void initializeActiveElements() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, (Rule) listView.getAdapter().getItem(position));
                intent.putExtras(bundle);
                intent.setClass(getActivity(), RuleSettings.class);
                startActivity(intent);
            }catch (IndexOutOfBoundsException e) {
                Log.w(LOG_TAG, "Banner Clicked");
            }
        }});

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateNewRule.class));
            }
        });
    }

    /**
     * This method initialize the all GUI elements.
     */
    private void initializeGUI() {

        ButterKnife.bind(this, layoutView);

        listAdapter = new RuleSelectionAdapter(getActivity(), R.layout.list_item_rule_selection, (ArrayList<Rule>) ruleList);

        listView.setAdapter(listAdapter);

    }

    /**
     * Internal class, which creates a special adapter for this activity.
     */
    private class RuleSelectionAdapter extends ArrayAdapter<Rule> {

        /**
         * Constructor
         *
         * @param context The current context.
         * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
         *                 instantiating views.
         * @param ruleList The objects to represent in the ListView.
         */
        public RuleSelectionAdapter(Context context, int textViewResourceId,
                                    ArrayList<Rule> ruleList) {
            super(context, textViewResourceId, ruleList);
        }

        @Override
        public int getCount() {
            return ruleList.size() +1 ;
        }

        /**
         * Internal class, which creates a holder for the adapter elements,
         */
        private class ViewHolder {
            TextView ruleName;
            TextView timeControlled;
            CheckBox isRuleActivated;
        }

        /**
         * {@inheritDoc}
         * Creates the view with the elements and her listeners.
         */
        @Override
        public synchronized View getView(final int position, View convertView, ViewGroup parent) {

            //Create Ad on the last item of the List
            if (position >= ruleList.size()) {
                if (convertView instanceof AdView) {
                    // Don’t instantiate new AdView, reuse old one
                    return convertView;
                } else {
                    try {
                        // Create a new AdView
                        adView = new AdView(parent.getContext());
                        adView.setAdSize(AdSize.BANNER);
                        adView.setAdUnitId("ca-app-pub-5905277784747964/9606094931");

                        // Convert the default layout parameters so that they play nice with
                        // ListView.

                        float density = parent.getResources().getDisplayMetrics().density;
                        int height = Math.round(AdSize.BANNER.getHeight() * density);
                        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                                AbsListView.LayoutParams.FILL_PARENT,
                                height);
                        adView.setLayoutParams(params);

                        adView.loadAd(new AdRequest.Builder().build());

                        return adView;
                    }catch (Exception e){



                        //Workaround, sollte es bei der Darstellung der ad zu Fehlern irgendeiner Art kommen
                        final ViewHolder viewHolder = new ViewHolder();

                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.list_item_rule_selection, null);

                        viewHolder.ruleName = (TextView) convertView.findViewById(R.id.list_item_rule_name);
                        viewHolder.timeControlled = (TextView) convertView.findViewById(R.id.list_item_rule_time_controlled);
                        viewHolder.isRuleActivated = (CheckBox) convertView.findViewById(R.id.list_item_is_active);
                        viewHolder.ruleName.setVisibility(View.INVISIBLE);
                        viewHolder.timeControlled.setVisibility(View.INVISIBLE);
                        viewHolder.isRuleActivated.setVisibility(View.INVISIBLE);
                        convertView.setOnClickListener(null);

                        return convertView;

                    }
                }
            } else {

                final ViewHolder viewHolder = new ViewHolder();
                Log.v(LOG_TAG, String.valueOf(position));

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_rule_selection, null);

                    viewHolder.ruleName = (TextView) convertView.findViewById(R.id.list_item_rule_name);
                    viewHolder.isRuleActivated = (CheckBox) convertView.findViewById(R.id.list_item_is_active);
                    viewHolder.timeControlled = (TextView) convertView.findViewById(R.id.list_item_rule_time_controlled);
                    convertView.setTag(viewHolder);

                    viewHolder.isRuleActivated.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CheckBox checkBox = (CheckBox) view;
                            Rule rule = (Rule) checkBox.getTag();
                            if (checkBox.isChecked()) {
                                Toast.makeText(CreateContextForResource.getContext(), getResources().getString(R.string.activity_rule_selection_toast_rule_activated, rule.getRuleName()), Toast.LENGTH_SHORT).show();
                                rule.setActive(true);
                            } else {
                                Toast.makeText(CreateContextForResource.getContext(), getResources().getString(R.string.activity_rule_selection_toast_rule_deactivated, rule.getRuleName()), Toast.LENGTH_SHORT).show();
                                rule.setActive(false);
                            }
                            RuleCreator.changeActive(rule, checkBox.isChecked());
                            DataSource db = new DataSource(view.getContext());
                            db.saveRule(rule);
                        }
                    });
                    viewHolder.ruleName.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();

                            bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, (Rule) listView.getAdapter().getItem(position));
                            intent.putExtras(bundle);
                            intent.setClass(getActivity(), RuleSettings.class);
                            startActivity(intent);
                        }
                    });
                    viewHolder.timeControlled.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();

                            bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, (Rule) listView.getAdapter().getItem(position));
                            intent.putExtras(bundle);
                            intent.setClass(getActivity(), RuleSettings.class);
                            startActivity(intent);
                        }
                    });

//                    Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
//                    PopupMenu popupMenu = new PopupMenu(wrapper, viewHolder.ruleName);
                    viewHolder.ruleName.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View view,
                                                        ContextMenu.ContextMenuInfo menuInfo) {
                            menu.setHeaderTitle(getResources().getString(R.string.activity_rule_selection_context_menu_title));
                            menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_edit));
                            menu.add(0, view.getId(), 0, getResources().getString(R.string.test_rule));
                            menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_send));
                            menu.add(0, view.getId(), 0, getResources().getString(R.string.activity_rule_selection_context_menu_action_delete));

                            mListener.onFragmentInteraction((Rule) viewHolder.ruleName.getTag());

                        }

                    });
                }

                Rule rule = ruleList.get(position);

                if (viewHolder.ruleName != null) {
                    viewHolder.ruleName.setText(rule.getRuleName());
                    viewHolder.isRuleActivated.setChecked(rule.isActive());

                    if(rule.isAlarmEveryTime()){
                        viewHolder.timeControlled.setVisibility(View.INVISIBLE);
                    }else {

                        String timeControlled = CreateContextForResource.getStringFromID(R.string.activity_rule_selection_time_controlled) + " ";

                        DataSource db = new DataSource(getContext());

                        Set<AlarmTimeModel.Days> daysSet = new HashSet<>();

                        for(AlarmTimeModel alarmTime : db.getAlarmTimes(rule)){
                            if(!daysSet.contains(alarmTime.getDay())) {
                                timeControlled = timeControlled + AlarmTimeModel.getStringForDay(alarmTime.getDay()) + "; ";
                                daysSet.add(alarmTime.getDay());
                            }
                        }

                        if(timeControlled.length() > 40) {
                            timeControlled = timeControlled.substring(0, 40);
                            timeControlled = timeControlled + "...";
                        }

                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        viewHolder.timeControlled.setText(timeControlled);
                    }
                    /**
                     * This sets the {@link rieger.alarmsmsapp.model.rules.Rule} as tag to the ruleName and isRuleActivated,
                     * so the context menu and other methods can work with the selected rule.
                     */
                    viewHolder.ruleName.setTag(rule);
                    viewHolder.isRuleActivated.setTag(rule);

                }
                return convertView;
            }
        }

    }

    public void notifyDataSetChanced(){
        sortRuleList();
        listAdapter.notifyDataSetChanged();
    }

//    @Override
//    void onResume() {
//        super.onResume();
//
//        // Resume the AdView.
//        if(adView != null)
//            adView.resume();
//
//        prefs = PreferenceManager.getDefaultSharedPreferences(RuleSelection.this);
//        if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_RULES, true)) {
//
//            final CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.activity_rule_selection);
//            ViewTreeObserver vto = layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                    int width = layout.getMeasuredWidth();
//                    int height = layout.getMeasuredHeight();
//                    System.out.println(listView.isShown());
//
//                    if (listView.getChildAt(0) != null) {
//                        ShowcaseView showcaseView = new ShowcaseView.Builder(RuleSelection.this)
//                                .setTarget(new ViewTarget(listView.getChildAt(0).findViewById(R.id.list_item_rule_name)))
//                                .setContentTitle("Zusatzeinstellungen")
//                                .setStyle(com.github.amlcurran.showcaseview.R.style.TextAppearance_ShowcaseView_Detail_Light)
//                                .setContentText("Drücke lange auf die Regel für zusätzliche Optionen.")
//                                .hideOnTouchOutside()
//                                .blockAllTouches()
//                                .build();
//
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_RULES, false);
//                        editor.commit();
//
//                    } else {
//                        System.out.println("Element nicht vorhanden");
//                    }
//                }
//            });
//        }
//
//
//    }

}
