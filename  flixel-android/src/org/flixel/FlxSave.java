package org.flixel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * A class to help automate and simplify save game functionality.
 */
public class FlxSave
{
	private final String DATABASE_NAME;
	private final String DATABASE_TABLE;
	private static int DATABASE_VERSION = 1;	
		
	// Variable to hold the database instance.
	private SQLiteDatabase _db;
	// Database open/upgrade helper.
	private FlxDbHelper _dbHelper;
	
	
	
	/**
	 * Constructor
	 * @param context
	 * @param fields	The names of columns.
	 */
	public FlxSave(Context context, String fields)
	{
		DATABASE_NAME = "flixel";
		DATABASE_TABLE = "flixel_table";
		constructor(context, fields);
	}
	
	/**
	 * Constructor
	 * @param context
	 * @param fields		The names of columns.
	 * @param databaseName	The name of the database, default flixel
	 * @param databaseTable	The name of the table, default flixel_table
	 */
	public FlxSave(Context context, String fields, String databaseName, String databaseTable)
	{
		DATABASE_NAME = databaseName;
		DATABASE_TABLE = databaseTable;
		constructor(context, fields);
	}
	
	private void constructor(Context context, String fields)
	{
		_dbHelper = new FlxDbHelper(context, DATABASE_NAME+".db", null, DATABASE_VERSION, fields);
	}

	
	/**
	 * Open the database.
	 * 
	 * @return FlxSave
	 * @throws SQLException
	 */
	public void open() throws SQLException
	{
		try
		{
			_db = _dbHelper.getWritableDatabase();
		}
		catch(Exception e)
		{
			_db = _dbHelper.getReadableDatabase();
		}
	}
	
	
	/**
	 * Write single value into the specific column.
	 * 
	 * @param	column		The column where the value needs to be put in.
	 * @param	fieldValue	The data you want to store.
	 * 
	 * @return	Whether or not the write were successful.
	 */
	public boolean write(String column, Object fieldValue)
	{
		ContentValues values = new ContentValues();
		parseObject(values, column, fieldValue);
		return _db.insert(DATABASE_TABLE, null, values) > -1;
	}
	
	
	/**
	 * Write values into the specific columns.
	 * @param 	columns		The columns where the values need to be put in.
	 * @param 	fieldValues	The data you want to store.
	 * @return	Whether or not the write were successful.
	 */
	public boolean write(String[] columns, Object[] fieldValues)
	{
		if(columns.length != fieldValues.length)
		{
			Log.w("FlxSave read", "Columns and FieldValues don't have the same size");
			return false;
		}
		
		ContentValues values = new ContentValues();
		int l = columns.length;
		for(int i = 0; i < l; i++)
		{					
			values = parseObject(values, columns[i], fieldValues[i]);
		}
		return _db.insert(DATABASE_TABLE, null, values) > -1;
	}
	
	/**
	 * Write values via raw SQL statement. Use this if you prefer writing SQL statements.
	 * @param 	sql		A complete SQL statement.
	 * @return	Whether or not the write were successful.
	 */
	public boolean write(String sql)
	{
		Cursor result = _db.rawQuery(sql, null);
		return result.moveToFirst();
	}
	
	
	/**
	 * Select values from specific columns via a single WHERE clause.
	 * @param columns
	 * @param where
	 * @param fieldValue
	 * @return
	 * @throws SQLException
	 */
	public Cursor read(String[] columns, String where, Object fieldValue) throws SQLException
	{		
		if(fieldValue instanceof String)
			fieldValue = "'" + fieldValue + "'";
		
		Cursor result = _db.query(	false, 
									DATABASE_TABLE, 
								  	columns,
								  	where + "=" + fieldValue,
								  	null, null, null,
								  	null, null);
		
		if((result.getCount() == 0) || !result.moveToFirst())
		{
			throw new SQLException("No items found for row: " + fieldValue);
		}		
		return result;
	}
	
	
	/**
	 * Select values from specific columns via multiple WHERE clauses.
	 * @param columns
	 * @param wheres
	 * @param whereArgs
	 * @return
	 * @throws SQLException
	 */
	public Cursor read(String[] columns, String[] wheres, Object[] whereArgs) throws SQLException
	{		
		if(wheres.length != whereArgs.length)
		{
			Log.w("FlxSave read", "Wheres and FieldValues don't have the same size");
			return null;
		}		
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		int l = columns.length;
		int i;
		for(i=0; i < l; i++)
		{			
			if(i == l-1)
				sql.append(columns[i] + " ");
			else
				sql.append(columns[i] + ", ");
		}
		sql.append("FROM " + DATABASE_TABLE + " ");
		sql.append("WHERE ");
		
		l = wheres.length;
		for(i = 0; i < l; i++)
		{				
			if(whereArgs[i] instanceof String)
				whereArgs[i] = "'" + whereArgs[i] + "'";
			
			if(i == l-1)
				sql.append(wheres[i] + "=" + whereArgs[i]);
			else
				sql.append(wheres[i] + "=" + whereArgs[i] + " AND ");
		}		
		
		Cursor result = _db.rawQuery(sql.toString(), null);		
		if((result.getCount() == 0) || !result.moveToFirst())
			throw new SQLException("No items found for row: " + whereArgs);
		return result;
	}
	
	
	/**
	 * Select values via raw SQL statement. Use this if you prefer writing SQL statements.
	 * @param sql
	 * @return
	 */
	public Cursor read(String sql)
	{
		Cursor result = _db.rawQuery(sql, null);
		if((result.getCount() == 0) || !result.moveToFirst())
			throw new SQLException("No items found with: " + sql);
		return result;
	}
	
	
	/**
	 * Update a single field via WHERE clause.
	 * @param column
	 * @param fieldValue
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public boolean update(String column, Object fieldValue, String where, String whereArg)
	{		
		ContentValues values = new ContentValues();
		if(whereArg instanceof String)
			whereArg = "'" + whereArg + "'";
		parseObject(values, column, fieldValue);
		return _db.update(DATABASE_TABLE, values, where + "=" + whereArg, null) > 0;
	}
	
	/**
	 * Update multiple fields via WHERE clause.
	 * @param columns
	 * @param fieldValues
	 * @param where
	 * @param whereArg
	 * @return
	 */
	public boolean update(String columns[], Object fieldValues[], String where, String whereArg)
	{
		ContentValues values = new ContentValues();
		int l = columns.length;
		for(int i = 0; i < l; i++)
		{					
			values = parseObject(values, columns[i], fieldValues[i]);
		}
		if(whereArg instanceof String)
			whereArg = "'" + whereArg + "'";
		return _db.update(DATABASE_TABLE, values, where + "=" + whereArg, null) > 0;
	}
	
	
	/**
	 * Update values by 
	 * @param sql
	 * @return
	 */
	public boolean update(String sql)
	{
		Cursor result = _db.rawQuery(sql, null);
		return result.moveToFirst();
	}
	
	
	
	
	/**
	 * 
	 * @param column
	 * @param fieldValue
	 * @return
	 */
	public boolean erase(String column, Object fieldValue)
	{		
		if(fieldValue instanceof String)
			fieldValue = "'" + fieldValue + "'";
		return _db.delete(DATABASE_TABLE, column + "=" + fieldValue, null) > 0;		
	}
	
	
	/**
	 * Remove data via raw SQL statement. Use this if you prefer writing SQL statements. 
	 * @param sql
	 * @return
	 */
	public boolean erase(String sql)
	{
		Cursor result = _db.rawQuery(sql, null);
		return result.moveToFirst();
	}
	
	
	/**
	 * Close the database.
	 */
	public void close()
	{
		_db.close();
	}
	
	
	/**
	 * Parse the object and put it in the ContentValues.
	 * @param values
	 * @param column
	 * @param fieldValue
	 * @return ContentValues with data that has been put in.
	 */
	private ContentValues parseObject(ContentValues values, String column, Object fieldValue)
	{
		if(fieldValue instanceof Boolean)
			values.put(column, ((Boolean) fieldValue).booleanValue());
		else if(fieldValue instanceof String)
			values.put(column, fieldValue.toString());
		else if(fieldValue instanceof Integer)
			values.put(column, ((Integer) fieldValue).intValue());
		else if(fieldValue instanceof Float)
			values.put(column, ((Float) fieldValue).floatValue());
		else if(fieldValue instanceof Long)
			values.put(column, ((Long) fieldValue).longValue());
		else if(fieldValue instanceof Double)
			values.put(column, ((Double) fieldValue).doubleValue());
		else if(fieldValue instanceof Byte)
			values.put(column, ((Byte) fieldValue).byteValue());
		else if(fieldValue == null)
			values.putNull(column);
		return values;
	}
		
	
	/**
	 * A helper class to manage database creation and version management.
	 *
	 */
	public class FlxDbHelper extends SQLiteOpenHelper
	{	
		
		// SQL Statement to create a new database.
		private final String CREATE;

		/**
		 * Constructor
		 * @param context
		 * @param databaseName
		 * @param factory
		 * @param version
		 * @param fields
		 */
		public FlxDbHelper(Context context, String databaseName, CursorFactory factory, int version, String fields)
		{
			super(context, databaseName, factory, version);
			CREATE = 	"CREATE TABLE " + DATABASE_TABLE + " " +
						"(" + fields + ");";			
		}
		

		/**
		 * Called when no database exists in disk and the helper class to needs 
		 * to create a new one.
		 * 
		 * @param db
		 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(CREATE);
			Log.w("create database", "success");
		}

		
		/**
		 * Called when there is a database version mismatch meaning that the version
		 * of the database on disk needs to be upgraded to the current version.
		 * 
		 * @param db
		 * @param oldVersion
		 * @param newVersion
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +
									oldVersion + " to " +
									newVersion + ", which will destroy all old data.");
			
			// Upgrade the existing database to conform to the new version. Multiple
			// previous versions can be handled by comparing oldVersion and newVersion 
			// values.		
			
			// The simplest case is to drop the old table and create a new one.		
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Create a new one.
			onCreate(db);
		}
	}
}
