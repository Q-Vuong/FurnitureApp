package com.example.furniturebuyandsell.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.furniturebuyandsell.database.DbHelper;

public class NguoiDungDAO {
    private DbHelper dbHelper;

    public NguoiDungDAO(Context context){
        dbHelper = new DbHelper(context);
    }

    public boolean checkLogin(String username, String password){
        SQLiteDatabase sqLiteOpenHelper = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteOpenHelper.rawQuery("SELECT * FROM NGUOIDUNG WHERE tendangnhap =? AND matkhau=?",new String[]{username,password});
        if(cursor.getCount() > 0)
            return true;
        return false;
    }

    public boolean Register(String username, String password, String hoten){
        SQLiteDatabase sqLiteOpenHelper = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tendangnhap",username);
        contentValues.put("matkhau",password);
        contentValues.put("hoten",hoten);

        long check = sqLiteOpenHelper.insert("NGUOIDUNG",null,contentValues);
        if(check != -1)
            return true;
        return false;
    }

    public String ForgotPassword(String mail){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT matkhau FROM NGUOIDUNG WHERE email =?",new String[]{mail});
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getString(0);
        }else{
            return "";
        }
    }

}

