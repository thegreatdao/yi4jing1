package iching.android.activities;

import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import static iching.android.persistence.IChingSQLiteDBHelper.*;

public class Divination extends ListActivity
{
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.TRUE);
		setListView();
	}
	
	private void setListView()
	{
		List<iching.android.bean.Divination> divinations = iChingSQLiteDBHelper.selectAllDivinations();
		setListAdapter(new DivinationAdapter(this, R.layout.divination_list_item, divinations));
	}
	
	private class DivinationAdapter extends ArrayAdapter<iching.android.bean.Divination>
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
	                Map<String, String> gua = iChingSQLiteDBHelper.selectOneGuaByField(GUA_CODE, divination.getOriginalLines(), locale);
	                ImageView originalIcon = (ImageView) findViewById(R.id.original_lines);
					String id = gua.get(ID);
					Integer index = Integer.parseInt(id) + 1;
	                originalIcon.setImageResource(IChingView.HEXAGRAM_ICONS[index]);
	        }
	        return view;
		}

	}
}
