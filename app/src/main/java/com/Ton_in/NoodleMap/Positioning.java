package com.Ton_in.NoodleMap;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.Bundle;

public class Positioning
{
	private Activity currentActivity;
	private Location currentLocation;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	public Positioning(Activity activity)
	{
		currentActivity = activity;
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
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	public void disable()
	{
		locationManager.removeUpdates(locationListener);
	}
	
	public void enableGPS()
	{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	public void disableGPS()
	{
		locationManager.removeUpdates(locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	
	protected void updateLocation(Location location)
	{
		currentLocation = location;
	}
	
	public Location getCurrentLocation()
	{
		return currentLocation;
	}
}
