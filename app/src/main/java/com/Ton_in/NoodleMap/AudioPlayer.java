package com.Ton_in.NoodleMap;
import android.content.*;
import android.media.*;
import android.net.*;
import android.widget.*;
import org.apache.http.client.utils.*;
import java.io.*;

public class AudioPlayer
{
	Context appContext;
	MediaPlayer mediaPlayer;
	
	int maxDistance;
	
	public AudioPlayer(Context context)
	{
		appContext = context;
		mediaPlayer = null;
		maxDistance = 0;
	}
	
	public void Play(String path)
	{
		Play(path, false);
	}
	
	public void Play(String path, boolean loop)
	{
		mediaPlayer = new MediaPlayer();
		
		if (mediaPlayer.isPlaying())
		{
			mediaPlayer.reset();
		}
		
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		
		try
		{
			FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(path);
            mediaPlayer.setDataSource(fileInputStream.getFD());
            fileInputStream.close();
				
			mediaPlayer.prepare();
			mediaPlayer.setLooping(loop);
			mediaPlayer.start();
			
			Toast.makeText(appContext, "Playing: " + path, Toast.LENGTH_SHORT).show();
		}
		catch (Exception e)
		{
			Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void Stop()
	{
		try 
		{
            if (mediaPlayer != null) 
			{
                mediaPlayer.stop();
				mediaPlayer.reset();
                mediaPlayer.release();

                mediaPlayer = null;
            }

        } 
		catch (Exception e) 
		{
            mediaPlayer = null;
         	Toast.makeText(appContext, e.toString(), Toast.LENGTH_LONG).show();  
        }
	}
	
	public void SetVolumeByDistance(int distance)
	{
		if (mediaPlayer == null)
			return;
			
		float log;
		
		if (distance == maxDistance)
		{
			log = 0.0f;
		}
		else 
		{
			float logLeft = (float)Math.log(maxDistance - distance);
			float logMaxDist = (float)Math.log(maxDistance);
			log = (logLeft / logMaxDist);
		}
	
		mediaPlayer.setVolume(1.0f - log, 1.0f - log);
	
		// /!\ Toast.makeText(appContext, "Distance: " + distance + " - Volume: " + (1.0f - log), Toast.LENGTH_SHORT).show();
	}
	
	public void SetMaxDistance(int distance)
	{
		maxDistance = distance;
	}
	
	public boolean IsPlaying()
	{
		if (mediaPlayer == null)
		{
			return false;
		}
		
		return mediaPlayer.isPlaying();
	}
}
