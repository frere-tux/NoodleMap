package com.Ton_in.NoodleMap;

import android.app.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.content.*;

public class MainActivity extends Activity 
{
	private TextView positionText;
	private TextView infoText;
	private Switch positionSwitch;
	private TextView logText;
	private EditText dataText;
	private Button dataButton;
	private LinearLayout dataLayout;
	
	LayoutInflater layoutInflater;
	
	private Positioning positioning;
	private DataManager dataManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		
		positionText = findViewById(R.id.positionText);
		infoText = findViewById(R.id.infoText);
		positionSwitch = findViewById(R.id.positionSwitch);
		logText = findViewById(R.id.logText);
		dataText = findViewById(R.id.dataText);
		dataButton = findViewById(R.id.dataButton);
		dataLayout = findViewById(R.id.dataLayout);
		
		layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		positioning = new Positioning(this, logText);
		positioning.init();
		positioning.enable();
		
		dataManager = new DataManager(positioning);
		
		positionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				if (isChecked) 
				{
					positioning.enableGPS();
				} 
				else 
				{
					positioning.disableGPS();
				}
			}
		});
			
		dataButton.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				saveData();
			}
		});
		
			
		final Handler handler=new Handler();
		handler.post(new Runnable(){ 
			@Override
			public void run() {
				update();
				handler.postDelayed(this, 250);
			}
		});
	}
	
	public void update()
	{
		Location currentLocation = positioning.getCurrentLocation();
		
		if (currentLocation != null)
		{
			String positionString = currentLocation.getLatitude() + " - " + currentLocation.getLongitude();
			positionText.setText(positionString);
		
			String infoString =  currentLocation.getProvider() + " - " + currentLocation.getTime() + " - " + currentLocation.getSpeed();
			infoText.setText(infoString);
		}
		
		dataLayout.removeAllViews();
		
		List<GeoData> data = dataManager.getGeoData();
		
		for (GeoData geoData : data) 
		{
			View v = layoutInflater.inflate(R.layout.data, null);

			TextView contentText = v.findViewById(R.id.contentText);
			contentText.setText(geoData.text);
			
			float distance = positioning.distanceToCurrentLocation(geoData.lattitude, geoData.longitude);
			TextView distanceText = v.findViewById(R.id.distanceText);
			distanceText.setText(distance + "m");

			dataLayout.addView(v);
		}
	}
	
	public void saveData()
	{
		String dataString = dataText.getText().toString();
		if (dataString == "")
		{
			return;
		}
		
		dataText.setText("");
		
		dataManager.addGeoData(dataString);
		
		logText.setText("Data saved");
		
		update();
	}
} 
