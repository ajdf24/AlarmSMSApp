package rieger.alarmsmsapp.util.googleplaces;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

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
import java.util.Locale;

import rieger.alarmsmsapp.util.AppConstants;

/**
 * A {@link ArrayAdapter}, which is responsible for getting a autocomplete of
 * real navigation target from google maps with the help of the
 * Google-Places-API.
 *
 * @author sebastian
 *
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String>
		implements Filterable {
	private ArrayList<String> resultList;

    /**
     * Constructor for the adapter, which calls a constructor from the class {@link ArrayAdapter}
     * @param context the context from which the adapter is calles
     * @param textViewResourceId the id of the {@link android.widget.TextView} on which the adapter should set
     */
    public GooglePlacesAutocompleteAdapter(Context context,
			int textViewResourceId) {
		super(context, textViewResourceId);
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
	public synchronized static ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;
		HttpURLConnection connection = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder stringBuilder = new StringBuilder(
					AppConstants.PalcesAPIStrings.PLACES_API_BASE
							+ AppConstants.PalcesAPIStrings.TYPE_AUTOCOMPLETE
							+ AppConstants.PalcesAPIStrings.OUT_JSON);
			stringBuilder.append("?key=" + AppConstants.API_KEY);
			stringBuilder.append("&components=country:" + Locale.getDefault().getCountry());
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
}
