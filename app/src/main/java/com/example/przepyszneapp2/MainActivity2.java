package com.example.przepyszneapp2;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class MainActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listview;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        dbHelper = new DatabaseHelper(MainActivity2.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursorprodukty = db.rawQuery("SELECT * FROM Produkty", null);

        ArrayList<String> itemsprodukty = new ArrayList<>();
        while (cursorprodukty.moveToNext()) {
            String nazwa = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("nazwa"));
            String waga = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("waga"));
            String item = nazwa + ", " + waga;
            itemsprodukty.add(item);
        }

        cursorprodukty.close();


        ListView listView = findViewById(R.id.listview);
        ArrayList<String> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Przepisy", null);
       while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
            String item =": "+ id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
            items.add(item);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        db.close();

    }


    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
        } else {
            isBackPressed = true;
            new AlertDialog.Builder(this)
                    .setMessage("Czy na pewno chcesz zamknąć aplikację?")
                    .setCancelable(false)
                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Zakończ wszystkie aktywności w aplikacji
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isBackPressed = false;
                        }
                    })
                    .show();
        }
    }
}