package iching.android.activities;

import iching.android.persistence.IChingSQLiteDBHelper;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class IChingGridView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
		setContentView(R.layout.iching_list_view);
		final Intent intent = new Intent(getApplicationContext(), Gua.class);
		final Locale locale = Locale.getDefault();
		List<String> titles = iChingSQLiteDBHelper.selectALlTitles(locale);
		ListView listView = (ListView) findViewById(R.id.hexagrams_list_view);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.gua_title_text_view, titles));
		/*setContentView(R.layout.iching_grid_view);
		GridView gridView = (GridView)findViewById(R.id.hexagrams_grid_view);
		registerForContextMenu(gridView);
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
		);*/
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Swithch view");
		menu.add(0, v.getId(), 0, "grid view");
		menu.add(0, v.getId(), 0, "list view");
	}

}
