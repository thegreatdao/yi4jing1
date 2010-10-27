package iching.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Gua extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gua);
        Bundle extras = getIntent().getExtras();
        String data = extras.getString("guaContent");
        TextView textView = (TextView)findViewById(R.id.gua_content);
        textView.setText(data);
	}
	
}
