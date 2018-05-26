package com.example.rajusingh.bisena;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rajusingh on 2017-12-03.
 */

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "accountdetails.db";
    public static final String TABLE_NAME = "account_table";
    public static final String TABLE_DATES = "dates_table";
    public static final String TABLE_Expense = "expense_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "CIBC";
    public static final String COL_3 = "TD";
    public static final String COL_4 = "SCOTIA";
    public static final String COL_5 = "OTHER";
    public static final String COL_6 = "TOTAL";
    public static final String COL_7 = "DATE";
    public static final String COL_8 = "expenseAmount";
    public static final String COL_9 = "expenseTotal";
    public static final String COL_10 = "expenseItem";
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,CIBC INTEGER,TD INTEGER,SCOTIA INTEGER,OTHER INTEGER,TOTAL INTEGER,DATE Integer)");
        db.execSQL("create table " + TABLE_DATES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE Integer)");
        db.execSQL("create table " + TABLE_Expense + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE Integer,expenseAmount INTEGER, expenseTotal INTEGER, expenseItem TEXT )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_DATES);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_Expense);
     onCreate(db);
    }

    public boolean insertAccountData(int cibcamts, int tdamts, int scotiaamts, int otheramts, int totalamts, long dates){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,cibcamts);
        contentValues.put(COL_3,tdamts);
        contentValues.put(COL_4,scotiaamts);
        contentValues.put(COL_5,otheramts);
        contentValues.put(COL_6,totalamts);
        contentValues.put(COL_7,dates);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result== -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean insertExpenseData(int expenseAmounts,int expenseTotals, String expenseItems, long dates){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_8,expenseAmounts);
        contentValues.put(COL_9,expenseTotals);
        contentValues.put(COL_7,dates);
        contentValues.put(COL_10,expenseItems);

        long result = db.insert(TABLE_Expense,null,contentValues);
        if(result== -1){
            return false;
        }
        else {
            return true;
        }
    }


    public boolean insertDateData(long dates){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7,dates);
        long result = db.insert(TABLE_DATES,null,contentValues);
        if(result== -1){
            return false;
        }
        else {
            return true;
        }
    }


    public Cursor getAllAccountData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME,null);
        return res;
    }
    public Cursor getAllExpenseData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_Expense,null);
        return res;
    }
    public Cursor getAllDatedata(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_DATES,null);
        return res;
    }

    public void deleteRow(int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COL_1+"='"+value+"'");
        db.close();
    }

}
