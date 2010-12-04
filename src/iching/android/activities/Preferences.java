package iching.android.activities;

import iching.android.R;
import iching.android.service.MusicControl;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static final String KEY_MUSIC = "musicPref";
	private static final boolean DEFAULT_VALUE_MUSIC = Boolean.TRUE;
	public static final String KEY_VIEW = "viewPref";
	public static final String DEFAULT_VALUE_VIEW = "0";
	public static final String KEY_DIVINATIONS = "numOfRecordsPref";
	public static final String DEFAULT_VALUE_DIVINATIONS = "10";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
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
		else if(key.equals(KEY_VIEW))
		{
			Toast.makeText(this, isMusicOn(this) + "", Toast.LENGTH_SHORT).show();
		}
		else if(key.equals(KEY_DIVINATIONS))
		{
			Toast.makeText(this, isMusicOn(this) + "", Toast.LENGTH_SHORT).show();
		}
	}

	public static boolean isMusicOn(Context context)
	{
		return getSharedPreferences(context).getBoolean(KEY_MUSIC, DEFAULT_VALUE_MUSIC);
	}
	
	public static String getStringValue(Context context, String key, String defaultValue)
	{
		return getSharedPreferences(context).getString(key, defaultValue);
	}
	
	public static int getIntValue(Context context, String key, int defaultValue)
	{
		return getSharedPreferences(context).getInt(key, defaultValue);
	}
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
