package com.example.furniturebuyandsell.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(Context context){
        super(context,"ADR2",null,11);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String qNguoiDung = "CREATE TABLE NGUOIDUNG(" +
                "tendangnhap TEXT PRIMARY KEY," +
                "matkhau TEXT," +
                "hoten TEXT," +
                "email TEXT)";

        String qSanPham = "CREATE TABLE SANPHAM(" +
                "masp INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tensp TEXT," +
                "giaban INTEGER," +
                "soluong INTEGER)";

        db.execSQL(qNguoiDung);
        db.execSQL(qSanPham);

        String dNguoiDung = "INSERT INTO NGUOIDUNG VALUES" +
                "('taipd','123','Phan Đình Tài','taipd@gmail.com')" ;

        db.execSQL(dNguoiDung);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS NGUOIDUNG");
            db.execSQL("DROP TABLE IF EXISTS SANPHAM");
            onCreate(db);
        }
    }
}

