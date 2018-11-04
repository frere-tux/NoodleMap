package com.Ton_in.NoodleMap;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.Bundle;
import android.widget.*;

public class Positioning
{
	private Activity currentActivity;
	private Location currentLocation;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private TextView logText;
	
	private static final int DEPRECATED_DURATION = 1000 * 60 * 2;
	private static final long UPDATE_TIME = 0;
	private static final float UPDATE_DISTANCE = 0;
	
	public Positioning(Activity activity, TextView text)
	{
		currentActivity = activity;
		logText = text;
	}
	
	public void init()
	{
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) currentActivity.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				updateLocation(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};
		
		currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}
	
	public void enable()
	{
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, locationListener);
	}
	
	public void disable()
	{
		locationManager.removeUpdates(locationListener);
	}
	
	public void enableGPS()
	{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, UPDATE_DISTANCE, locationListener);
	}

	public void disableGPS()
	{
		locationManager.removeUpdates(locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	protected void updateLocation(Location location)
	{
		if (isBetterLocation(location, currentLocation))
		{
			currentLocation = location;
		} 
		else
		{
			logText.setText("Location update ignored (" + location.getTime() + ")");
		}
	}
	
	public Location getCurrentLocation()
	{
		return currentLocation;
	}

	/** Determines whether one Location reading is better than the current Location fix
	 * @param location  The new Location that you want to evaluate
	 * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	 */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) 
	{
		if (currentBestLocation == null) 
		{
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > DEPRECATED_DURATION;
		boolean isSignificantlyOlder = timeDelta < -DEPRECATED_DURATION;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer)
		{
			return true;
			// If the new location is more than two minutes older, it must be worse
		} 
		else if (isSignificantlyOlder) 
		{
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) 
		{
			return true;
		} 
		else if (isNewer && !isLessAccurate) 
		{
			return true;
		} 
		else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) 
		{
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) 
	{
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}
}
