package com.sherlock504.projectandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mydbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "projectAndroid.db";
    public static final String TABLE_NAME = "accounts_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";
    public static final String COL_4 = "name";
    public static final String COL_5 = "name1";
    public static final String COL_6 = "birthday";
    public static final String COL_7 = "gender";
    public static final String COL_8 = "display_photo";
    public static final String COL_9 = "status";
    public static final String TABLE_CREATE = "create table "+TABLE_NAME+" (id integer primary key autoincrement not null , username text not null,password text not null, name text, name1 text,birthday text, gender text, display_photo text, status text)";
    public mydbHelper(Context context){
        super(context,DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }
    public boolean insertRow(String username, String password,String name, String name1,String birthday,String gender, String display_photo, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(COL_2,username);
        contentValues.put(COL_3,password);
        contentValues.put(COL_4,name);
        contentValues.put(COL_5,name1);
        contentValues.put(COL_6,birthday);
        contentValues.put(COL_7,gender);
        contentValues.put(COL_8,display_photo);
        contentValues.put(COL_9,status);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateRow(String id, String username, String password,String name, String name1,String birthday,String gender, String display_photo, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues() ;
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,username);
        contentValues.put(COL_3,password);
        contentValues.put(COL_4,name);
        contentValues.put(COL_5,name1);
        contentValues.put(COL_6,birthday);
        contentValues.put(COL_7,gender);
        contentValues.put(COL_8,display_photo);
        contentValues.put(COL_9,status);
        db.update(TABLE_NAME,contentValues,"ID=?",new String[] {id});
        return  true;
    }
    public Cursor login(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(TABLE_NAME,null,"username = ? and password = ?", new String[]{
                username,
                password
        }, null,null,null);
        return result;
    }
    public boolean checkIfExists(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(TABLE_NAME,null,"username = ?", new String[]{
                username
        }, null,null,null);
        if(!result.moveToFirst() || result.getCount() == 0){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor checkSession(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(TABLE_NAME,null,"status = ?", new String[]{
                "active"
        }, null,null,null);
        return result;
    }
}
