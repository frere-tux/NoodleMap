package com.Ton_in.NoodleMap;

import android.app.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	private TextView positionText;
	private TextView infoText;
	private Switch positionSwitch;
	
	private Positioning positioning;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		
		TextView logText = findViewById(R.id.positionText);
		positioning = new Positioning(this, logText);
		positioning.init();
		positioning.enable();
		
		positionText = findViewById(R.id.positionText);
		infoText = findViewById(R.id.infoText);
		positionSwitch = findViewById(R.id.positionSwitch);
		
		positionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
			
		final Handler handler=new Handler();
		handler.post(new Runnable(){ 
			@Override
			public void run() {
				update();
				handler.postDelayed(this,500); // set time here to refresh textView
			}
		});
	}
	
	public void update()
	{
		Location location = positioning.getCurrentLocation();
		
		if (location != null)
		{
			String positionString = location.getLatitude() + " - " + location.getLongitude();
			positionText.setText(positionString);
		
			String infoString =  location.getProvider() + " - " + location.getTime() + " - " + location.getSpeed();
			infoText.setText(infoString);
		}
	}
}
