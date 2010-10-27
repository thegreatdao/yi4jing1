package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.*;

import java.util.Locale;

import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.IChingGridViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class IChingGridView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this);
		setContentView(R.layout.iching_grid_view);
		GridView gridview = (GridView)findViewById(R.id.ichingGridView);
		gridview.setAdapter(new IChingGridViewAdapter(this));
		final Intent intent = new Intent(getApplicationContext(), Gua.class);
		
		gridview.setOnItemClickListener(
			new OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					id = position + 1;
					Locale locale = Locale.getDefault();
					String field = EN;
					if(locale.equals(Locale.TAIWAN))
					{
						field = TW;
					}
					else if(locale.equals(Locale.CHINA))
					{
						field = CN;
					}
					String guaContent = iChingSQLiteDBHelper.selectOne(TABLE_GUA, field, id);
					String guaIcon = iChingSQLiteDBHelper.selectOne(TABLE_GUA, field, id);
					intent.putExtra("guaContent", guaContent);
					intent.putExtra("icon", guaIcon);
					startActivity(intent);
				}
			}
		);

	}

}
