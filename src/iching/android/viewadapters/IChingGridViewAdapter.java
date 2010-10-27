package iching.android.viewadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import static iching.android.activities.R.drawable.*;

public class IChingGridViewAdapter extends BaseAdapter
{
	private Integer[] guaImages = {qian3, };
	private Context context;

	public IChingGridViewAdapter(Context context)
	{
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return guaImages.length;
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

		imageView.setImageResource(guaImages[position]);
		return imageView;
	}

}
