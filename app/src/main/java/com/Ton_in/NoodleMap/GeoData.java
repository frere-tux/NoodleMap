package com.Ton_in.NoodleMap;

import android.widget.*;
import org.json.JSONObject;

public class GeoData
{
	public double lattitude;
	public double longitude;
	public String text;
	public String sound;
	
	public GeoData()
	{
		lattitude = 0.0;
		longitude = 0.0;
		sound = "None";
	}
	
	public JSONObject toJSon() throws Exception
	{
		JSONObject obj = new JSONObject();
		
		obj.put("lattitude", lattitude);
		obj.put("longitude", longitude);
		obj.put("text", text);
		obj.put("sound", sound);

		return obj;
	}
}
