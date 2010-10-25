package iching.android.viewadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import static iching.android.activities.R.drawable.*;

public class IChingGridViewAdapter extends BaseAdapter
{
	private Integer[] guaImages = { bi, bi2, bo, cui, daguo, daxu, dayou,
			dazhuang, ding, dui, dun, feng, fou, fu, ge, gen, gou, guan,
			guimei, heng, huan, jian, jiaren, jie, jie2, jiji, jin, jing, jue,
			kan, kui, kun, kun2, li, lin, lu, lv, meng, mingyi, qian, qian2,
			qian3, sheng, shi, shike, song, sui, sun, tai, tongren, tun, weiji,
			wuwang, xian, xiaochu, xiaoguo, xu, xun, yi, yi2, yu, zhen, zhong,
			zhongfu };
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
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(2, 2, 2, 2);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(guaImages[position]);
		return imageView;
	}

}
