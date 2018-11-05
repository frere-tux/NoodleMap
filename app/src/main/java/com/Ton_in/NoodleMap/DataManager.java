package com.Ton_in.NoodleMap;

import java.util.*;
import android.location.*;

public class DataManager
{
	private List<GeoData> geoDataList;
	private Positioning positioning;

	public DataManager(Positioning posManager)
	{
		geoDataList = new ArrayList<GeoData>();
		positioning = posManager;
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
}
