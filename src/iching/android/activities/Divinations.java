package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.*;

import java.util.Locale;
import java.util.Map;

import iching.android.R;
import iching.android.contentprovider.DivinationProvider;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.DivinationsCursorAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Divinations extends ListActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divinations);
		Cursor cursor = managedQuery(DivinationProvider.CONTENT_URI, new String[] {ID, QUESTION, ORIGINAL_ICON, RELATING_ICON}, null, null, CREATED_TIME);
        String[] from = new String[]{QUESTION, ORIGINAL_ICON, RELATING_ICON};
        int[] to = new int[] {R.id.divination_question};
        DivinationsCursorAdapter divinationsCursorAdapter = new DivinationsCursorAdapter(this, R.layout.divination_list_item, cursor, from, to);
        setListAdapter(divinationsCursorAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		Map<String, String> divination = iChingSQLiteDBHelper.selectOneDivinationByField("_id", id, Locale.getDefault());
		Intent divinationIntent = new Intent(getBaseContext(), Divination.class);
		divinationIntent.putExtra(QUESTION, divination.get(QUESTION));
		divinationIntent.putExtra(ORIGINAL_LINES, divination.get(ORIGINAL_LINES));
		divinationIntent.putExtra(CHANGING_LINES, divination.get(CHANGING_LINES));
		startActivity(divinationIntent);
	}
	
}
