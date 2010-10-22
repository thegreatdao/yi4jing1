package iching.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class IChing extends Activity implements OnClickListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpListeners();
	}
	
	private void setUpListeners()
	{
		View referencesCheckButton = findViewById(R.id.references_check);
		referencesCheckButton.setOnClickListener(this);
		View castIChingButton = findViewById(R.id.cast_iching);
		castIChingButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit);
		exitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.references_check:
				break;
			case R.id.cast_iching:
				break;
			case R.id.about:
				Intent intent = new Intent(this, About.class);
				startActivity(intent);				
				break;
			case R.id.exit:
				finish();
				break;
			default:
				break;
		}
	}
}