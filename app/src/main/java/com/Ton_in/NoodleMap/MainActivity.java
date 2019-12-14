package com.Ton_in.NoodleMap;

import android.app.*;
import android.content.*;
import android.location.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class MainActivity extends Activity 
{
	private TextView positionText;
	private TextView infoText;
	private Switch positionSwitch;
	private TextView logText;
	private EditText dataText;
	private Button dataButton;
	private LinearLayout dataLayout;
	private SeekBar distanceBar;
	private Button recordButton;
	
	LayoutInflater layoutInflater;
	
	private Positioning positioning;
	private DataManager dataManager;
	private AudioPlayer audioPlayer;
	private AudioRecorder audioRecorder;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		initInterface();
		
		positioning = new Positioning(this, logText);
		positioning.init();
		positioning.enable();
		
		dataManager = new DataManager(getApplicationContext(), positioning);
		dataManager.init();
		
		PositionSwitchInit();
			
		DataButtonInit();
			
		UpdateHandlerInit();
		
		DistanceBarInit();
		
		AudioPlayerInit();
		
		AudioRecorderInit();
		
		RecordButtonInit();
	}

	private void initInterface()
	{
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

		positionText = findViewById(R.id.positionText);
		infoText = findViewById(R.id.infoText);
		positionSwitch = findViewById(R.id.positionSwitch);
		logText = findViewById(R.id.logText);
		dataText = findViewById(R.id.dataText);
		dataButton = findViewById(R.id.dataButton);
		dataLayout = findViewById(R.id.dataLayout);
		distanceBar = findViewById(R.id.distanceBar);
		recordButton = findViewById(R.id.recordButton);

		layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private void RecordButtonInit()
	{
		recordButton.setOnClickListener(new Button.OnClickListener()
			{
				public void onClick(View v)
				{
					OnClickRecord();
				}
			});
	}

	private void AudioPlayerInit()
	{
		audioPlayer = new AudioPlayer(getApplicationContext());
		audioPlayer.SetMaxDistance(100);
	}
	
	private void AudioRecorderInit()
	{
		audioRecorder = new AudioRecorder(getApplicationContext());
	}

	private void DistanceBarInit()
	{
		distanceBar.setProgress(distanceBar.getMax());
		distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() 
			{

				@Override
				public void onStartTrackingTouch(SeekBar p1)
				{
				}

				@Override
				public void onStopTrackingTouch(SeekBar p1)
				{
					//Toast.makeText(getApplicationContext(), "distance: " + p1.getProgress(), Toast.LENGTH_SHORT).show();
				}

				int progressChangedValue = 0;

				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
					audioPlayer.SetVolumeByDistance(progress);
				}
			});
	}

	private void UpdateHandlerInit()
	{
		final Handler handler=new Handler();
		handler.post(new Runnable(){ 
				@Override
				public void run()
				{
					update();
					handler.postDelayed(this, 250);
				}
			});
	}

	private void DataButtonInit()
	{
		dataButton.setOnClickListener(new Button.OnClickListener()
			{
				public void onClick(View v)
				{
					saveData();
				}
			});
	}

	private void PositionSwitchInit()
	{
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
	}
	
	public void update()
	{
		Location currentLocation = positioning.getCurrentLocation();
		
		if (currentLocation != null)
		{
			String positionString = currentLocation.getLatitude() + " - " + currentLocation.getLongitude();
			positionText.setText(positionString);
		
			String infoString =  currentLocation.getProvider() + " - " + currentLocation.getTime() + " - " + currentLocation.getSpeed() + " - " + currentLocation.getAccuracy();
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
		if (dataString.length() == 0)
		{
			if (audioPlayer.IsPlaying())
			{
				audioPlayer.Stop();
			}
			else
			{
				String filePath = "/storage/emulated/0/Projects/NoodleMap/recordTest.3gp";
				audioPlayer.Play(filePath, true);
				audioPlayer.SetVolumeByDistance(distanceBar.getProgress());
			}
			
			return;
		}
		
		dataText.setText("");
		
		dataManager.addGeoData(dataString);
		
		logText.setText("Data saved");
		
		update();
	}
	
	@Override
    public void onStop() 
	{
        audioRecorder.End();
		
		audioPlayer.End();
    }
	
	private void OnClickRecord()
	{
		dataManager.saveGeoData();
		if (audioRecorder.IsRecording())
		{
			audioRecorder.Stop();
			recordButton.setText("Rec");
		}
		else
		{
			audioRecorder.Record("recordTest");
			recordButton.setText("Stop");
		}
	}
} 
