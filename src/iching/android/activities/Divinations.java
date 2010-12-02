package iching.android.activities;

import iching.android.R;
import iching.android.contentprovider.DivinationProvider;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static iching.android.persistence.IChingSQLiteDBHelper.*;

public class Divinations extends ListActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divinations);
		setListView();
		Cursor cursor = getContentResolver().query(DivinationProvider.CONTENT_URI, new String[] {ID, QUESTION}, null, null, CREATED_TIME);
        startManagingCursor(cursor);
        String[] from = new String[]{QUESTION};
        int[] to = new int[] {R.id.divination_question};
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.divination_list_item, cursor, from, to);
        setListAdapter(simpleCursorAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Toast.makeText(this, position + " " + id, Toast.LENGTH_LONG).show();
		/*Intent divinationIntent = new Intent(getBaseContext(), Divination.class);
		startActivity(divinationIntent);*/
	}

	private void setListView()
	{
/*		List<iching.android.bean.Divination> divinations = iChingSQLiteDBHelper.selectAllDivinations();
		setListAdapter(new DivinationAdapter(this, R.layout.divination_list_item, divinations));*/
	}
	
	/*private class DivinationAdapter extends ArrayAdapter<iching.android.bean.Divination>
	{

		private List<iching.android.bean.Divination> divinations;
		
		public DivinationAdapter(Context context, int textViewResourceId, List<iching.android.bean.Divination> divinations)
		{
			super(context, textViewResourceId, divinations);
			this.divinations = divinations;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
	        if (view == null)
	        {
	            LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = vi.inflate(R.layout.divination_list_item, null);
	        }
	        iching.android.bean.Divination divination = divinations.get(position);
	        if (divination != null)
	        {
	                TextView hexagramText = (TextView) view.findViewById(R.id.divination_question);
	                hexagramText.setText(divination.getQuestion());
	                Locale locale = Locale.getDefault();
	                String originalCode = "'" + divination.getOriginalLines() + "'";
	                Map<String, String> gua = iChingSQLiteDBHelper.selectOneGuaByField(GUA_CODE, originalCode, locale);
	                ImageView originalIcon = (ImageView)view.findViewById(R.id.original_lines);
					setIcon(gua, originalIcon);
	                String changingLines = divination.getChangingLines();
	                if(changingLines.trim().length() != 0)
	                {
	                	String relatingCode = "'" + getRelatingCode(divination.getOriginalLines(), changingLines) + "'";
	                	gua = iChingSQLiteDBHelper.selectOneGuaByField(GUA_CODE, relatingCode, locale);
	                	ImageView relatingIcon = (ImageView)view.findViewById(R.id.relating_lines);
	                	setIcon(gua, relatingIcon);
	                }
	        }
	        return view;
		}
		
		private void setIcon(Map<String, String> gua, ImageView icon)
		{
			Integer index = Integer.parseInt(gua.get(ID)) - 1;
            icon.setImageResource(IChingView.HEXAGRAM_ICONS[index]);
		}
		
		private String getRelatingCode(String originalCode, String changingLines)
		{
			char[] originalCodeArray = originalCode.toCharArray();
			char[] changingLineArray = changingLines.toCharArray();
			for(char digit : changingLineArray)
			{
				int index = Integer.parseInt(digit + "") - 1;
				char lineAtIndex = originalCodeArray[index];
				if(lineAtIndex == '1')
				{
					originalCodeArray[index] = '0';
				}
				else
				{
					originalCodeArray[index] = '1';
				}
			}
			return new String(originalCodeArray);
		}
	}*/
}
