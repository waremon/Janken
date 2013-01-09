package org.waremon.janken;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class localSQLManager extends SQLiteOpenHelper {
	private static final String DB_NAME = "janken_db";
	private static final String TABLE1 = "mydata";
	private static final String TABLE2 = "twtoken";
	private static final int DB_VERSION = 1;
	private static final String CREATE_TABLE1 =
			"create table " + TABLE1 + "("
			+ "gn integer not null," +
			"cn integer not null," +
			"pn integer not null," +
			"gw integer not null," +
			"cw integer not null," +
			"pw integer not null," +
			"tw integer not null," +
			"cnt integer not null," +
			"score integer not null);";
	private static final String CREATE_TABLE2 =
			"create table " + TABLE2 + "("
			+ "key text not null,"
			+ "secret text not null);";
	private static final String INSERT_TABLE1 =
			"insert into " + TABLE1 + " (gn, cn, pn, gw, cw, pw, tw, cnt, score)" +
					" values (0, 0, 0, 0, 0, 0, 0, 0, 0);";
	private static final String INSERT_TABLE2 =
			"insert into " + TABLE2 + " (key, secret)" +
					" values ('init', 'init');";
	public localSQLManager (Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE1);
		db.execSQL(INSERT_TABLE1);
		db.execSQL(CREATE_TABLE2);
		db.execSQL(INSERT_TABLE2);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
