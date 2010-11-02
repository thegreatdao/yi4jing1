package iching.android.viewadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import static iching.android.R.drawable.*;

public class IChingGridViewAdapter extends BaseAdapter
{
	private Integer[] guaImages = {qian3, kun, tun, meng, xu, song, shi, bi,
									xiaochu, fu, tai, fou, tongren, dayou, qian, yu,
									sui, zhong, lin, guan, shike, bi2, bo, fu,
									wuwang, daxu, yi, daguo, kan, li, xian, heng,
									dun, dazhuang, jin, mingyi, jiaren, kui, qian2, jie,
									sun, yi2, jue, gou, cui, sheng, kun2, jing,
									ge, ding, zhen, gen, jian ,guimei, feng, lv,
									xun, dui, huan, jie2, zhongfu, xiaoguo, jiji, weiji};
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
