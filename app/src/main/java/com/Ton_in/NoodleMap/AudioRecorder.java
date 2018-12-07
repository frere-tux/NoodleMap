package com.Ton_in.NoodleMap;
import android.content.*;
import android.media.*;
import android.os.*;
import android.text.format.*;
import android.util.*;
import java.io.*;
import java.util.*;
import android.widget.*;

public class AudioRecorder
{
	Context appContext;
	MediaRecorder mediaRecorder;
	
	public AudioRecorder(Context context)
	{
		appContext = context;
		mediaRecorder = null;
	}
	
	public void Record(String fileName)
	{
		if (mediaRecorder != null)
		{
			return;
		}
		
		//String filePath = Environment.getExternalStorageDirectory()+"/"+ DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) + ".mp3";
		//String filePath = Environment.getExternalStorageDirectory()+"/" + fileName + ".mp3";
		String filePath = "/storage/emulated/0/AppProjects/NoodleMap/Audio/" + fileName + ".mp3";
		
		mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try 
		{
            mediaRecorder.prepare();
        } 
		catch (IOException e) 
		{
			mediaRecorder = null;
			Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
        }

        mediaRecorder.start();
	}
	
	public void Stop()
	{
		if (mediaRecorder != null)
		{
			mediaRecorder.stop();
        	mediaRecorder.release();
        	mediaRecorder = null;
		}
	}
	
	public boolean IsRecording()
	{
		if (mediaRecorder == null)
		{
			return false;
		}

		return true;
	}
	
	public void End()
	{
		Stop();
	}
}