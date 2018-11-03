package com.Ton_in.NoodleMap;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	private TextView positionText;
	private TextView infoText;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		positionText = findViewById(R.id.positionText);
		infoText = findViewById(R.id.infoText);
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				updateLocation(location);
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};

// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
	
	protected void updateLocation(Location location)
	{
		String positionString = location.getLatitude() + " - " + location.getLongitude();
		positionText.setText(positionString);
		
		String infoString =  location.getProvider() + " - " + location.getTime();
		infoText.setText(infoString);
	}
}
