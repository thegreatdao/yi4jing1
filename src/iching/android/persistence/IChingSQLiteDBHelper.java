package iching.android.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IChingSQLiteDBHelper
{
	private static final String DB_PATH = "/data/data/iching.android.activities/databases/";
	private static final String DB_NAME = "iching.db";
	public static final String EN = "en";
	public static final String CN = "cn";
	public static final String TW = "tw";
	public static final String TABLE_GUA = "gua";
	public static final String TABLE_GONG = "gong";
	private SQLiteDatabase sqLiteDatabase;
	
	public IChingSQLiteDBHelper(Context context)
	{
		IChingSQLiteOpenHelper iChingSQLiteOpenHelper = new IChingSQLiteOpenHelper(context);
		sqLiteDatabase = iChingSQLiteOpenHelper.getWritableDatabase();
	}
	
	public List<String> selectAll(String tableName, String field)
	{
		List<String> results = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(tableName, new String[]{field}, null, null, null, null, " " + field + " desc");
		if(cursor.moveToFirst())
		{
			do
			{
				results.add(cursor.getString(0));
			}while(cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed())
		{
			cursor.close();
		}
		return results;
	}
	
	private static class IChingSQLiteOpenHelper extends SQLiteOpenHelper
	{
		private SQLiteDatabase sqLiteDatabase;
		private final Context context;
		
		public IChingSQLiteOpenHelper(Context context)
		{
			super(context, DB_NAME, null, 1);
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

		/*private boolean checkDataBase()
		{
			SQLiteDatabase sqLiteDatabase = null;
			try
			{
				String dbPath = DB_PATH + DB_NAME;
				sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
			}
			catch (SQLiteException e)
			{
				throw new Error("error occurs while accessing database");
			}
			if (sqLiteDatabase != null)
			{
				sqLiteDatabase.close();
			}
			return sqLiteDatabase != null ? true : false;
		}*/

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
	}
}