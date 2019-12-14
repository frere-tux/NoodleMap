package com.Ton_in.NoodleMap;

import java.util.*;
import android.location.*;
import android.content.*;
import org.json.JSONArray;
import org.json.JSONObject;
import android.widget.*;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;

public class DataManager
{
	private List<GeoData> geoDataList;
	private Positioning positioning;
	private Context appContext;

	public DataManager(Context context, Positioning posManager)
	{
		appContext = context;
		geoDataList = new ArrayList<GeoData>();
		positioning = posManager;
	}
	
	public void init()
	{
		loadGeoData();
	}
	
	public List<GeoData> getGeoData()
	{
		return geoDataList;
	}

	public void addGeoData(String text)
	{
		if (text != "")
		{
			Location currentLocation = positioning.getCurrentLocation();
			GeoData data = new GeoData();
			data.lattitude = currentLocation.getLatitude();
			data.longitude = currentLocation.getLongitude();
			data.text = text;
			
			geoDataList.add(data);
		}
	}
	
	public void saveGeoData()
	{
		JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		
		try
		{
			for (GeoData data : geoDataList)
			{
				array.put(data.toJSon());
			}
			
			obj.put("data", array);
			
			IOHelper.writeStringToFile("/storage/emulated/0/Projects/NoodleMap/Data/GeoData.json", obj.toString());
		}
		catch (Exception e)
		{
			Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void loadGeoData()
	{
		geoDataList.clear();
		try
		{
			String fileContent = IOHelper.getStringFromFile("/storage/emulated/0/Projects/NoodleMap/Data/GeoData.json");
			
			if (fileContent == null)
			{
				return;
			}
			
			JSONObject jsonObject = new JSONObject(fileContent);
			JSONArray array = jsonObject.getJSONArray("data");
			if (array != null)
			{
				for(int i=0 ; i<array.length() ; i++)
				{
					JSONObject jsonData = array.getJSONObject(i);
					
					GeoData data = new GeoData();
					data.lattitude = jsonData.getDouble("lattitude");
					data.longitude = jsonData.getDouble("longitude");
					data.text = jsonData.getString("text");
					data.sound = jsonData.getString("sound");
					
					geoDataList.add(data);
				}
			}
			
		}
		catch (Exception e)
		{
			Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
