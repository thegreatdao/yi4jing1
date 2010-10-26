package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.EN;
import static iching.android.persistence.IChingSQLiteDBHelper.TABLE_GUA;
import iching.android.persistence.IChingSQLiteDBHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class IChing extends Activity implements OnClickListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpListeners();
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.references_check:
				Intent referencesCheckintent = new Intent(this, IChingGridView.class);
				startActivity(referencesCheckintent);
				break;
			case R.id.cast_iching:
				IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
				String name = iChingSQLiteDBHelper.selectOne(TABLE_GUA, EN, 64);
		        Toast.makeText(IChing.this, name, Toast.LENGTH_LONG).show();
				break;
			case R.id.about:
				Intent aboutIntent = new Intent(this, About.class);
				startActivity(aboutIntent);
				break;
			case R.id.exit:
				showDialog(0);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirmation_exit_text)
		.setCancelable(false)
		.setPositiveButton(R.string.yes,
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		}).setNegativeButton(R.string.no,
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		return builder.create();
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
}