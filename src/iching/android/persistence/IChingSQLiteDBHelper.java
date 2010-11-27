package iching.android.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IChingSQLiteDBHelper extends SQLiteOpenHelper
{
	private static final String DB_PATH = "/data/data/iching.android/databases/";
	private static final String DB_NAME = "iching.db";
	public static final String EN = "en";
	public static final String CN = "cn";
	public static final String TW = "tw";
	public static final String ICON = "icon";
	public static final String TABLE_GUA = "gua";
	public static final String TABLE_GONG = "gong";
	public static final String TITLE_SUFFIX = "_title";
	public static final String TITLE_EN = EN + TITLE_SUFFIX;
	public static final String TITLE_CN = CN + TITLE_SUFFIX;
	public static final String TITLE_TW = TW + TITLE_SUFFIX;
	public static final String GUA_BODY = "guaBody";
	public static final String GUA_TITLE = "guaTitle";
	public static final String GUA_ICON = "guaIcon";
	
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	
	public IChingSQLiteDBHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
		sqLiteDatabase = getReadableDatabase();
		this.context = context;
		try
		{
			createDataBase();
		} catch (IOException e)
		{
			throw new Error("Error creating database");
		}
	}

	public void createDataBase() throws IOException
	{
//			boolean dbExists = checkDataBase();
//			if(!dbExists)
		if(true)
		{
			this.getReadableDatabase();
			try
			{
				copyDataBase();
			}
			catch (IOException e)
			{
				throw new Error("Error copying database");
			}
		}
	}

	@Override
	public synchronized void close()
	{
		if(sqLiteDatabase != null)
		{
			sqLiteDatabase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

	private void copyDataBase() throws IOException
	{
		InputStream dbInput = context.getAssets().open(DB_NAME);
		String outDbFileName = DB_PATH + DB_NAME;
		OutputStream dBOutPutStream = new FileOutputStream(outDbFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = dbInput.read(buffer)) > 0)
		{
			dBOutPutStream.write(buffer, 0, length);
		}
		dBOutPutStream.flush();
		dBOutPutStream.close();
		dbInput.close();
	}
	
	public List<String> selectAllTitles(Locale locale)
	{
		String field = TITLE_EN;
		if(locale.equals(Locale.TAIWAN))
		{
			field = TITLE_TW;
		}
		else if(locale.equals(Locale.CHINA))
		{
			field = TITLE_CN;
		}
		return selectAllForOneField(TABLE_GUA, field, " _id asc");
	}
	
	private List<String> selectAllForOneField(String tableName, String field, String orderBy)
	{
		if(orderBy == null)
		{
			orderBy = " " + field + " desc";
		}
		List<String> results = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(tableName, new String[]{field}, null, null, null, null, orderBy);
		if(cursor.moveToFirst())
		{
			do
			{
				results.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		cursor.close();
		return results;
	}
	
	public String selectOne(String tableName, String field, long id)
	{
		Cursor cursor = sqLiteDatabase.query(tableName, new String[]{field}, "_id=" + id, null, null, null, null);
		String result = "";
		if(cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			result = cursor.getString(0);
		}
		cursor.close();
		return result;
	}
	
	public Map<String, String> selectOneGuaByField(String fieldName, Object value, Locale locale)
	{
		String bodyLan = EN;
		String titleLan = TITLE_EN;
		if(locale.equals(Locale.TAIWAN))
		{
			bodyLan = TW;
			titleLan = TITLE_TW;
		}
		else if (locale.equals(Locale.CHINA))
		{
			bodyLan = CN;
			titleLan = TITLE_CN;
		}
		Map<String, String> gua = new HashMap<String, String>();
		Cursor cursor = sqLiteDatabase.query(TABLE_GUA, new String[]{bodyLan, titleLan, ICON}, fieldName + "=" + value, null, null, null, null);
		if(cursor.getCount() != 0)
		{
			cursor.moveToFirst();
			gua.put(GUA_BODY, cursor.getString(0));
			gua.put(GUA_TITLE, cursor.getString(1));
			gua.put(GUA_ICON, cursor.getString(2));
		}
		cursor.close();
		return gua;
	}

}