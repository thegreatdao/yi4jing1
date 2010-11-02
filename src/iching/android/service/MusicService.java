package iching.android.service;

import iching.android.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service
{

	private  MediaPlayer mediaPlayer;
	
	@Override
	public void onCreate()
	{
		mediaPlayer = MediaPlayer.create(this, R.raw.bg);
	}

	@Override
	public void onDestroy()
	{
		mediaPlayer.stop();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		mediaPlayer.start();
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}
