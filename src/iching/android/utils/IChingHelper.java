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
	
	public static String getRelatingCode(String originalCode, String changingLines)
	{
		char[] originalCodeArray = originalCode.toCharArray();
		char[] changingLineArray = changingLines.toCharArray();
		for(char digit : changingLineArray)
		{
			int index = Integer.parseInt(digit + "") - 1;
			char lineAtIndex = originalCodeArray[index];
			if(lineAtIndex == '1')
			{
				originalCodeArray[index] = '0';
			}
			else
			{
				originalCodeArray[index] = '1';
			}
		}
		return new String(originalCodeArray);
	}
}
