package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;

import java.lang.reflect.Field;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Gua extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gua);
		Bundle extras = getIntent().getExtras();
		String data = extras.getString(GUA_BODY);
		TextView textView = (TextView) findViewById(R.id.gua_content);
		textView.setText(data);
		setTitle(extras.getString(GUA_TITLE));
		setTitleColor(Color.rgb(255, 218, 47));
		ImageView iconImage = (ImageView) findViewById(R.id.gua_icon);
		String icon = extras.getString(GUA_ICON);
		int iconId = getIconId(icon);
		iconImage.setImageResource(iconId);
	}

	private int getIconId(String icon)
	{
		Field[] fields = R.drawable.class.getFields();
		int iconId = R.drawable.bi;
		for (Field field : fields)
		{
			String fieldName = field.getName();
			if (fieldName.endsWith(icon))
			{
				try
				{
					iconId = (Integer) field.get(null);
				} catch (Exception e)
				{
					throw new Error("no such icon");
				}
			}
		}
		return iconId;
	}

}
