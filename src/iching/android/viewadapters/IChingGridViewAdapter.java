package iching.android.viewadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IChingGridViewAdapter extends BaseAdapter
{
	private Integer[] hexagramIcons = AdapterCommons.HEXAGRAM_ICONS;
	private Context context;

	public IChingGridViewAdapter(Context context)
	{
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return hexagramIcons.length;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(hexagramIcons[position]);
		return imageView;
	}

}
