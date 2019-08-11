package com.project.android.callrecorder.Database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.project.android.callrecorder.Model.Contact_Data;

import java.util.ArrayList;
import java.util.List;

public class ContactDataSource {

    //database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_CONTACT};
    public ContactDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }
    public void open() throws SQLiteException{
        database = dbHelper.getWritableDatabase();
    }
    public void openRead() throws SQLiteException{
        database = dbHelper.getReadableDatabase();
    }
    public void close() throws SQLiteException{
        dbHelper.close();
    }

    public int saveContactDb(String phone_number){
        if(Exists(phone_number))
            return -1;
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CONTACT, phone_number);

        long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null, values);
        if(insertId != -1){
            Log.d("CuongCM" ,"save phone number "+phone_number + " success");
        }
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns,
                MySQLiteHelper.COLUMN_ID +" = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        //Contact_Data newData = cursoToContact(cursor);
        cursor.close();
        return 0;
    }
    public void deleteContact(Contact_Data data){
        long id = data.getId();

        database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID + " = " +id,null);
    }
    public void deleteAllContact(){
        database.delete(MySQLiteHelper.TABLE_NAME, null,null);
    }
    public List<String> getSelectContact(){
        List<String> listContact = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, allColumns, null, null, null,
            null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
//            Contact_Data data =  cursoToContact(cursor);
            listContact.add(cursor.getString(1));
            Log.e("CuongCM"," read from database "+cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return listContact;
    }
    public Contact_Data cursoToContact(Cursor cursor){
        Contact_Data data = new Contact_Data();
        data.setId(cursor.getLong(0));
        data.setPhone(cursor.getString(1));
        return data;
    }
    public boolean Exists(String searchItem) {

        String[] columns = { MySQLiteHelper.COLUMN_CONTACT };
        String selection = MySQLiteHelper.COLUMN_CONTACT + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

}
