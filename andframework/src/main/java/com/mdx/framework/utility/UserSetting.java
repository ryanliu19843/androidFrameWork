package com.mdx.framework.utility;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSetting extends SQLiteOpenHelper {
	public static final String tableName = "STATISTICS_SETTING";
	public static final String ID = "ID";
	public static final String NAME = "NAME";
	public static final String VALUE = "VALUE";
	public static final String STATE = "STATE";
	public static final int version = 1;

	public UserSetting(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public UserSetting(Context context) {
		super(context, tableName, null, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (" + ID
				+ " VARCHAR," + NAME + " VARCHAR," + VALUE + " VARCHAR,"
				+ STATE + " VARCHAR)");
	}

	public void Insert(String id, String name, String value, String state) {
		SQLiteDatabase sql = this.getWritableDatabase();
		try {
			sql.execSQL("DELETE FROM " + tableName + " WHERE " + NAME + "='"
					+ name + "' and " + ID + "='" + id + "';");
			ContentValues tcv = new ContentValues();
			tcv.put(ID, id);
			tcv.put(NAME, name);
			tcv.put(VALUE, value);
			tcv.put(STATE, state);
			sql.insert(tableName, null, tcv);
		} finally {
			sql.close();
		}
	}

	public void Update(String id, String name, String value, String state) {
		SQLiteDatabase sql = this.getWritableDatabase();
		try {
			sql.execSQL("UPDATE " + tableName + " set " + NAME + "='" + name
					+ "'," + VALUE + "='" + value + "'," + STATE + "='" + state
					+ "' where " + ID + "='" + id + "';");
		} finally {
			sql.close();
		}
	}

	public List<String[]> find(String selection) {
		SQLiteDatabase sql = this.getWritableDatabase();
		List<String[]> retn = new ArrayList<String[]>();
		try {
			Cursor mCursor = sql.query(tableName, null, selection, null, null,
					null, null);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					String str[] = new String[mCursor.getColumnCount()];
					for (int i = 0; i < str.length; i++) {
						str[i] = mCursor.getString(i);
					}
					retn.add(str);
				}
				mCursor.close();
			}
		} finally {
			sql.close();
		}
		return retn;
	}

	public boolean has(String selection) {
		SQLiteDatabase sql = this.getWritableDatabase();
		Cursor mCursor = sql.query(tableName, null, selection, null, null,
				null, null);
		try {
			if (mCursor != null) {
				if (mCursor.moveToNext()) {
					return true;
				}
			}
		} finally {
			mCursor.close();
			sql.close();
		}

		return false;
	}

	public boolean hasId(String id) {
		SQLiteDatabase sql = this.getWritableDatabase();
		Cursor mCursor = sql.query(tableName, null, ID + "='" + id + "'", null,
				null, null, NAME);
		try {
			if (mCursor != null) {
				if (mCursor.moveToNext()) {
					return true;
				}
			}
		} finally {
			mCursor.close();
			sql.close();
		}
		return false;
	}

	public String findValue(String id, String name) {
		String[] clumns = new String[] { VALUE };
		String[] retn = findOne(id, name, clumns);
		if (retn != null) {
			return retn[0];
		}
		return null;
	}

	public String findState(String id, String name) {
		String[] clumns = new String[] { STATE };
		String[] retn = findOne(id, name, clumns);
		if (retn != null) {
			return retn[0];
		}
		return null;
	}

	public String[] findOne(String id, String name, String[] clumns) {
		List<String[]> list = find(clumns, NAME + "='" + name + "' and " + ID
				+ "='" + id + "'");
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<String[]> findByid(String id) {
		SQLiteDatabase sql = this.getWritableDatabase();
		List<String[]> retn = new ArrayList<String[]>();
		try {
			Cursor mCursor = sql.query(tableName, null, ID + "='" + id + "'",
					null, null, null, NAME);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					String str[] = new String[mCursor.getColumnCount()];
					for (int i = 0; i < str.length; i++) {
						str[i] = mCursor.getString(i);
					}
					retn.add(str);
				}
				mCursor.close();
			}
		} finally {
			sql.close();
		}
		return retn;
	}

	public List<String[]> find(String[] columns, String selection) {
		SQLiteDatabase sql = this.getWritableDatabase();
		List<String[]> retn = new ArrayList<String[]>();
		try {
			Cursor mCursor = sql.query(tableName, columns, selection, null,
					null, null, null);
			if (mCursor != null) {
				while (mCursor.moveToNext()) {
					String str[] = new String[mCursor.getColumnCount()];
					for (int i = 0; i < str.length; i++) {
						str[i] = mCursor.getString(i);
					}
					retn.add(str);
				}
			}
			mCursor.close();
		} finally {
			sql.close();
		}
		return retn;
	}

	public String[] findbyid(String id, String name) {
		List<String[]> list = this.find(ID + "='" + id + "' and " + NAME + "='"
				+ name + "'");
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public void Deletebyid(String id) {
		SQLiteDatabase sql = this.getWritableDatabase();
		try {
			sql.execSQL("DELETE FROM " + tableName + " WHERE " + ID + "='" + id
					+ "';");
		} finally {
			sql.close();
		}
	}

	public void recreate() {
		SQLiteDatabase sql = this.getWritableDatabase();
		try {
			onCreate(sql);
		} finally {
			sql.close();
		}
	}

	public void drop() {
		SQLiteDatabase sql = this.getWritableDatabase();
		try {
			sql.execSQL("DROP TABLE IF EXISTS " + tableName);
		} finally {
			sql.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
		onCreate(db);
	}

}
