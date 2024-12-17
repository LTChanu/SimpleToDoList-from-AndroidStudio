package com.chanu.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Study.DB";
    private static final String TABLE_NAME = "TaskList_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SUB = "subject";
    private static final String COLUMN_TASK = "task";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_DUE = "dueDate";
    private static final String COLUMN_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SUB + " TEXT,"
                + COLUMN_TASK + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_DUE + " DATE,"
                + COLUMN_COMPLETED + " INTEGER DEFAULT 0"
                + ")";

        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + TABLE_NAME + "'";
        Cursor cursor = db.rawQuery(query, null);

        //CREATE_TABLE if not in DB
        if (cursor.getCount() == 0) {
            db.execSQL(CREATE_TABLE);
        }
        cursor.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public boolean addTask(String sub, String task, String desc, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SUB,sub);
        contentValues.put(COLUMN_TASK,task);
        contentValues.put(COLUMN_DESC,desc);
        contentValues.put(COLUMN_DUE,date);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAllTask(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results = db.rawQuery("select * from "+TABLE_NAME,null);
        return results;
    }

    public boolean updateTask(String id, String sub, String task, String desc, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,id);
        contentValues.put(COLUMN_SUB,sub);
        contentValues.put(COLUMN_TASK,task);
        contentValues.put(COLUMN_DESC,desc);
        contentValues.put(COLUMN_DUE,date);
        long result = db.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }

    public Integer deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"id = ?",new String[] {id});
    }

    public boolean makeAsComplete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID,1);
        contentValues.put(COLUMN_COMPLETED,1);;
        long result = db.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        if(result== -1){
            return false;
        }else {
            return true;
        }
    }

    public void dropTable(){
        //Drop Table
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


}

