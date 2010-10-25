package iching.android.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class IChingSQLiteDBHelper
{
	private static final String DATABASE_NAME = "iching.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_GUA = "gua";
	private static final String TABLE_GONG = "gong";

	private Context context;
	private SQLiteDatabase sqLiteDatabase;

	private SQLiteStatement insertGuaStatement;
	private SQLiteStatement insertGongStatement;

	private static final String INSERT_GUA = "inset into " + TABLE_GUA + " (id, en, cn, tw) values (?, ? ,?, ?)";
	private static final String INSERT_GONG = "inset into " + TABLE_GONG + " (id, en, cn, tw) values (?, ? ,?, ?)";
	public static final int GONG = 1;
	public static final int GUA = 2;
	
	public IChingSQLiteDBHelper(Context context)
	{
		this.context = context;
		IChingSQLiteOpenHelper iChingSQLiteOpenHelper = new IChingSQLiteOpenHelper(this.context);
		sqLiteDatabase = iChingSQLiteOpenHelper.getWritableDatabase();
		insertGongStatement = sqLiteDatabase.compileStatement(INSERT_GONG);
		insertGuaStatement = sqLiteDatabase.compileStatement(INSERT_GUA);
	}

	public long insert(int table, String en, String cn, String tw)
	{
		SQLiteStatement sqLiteStatement = insertGongStatement;
		if(table == GUA)
		{
			sqLiteStatement = insertGuaStatement;
		}
		sqLiteStatement.bindString(1, en);
		sqLiteStatement.bindString(2, cn);
		sqLiteStatement.bindString(3, tw);
		return sqLiteStatement.executeInsert();
	}
	
	public void deleteAll()
	{
		sqLiteDatabase.delete(TABLE_GONG, null, null);
		sqLiteDatabase.delete(TABLE_GUA, null, null);
	}
	
	public List<String> selectAll(int table, String field)
	{
		String tableName = TABLE_GONG;
		if(table == GUA)
		{
			tableName = TABLE_GUA;
		}
		List<String> results = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.query(tableName, new String[]{field}, null, null, null, null, field + " desc", null);
		if (cursor.moveToFirst())
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

		IChingSQLiteOpenHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.d("IChingSQLiteDBHelper", "create openhelper");
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase)
		{
			sqLiteDatabase.execSQL("create table " + TABLE_GONG + " (id integer primary key, en text, cn text, tw text)");
			sqLiteDatabase.execSQL("create table " + TABLE_GUA + " (id integer primary key, en text, cn text, tw text, gong_id integer)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
		{
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GONG);
			sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GUA);
			onCreate(sqLiteDatabase);
		}
	}

}
