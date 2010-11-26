package iching.android.utils;

import iching.android.R;

import java.lang.reflect.Field;

public class IChingHelper
{
	public static int getId(String name, Class<?> whichClass)
	{
		Field[] fields = whichClass.getFields();
		int iconId = R.drawable.bi;
		for (Field field : fields)
		{
			String fieldName = field.getName();
			if (fieldName.equals(name))
			{
				try
				{
					iconId = (Integer) field.get(null);
				}
				catch(Exception e)
				{
					throw new Error("no such icon");
				}
			}
		}
		return iconId;
	}
}
