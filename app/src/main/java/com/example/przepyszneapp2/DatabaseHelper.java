package com.example.przepyszneapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbpyszneapp2.db";
    private static final int DATABASE_VERSION = 20;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Uzytkownicy (id INTEGER PRIMARY KEY, nazwa TEXT, haslo TEXT, admin BOOLEAN)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Przepisy (id INTEGER PRIMARY KEY, nazwa TEXT, produkt1 TEXT, produkt2 TEXT, produkt3 TEXT, grafika TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Produkty (id INTEGER PRIMARY KEY, nazwa TEXT, waga TEXT,kcal TEXT )");
        db.execSQL("INSERT INTO Uzytkownicy (id, nazwa, haslo, admin) VALUES (1, 'admin', '1234', 'true')");
        db.execSQL("INSERT INTO Uzytkownicy (id, nazwa, haslo, admin) VALUES (2, 'user', '1234', 'false')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (1, 'pomidor', '40g', '46kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (2, 'ogorek', '30g', '36kcal')");
        db.execSQL("INSERT INTO Przepisy (id,nazwa,produkt1,produkt2,produkt3)VALUES (1,'test1','test','test','test')");
        db.execSQL("INSERT INTO Przepisy (id,nazwa,produkt1,produkt2,produkt3)VALUES (2,'test2','test','test','test')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Uzytkownicy");
        db.execSQL("DROP TABLE IF EXISTS Przepisy");
        db.execSQL("DROP TABLE IF EXISTS Produkty");
        onCreate(db);
    }



}