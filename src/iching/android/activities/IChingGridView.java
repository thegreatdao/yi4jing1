package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.IChingGridViewAdapter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;

public class IChingGridView extends Activity
{
	private ViewSwitcher viewSwitcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
		setContentView(R.layout.iching_view_switcher);
		final Locale locale = Locale.getDefault();
		setListView(iChingSQLiteDBHelper, locale);
		viewSwitcher = (ViewSwitcher) findViewById(R.id.iching_view_switcher);
		setGridView(iChingSQLiteDBHelper, locale);
		viewSwitcher.showNext();
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Swithch view");
		menu.add(0, v.getId(), 0, "grid view");
		menu.add(0, v.getId(), 0, "list view");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		viewSwitcher.showPrevious();
		return super.onOptionsItemSelected(item);
	}
	
	private ListView setListView(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale)
	{
		List<String> titles = iChingSQLiteDBHelper.selectALlTitles(locale);
		ListView listView = (ListView) findViewById(R.id.hexagrams_list_view);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.gua_title_text_view, titles));
		registerForContextMenu(listView);
		return listView;
	}
	
	private GridView setGridView(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale)
	{
		GridView gridView = (GridView)findViewById(R.id.hexagrams_grid_view);
		registerForContextMenu(gridView);
		gridView.setAdapter(new IChingGridViewAdapter(this));
		
		final Intent intent = new Intent(getApplicationContext(), Gua.class);
		
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
		return gridView;
	}
	
}
