package rieger.alarmsmsapp.util.googleplaces;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A {@link ArrayAdapter}, which is responsible for getting a autocomplete of
 * real navigation targets from google maps with the help of the
 * Google-Places-API.
 *
 * @author sebastian
 *
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String>
		implements Filterable {

	/**
	 * Do not filter the list by a country.
	 * <note>Note:</note> The list is filtered by the google service so if you use ths flag is the amount of loaded data really high.
	 */
	public static final int DONT_FILTER_RESULTS = 1;

	/**
	 * Do a filtering by the given country.
	 * <note>Note:</note> Do not forget to set {@link GooglePlacesAutocompleteAdapter#location} to a valid ISO-3166 shortcut.
	 */
	public static final int FILTER_BY_GIVEN_LOCATION = 2;

	/**
	 * Filter automatically by the current country.
	 */
	public static final int AUTO_FILTER_BY_THE_CURRENT_COUNTRY = 3;

	/**
	 * The list with the results
	 */
	private ArrayList<String> resultList;

	/**
	 * The api key for this app
	 */
	private static final String API_KEY = "AIzaSyBwLNUi7T_maJpyJldrCvYRuvz5k1ILp8w";

	/**
	 * A flag which shows, which location filter should be used.
	 */
	private int locationFilter = 0;

	/**
	 * The location on which a filtering are based.
	 */
	private String location;

	/**
	 * Constructor for the adapter, which calls a constructor from the class {@link ArrayAdapter}
	 * @param context the context from which the adapter is calles
	 * @param textViewResourceId the id of the {@link android.widget.TextView} on which the adapter should set
	 * @param locationFilter a flag, which shows how to filter locations
	 */
	public GooglePlacesAutocompleteAdapter(Context context,
										   int textViewResourceId, int locationFilter) {
		super(context, textViewResourceId);
		this.locationFilter = locationFilter;
	}

	/**
	 * Constructor for the adapter, which calls a constructor from the class {@link ArrayAdapter}
	 * @param context the context from which the adapter is calles
	 * @param textViewResourceId the id of the {@link android.widget.TextView} on which the adapter should set
	 * @param locationFilter a flag, which shows how to filter locations
	 * @param location a ISO-3166 name of a country which should be used.
	 * <note>Note:</note> the param location is only used with the locationFilter flag {@link}
	 */
	public GooglePlacesAutocompleteAdapter(Context context,
										   int textViewResourceId, int locationFilter, String location) {
		super(context, textViewResourceId);
		this.locationFilter = locationFilter;
		this.location = location;
	}

	/**
	 * {@inheritDoc}
	 * @return the size of the results
	 */
	@Override
	public int getCount() {
		return resultList.size();
	}

	/**
	 * {@inheritDoc}
	 * @param index the index of a item
	 * @return the index of the item in the internal list
	 */
	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	/**
	 * {@inheritDoc}
	 * @return a {@link Filter} for the results
	 */
	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

			/**
			 * {@inheritDoc}
			 *
			 * It is <code>synchronized</code> because of the multithreading problem.
			 * @param constraint the constraint which should be filtered
			 * @return the result after filtering
			 */
			@Override
			protected synchronized FilterResults performFiltering(
					CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());
					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			/**
			 * {@inheritDoc}
			 *
			 * It is <code>synchronized</code> because of the multithreading problem.
			 * @param constraint the constraint which should be filtered
			 * @param results the results after filtering
			 */
			@Override
			protected synchronized void publishResults(CharSequence constraint,
													   FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
		return filter;
	}

	/**
	 * This method complete a {@link String} with the help of google places API.
	 * It is <code>synchronized</code> because of the multithreading problem.
	 *
	 * @param input string which should complete
	 * @return a array with matching strings
	 */
	public synchronized ArrayList<String> autocomplete(String input) {

		ArrayList<String> resultList = null;
		HttpURLConnection connection = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder stringBuilder = new StringBuilder(
					PalcesAPIStrings.PLACES_API_BASE
							+ PalcesAPIStrings.TYPE_AUTOCOMPLETE
							+ PalcesAPIStrings.OUT_JSON);
			stringBuilder.append("?key=" + API_KEY);

			switch (locationFilter){
				case AUTO_FILTER_BY_THE_CURRENT_COUNTRY: {
					TelephonyManager tm = (TelephonyManager) CreateContextForResource.getContext().getSystemService(CreateContextForResource.getContext().TELEPHONY_SERVICE);
					String countryCode = tm.getNetworkCountryIso();
					stringBuilder.append("&components=country:" + countryCode);
					break;
				}
				case DONT_FILTER_RESULTS:{
					//don't set a country here
					break;
				}
				case FILTER_BY_GIVEN_LOCATION:{
					if (location != null){
						stringBuilder.append("&components=country:" + location);
					}else {
						Log.e(AppConstants.DEBUG_TAG, "no location set. use default");
					}
					break;
				}
				default:{
					Log.e(AppConstants.DEBUG_TAG, "no locationFlag set. use default");
					break;
				}
			}

			stringBuilder.append("&input=" + URLEncoder.encode(input, "utf8"));
			URL url = new URL(stringBuilder.toString());
			connection = (HttpURLConnection) url.openConnection();
			InputStreamReader inputStream = new InputStreamReader(
					connection.getInputStream());
			// Load the results into a StringBuilder
			int read;
			char[] buffer = new char[1024];
			while ((read = inputStream.read(buffer)) != -1) {
				jsonResults.append(buffer, 0, read);
			}


		} catch (MalformedURLException e) {
			Log.e(AppConstants.DEBUG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(AppConstants.DEBUG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObject = new JSONObject(jsonResults.toString());
			JSONArray predictionsJsonArray = jsonObject
					.getJSONArray("predictions");
			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predictionsJsonArray.length());
			for (int i = 0; i < predictionsJsonArray.length(); i++) {
				resultList.add(predictionsJsonArray.getJSONObject(i).getString(
						"description"));
			}
		} catch (JSONException e) {
			Log.e(AppConstants.DEBUG_TAG, "Cannot process JSON results", e);
		}
		return resultList;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Strings, which are used to create a {@link URL} for the PlacesAPI-Request.
	 * @author sebastian
	 *
	 */
	private interface PalcesAPIStrings{

		/**
		 * Base url
		 */
		String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

		/**
		 * Type autocomplete
		 */
		String TYPE_AUTOCOMPLETE = "/autocomplete";

		/**
		 * Output type as json
		 */
		String OUT_JSON = "/json";

	}
}
