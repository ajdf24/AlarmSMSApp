package rieger.alarmsmsapp.util.googleplaces;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

import rieger.alarmsmsapp.util.AppConstants;
import rieger.alarmsmsapp.util.standard.CreateContextForResource;

/**
 * This class contains different methods for calculating a distance between to points.
 * Created by sebastian on 14.03.15.
 */
public class DistanceCalculator {

    /**
     * This method calculate the distance between the current location and the given location in meter.
     *
     * <p> The computed distance is stored in results[0].  If results has length
     * 2 or greater, the initial bearing is stored in results[1]. If results has
     * length 3 or greater, the final bearing is stored in results[2].
     *
     * @return the distance as array
     */
    public static float[] calculateDistance(String address) {
        float[] results = new float[3];

        double addressLatitude = 0;
        double addressLongitude = 0;

        Geocoder geocoder = new Geocoder(CreateContextForResource.getContext());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(address, 1);

            if (addresses.size() > 0) {
                addressLatitude = addresses.get(0).getLatitude();
                addressLongitude = addresses.get(0).getLongitude();
            }
            if (ActivityCompat.checkSelfPermission(CreateContextForResource.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) CreateContextForResource.getContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, false);

                Location CurrentLocation = locationManager.getLastKnownLocation(provider);
                Location.distanceBetween(addressLatitude, addressLongitude, CurrentLocation.getLatitude(), CurrentLocation.getLongitude(), results);
            }
            //TODO: Meldung????

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }


        return results;
    }




}
