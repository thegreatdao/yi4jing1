package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import iching.android.R;
import iching.android.bean.Hexagram;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.AdapterCommons;
import iching.android.viewadapters.IChingGridViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class IChingView extends Activity
{
	private ViewSwitcher viewSwitcher;
	private boolean isGridView;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);		
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
		this.iChingSQLiteDBHelper = iChingSQLiteDBHelper;
		setContentView(R.layout.iching_view_switcher);
		final Locale locale = Locale.getDefault();
		setListView(iChingSQLiteDBHelper, locale);
		viewSwitcher = (ViewSwitcher) findViewById(R.id.iching_view_switcher);
		setGridView(iChingSQLiteDBHelper, locale);
		isGridView = Preferences.getStringValue(this).equals("0");
		if(!isGridView)
		{
			viewSwitcher.showPrevious();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo)
	{
		super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
		contextMenu.setHeaderTitle(R.string.switch_view);
		contextMenu.add(Menu.NONE, view.getId(), Menu.NONE, R.string.grid_view);
		contextMenu.add(Menu.NONE, view.getId(), Menu.FIRST, R.string.list_view);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem menuItem)
	{
		int itemOrder = menuItem.getOrder();
		if(itemOrder == Menu.FIRST && isGridView)
		{
			isGridView = Boolean.FALSE;
			viewSwitcher.showPrevious();
		}
		if(itemOrder == Menu.NONE && !isGridView)
		{
			isGridView = Boolean.TRUE;
			viewSwitcher.showPrevious();
		}
		return super.onOptionsItemSelected(menuItem);
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		iChingSQLiteDBHelper.close();
	}
	
	private ListView setListView(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale)
	{
		List<String> titles = iChingSQLiteDBHelper.selectALlTitles(locale);
		ListView listView = (ListView) findViewById(R.id.hexagrams_list_view);
		listView.setAdapter(new HexagramAdapter(this, R.layout.list_item, getHexagrams(titles, AdapterCommons.HEXAGRAM_ICONS)));
		registerForContextMenu(listView);
		setOnItemClickListener(iChingSQLiteDBHelper, locale, listView);
		return listView;
	}
	
	private ArrayList<Hexagram> getHexagrams(List<String> titles, Integer[] icons)
	{		
		ArrayList<Hexagram> result = new ArrayList<Hexagram>();
		int index = 0;
		for(String title : titles)
		{
			Hexagram hexagram = new Hexagram();
			hexagram.setTitle(title);
			hexagram.setIcon(icons[index]);
			result.add(hexagram);			
			index++;
		}
		return result;
	}
	
	private GridView setGridView(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale)
	{
		GridView gridView = (GridView)findViewById(R.id.hexagrams_grid_view);
		registerForContextMenu(gridView);
		gridView.setAdapter(new IChingGridViewAdapter(this));
		setOnItemClickListener(iChingSQLiteDBHelper, locale, gridView);
		return gridView;
	}
	
	private void setOnItemClickListener(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale, AdapterView<?> adapterView)
	{
		adapterView.setOnItemClickListener(
			new OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					final Intent intent = new Intent(getApplicationContext(), Gua.class);
					setUpHexagram(iChingSQLiteDBHelper, locale, intent,
							position);
				}

				private void setUpHexagram(
						final IChingSQLiteDBHelper iChingSQLiteDBHelper,
						final Locale locale, final Intent intent, int position)
				{
					Map<String, String> gua = iChingSQLiteDBHelper.selectOneGua(position + 1, locale);
					intent.putExtra(GUA_BODY, gua.get(GUA_BODY));
					intent.putExtra(GUA_TITLE, gua.get(GUA_TITLE));
					intent.putExtra(GUA_ICON, gua.get(GUA_ICON));
					startActivity(intent);
				}
			}
		);
	}
	
	private class HexagramAdapter extends ArrayAdapter<Hexagram>
	{

		private ArrayList<Hexagram> hexagrams;
		
		public HexagramAdapter(Context context, int textViewResourceId, ArrayList<Hexagram> hexagrams)
		{
			super(context, textViewResourceId, hexagrams);
			this.hexagrams = hexagrams;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
	        if (view == null)
	        {
	            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = vi.inflate(R.layout.list_item, null);
	        }
	        Hexagram hexagram = hexagrams.get(position);
	        if (hexagram != null)
	        {
	                TextView hexagramText = (TextView) view.findViewById(R.id.hexagram_text);
	                hexagramText.setText(hexagram.getTitle());
	                ImageView hexagramIcon = (ImageView)view.findViewById(R.id.hexagram_icon);
	                hexagramIcon.setImageResource(R.drawable.icon);
	        }
	        return view;
		}

	}
	
}
