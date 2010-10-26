package iching.android.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class IChingSQLiteDBHelper extends SQLiteOpenHelper
{
	private static final String DB_PATH = "/data/data/iching.android.activities/databases/";
	private static final String DB_NAME = "iching.db";
	private SQLiteDatabase sqLiteDatabase;
	private final Context context;

	public IChingSQLiteDBHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	public void createDataBase() throws IOException
	{
		boolean dbExists = checkDataBase();
		if(!dbExists)
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

	public void openDataBase() throws SQLException
	{
		String pathToDB = DB_PATH + DB_NAME;
		sqLiteDatabase = SQLiteDatabase.openDatabase(pathToDB, null, SQLiteDatabase.OPEN_READONLY);
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

	private boolean checkDataBase()
	{
		SQLiteDatabase sqLiteDatabase = null;
		try
		{
			String myPath = DB_PATH + DB_NAME;
			sqLiteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
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
}