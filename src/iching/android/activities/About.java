package iching.android.activities;

import iching.android.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

public class About extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setTitleColor(Color.rgb(41, 40, 41));
	}
	
}
