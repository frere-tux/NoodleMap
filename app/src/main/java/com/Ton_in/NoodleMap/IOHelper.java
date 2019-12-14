package com.Ton_in.NoodleMap;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;

public class IOHelper
{
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception
	{
		File fl = new File(filePath);
		
		if (!fl.exists())
		{
			return null;
		}
		
		FileInputStream fin = new FileInputStream(fl);
		String ret = convertStreamToString(fin);
		fin.close();        
		return ret;
	}
	
	public static  void writeStringToFile(String filePath, String content) throws Exception
	{
		File file = new File(filePath);

		FileOutputStream fos = new FileOutputStream(file); 
		fos.write(content.getBytes(), 0, content.length());
		fos.close();
	}
}
