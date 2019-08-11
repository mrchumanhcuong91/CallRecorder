package com.project.android.callrecorder.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME ="contacts";
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_CONTACT ="contact";

    public static final String DATABASE_NAME = "contact_store.db";
    public static final int DATA_VERSION = 1;


    private static final String CREATE_TABLE = "create table "
            + TABLE_NAME +"( " + COLUMN_ID +" integer primary key autoincrement, "+ COLUMN_CONTACT
            + " text not null);";

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME,null, DATA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
