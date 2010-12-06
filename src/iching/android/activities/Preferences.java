package iching.android.activities;

import iching.android.R;
import iching.android.service.MusicControl;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static final String KEY_MUSIC = "musicPref";
	private static final boolean DEFAULT_VALUE_MUSIC = Boolean.TRUE;
	private static final String KEY_VIEW = "viewPref";
	private static final String DEFAULT_VALUE_VIEW = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	public static boolean isMusicOn(Context context)
	{
		return getSharedPreferences(context).getBoolean(KEY_MUSIC, DEFAULT_VALUE_MUSIC);
	}
	
	public static String getStringValue(Context context)
	{
		return getSharedPreferences(context).getString(KEY_VIEW, DEFAULT_VALUE_VIEW);
	}
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if(key.equals(KEY_MUSIC))
		{
			if(isMusicOn(this))
			{
				MusicControl.play(this, R.raw.bg);				
			}
			else
			{
				MusicControl.stop(this);
			}
		}
	}


}
