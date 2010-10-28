package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.IChingGridViewAdapter;

import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class IChingGridView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
		setContentView(R.layout.iching_grid_view);
		GridView gridView = (GridView)findViewById(R.id.hexagrams_grid_view);
		gridView.setAdapter(new IChingGridViewAdapter(this));
		final Intent intent = new Intent(getApplicationContext(), Gua.class);
		final Locale locale = Locale.getDefault();
		gridView.setOnItemClickListener(
			new OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					Map<String, String> gua = iChingSQLiteDBHelper.selectOneGua(position + 1, locale);
					intent.putExtra(GUA_BODY, gua.get(GUA_BODY));
					intent.putExtra(GUA_TITLE, gua.get(GUA_TITLE));
					intent.putExtra(GUA_ICON, gua.get(GUA_ICON));
					startActivity(intent);
				}
			}
		);
		gridView.setOnItemSelectedListener(
				new OnItemSelectedListener()
				{
		
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3)
					{
						//Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_LONG).show();
					}
		
					@Override
					public void onNothingSelected(AdapterView<?> arg0)
					{
					}
				}
		);
	}

}
