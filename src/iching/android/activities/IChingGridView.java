package iching.android.activities;

import iching.android.viewadapters.IChingGridViewAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class IChingGridView extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iching_grid_view);
		GridView gridview = (GridView)findViewById(R.id.ichingGridView);
		gridview.setAdapter(new IChingGridViewAdapter(this));

		gridview.setOnItemClickListener(
			new OnItemClickListener(){
				public void onItemClick(AdapterView<?> parent, View v, int position, long id)
				{
					Toast.makeText(IChingGridView.this, "" + position, Toast.LENGTH_SHORT).show();
				}
			}
		);

	}

}
