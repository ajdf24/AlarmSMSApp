package rieger.alarmsmsapp.view.ruleactivitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.SimpleShowcaseEventListener;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rieger.alarmsmsapp.R;
import rieger.alarmsmsapp.model.rules.Rule;
import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.BundleHandler;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;
import rieger.alarmsmsapp.view.MainActivity;

public class RuleSettings extends AppCompatActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	private TabLayout tabLayout;

	private Rule rule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule_settings);

		rule = BundleHandler.getRuleFromBundle(this);

		setUpToolbar();

		showCaseViews();

	}

	private void setUpToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(getResources().getString(R.string.title_activity_rule_settings)+ ": "+rule.getRuleName());
		setSupportActionBar(toolbar);

		final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
		upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
		getSupportActionBar().setHomeAsUpIndicator(upArrow);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		Bundle bundle = getIntent().getExtras();
		if(bundle.getInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER) >= 0) {
			mViewPager.setCurrentItem(bundle.getInt(AppConstants.BUNDLE_SETTINGS_TAB_NUMBER) - 1);
		}

		if(bundle.getBoolean(AppConstants.BUNDLE_SETTINGS_SHOW_TRIGGER_ALREADY_SET)){
			AlertDialog.Builder builder = new AlertDialog.Builder(RuleSettings.this);
			builder.setTitle(R.string.showcase_rule_settings_trigger_title);
			builder.setMessage(R.string.dialog_trigger_already_set_text);


			builder.setPositiveButton(R.string.general_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

				}
			});

			AlertDialog dialog = builder.create();
			dialog.show();
			dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
			dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
		}

		mViewPager.getChildAt(0);

		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(mViewPager);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				intent.setClass(RuleSettings.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	private void showCaseViews(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RuleSettings.this);
		if(prefs.getBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_SETTINGS, true)) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(AppConstants.SharedPreferencesKeys.FIRST_SHOW_SETTINGS, false);
			editor.apply();

			ShowcaseView showcaseView = new ShowcaseView.Builder(this)
					.setTarget(new ViewTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0)))
					.setContentTitle(R.string.showcase_rule_settings_trigger_title)
					.setContentText(R.string.showcase_rule_settings_trigger_text)
					.hideOnTouchOutside()
					.setStyle(R.style.CustomShowcaseTheme)
					.build();
			final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
			lps.setMargins(margin, margin, margin, margin);
			showcaseView.setButtonPosition(lps);
			showcaseView.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));

			showcaseView.setOnShowcaseEventListener(new SimpleShowcaseEventListener() {
				@Override
				public void onShowcaseViewHide(ShowcaseView showcaseView) {
					super.onShowcaseViewHide(showcaseView);
					ShowcaseView showcaseView2 = new ShowcaseView.Builder(RuleSettings.this)
							.setTarget(new ViewTarget(((ViewGroup) tabLayout.getChildAt(0)).getChildAt(1)))
							.setContentTitle(R.string.import_settings_dialog_alarm_settings)
							.setContentText(R.string.showcase_rule_settings_alarm_settings_text)
							.hideOnTouchOutside()
							.setStyle(R.style.CustomShowcaseTheme)
							.build();
					showcaseView2.setButtonPosition(lps);
					showcaseView2.setButtonText(CreateContextForResource.getStringFromID(R.string.activity_alarm_settings_alert_dialog_button));
				}
			});
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return PlaceholderFragment.newInstance(position + 1, rule);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return getString(R.string.showcase_rule_settings_trigger_title);
				case 1:
					return getString(R.string.import_settings_dialog_alarm_settings);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private final String LOG_TAG = this.getClass().getSimpleName();

		private List<Activity> activityList = new ArrayList<Activity>();

		private View rootView;

		private Intent intent;

		private Bundle bundle = new Bundle();

		@Bind(R.id.fragment_rule_settings_list)
		ListView listView;

		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";
		private static final String ARG_RULE = "rule";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber, Rule rule) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			args.putSerializable(ARG_RULE, rule);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_rule_settings, container, false);

			ButterKnife.bind(this, rootView);

			initializeGUI();

			return rootView;
		}

		/**
		 * This method initialize the all GUI elements.
		 */
		private void initializeGUI() {

			createActivityList();

			createListAdapter();
		}

		/**
		 * This method create a list with all setting activity's.
		 * </br>
		 * <b>Note</b>: If a new activity was created so this class must be added manually in this method.
		 */
		private void createActivityList() {

			if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
				activityList.add(new SenderSelection());
				activityList.add(new WordSelection());
				activityList.add(new AlarmTimeSettings());
			}

			if(getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				activityList.add(new SoundSelection());
				activityList.add(new AnswerCreation());
				activityList.add(new LightSettings());
//				activityList.add(new TwitterPostSelection());
				activityList.add(new NavigationTargetSelection());
				activityList.add(new ReadingSettings());
			}

		}

		/**
		 * This method creates the adapter for the list, which should be shown.
		 */
		private void createListAdapter() {
			ListAdapter adapter = new ArrayAdapter<Activity>(rootView.getContext(), R.layout.list_item_rule_settings, activityList);

			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {

					Log.d(LOG_TAG, listView.getAdapter().getItem(position).getClass().getName());

					intent = new Intent();

					Rule regel = (Rule) getArguments().getSerializable(ARG_RULE);

					bundle.putSerializable(AppConstants.BUNDLE_CONTEXT_RULE, regel);

					intent.putExtras(bundle);
					intent.setClassName(rootView.getContext().getPackageName(), listView.getAdapter().getItem(position).getClass().getName());
					startActivity(intent);
				}
			});


		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		rule = BundleHandler.getRuleFromBundle(this);
	}
}
