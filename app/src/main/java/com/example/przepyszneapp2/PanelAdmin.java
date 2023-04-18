package com.example.przepyszneapp2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class PanelAdmin extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listview;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paneladmin);
        Button button = findViewById(R.id.buttonwyloguj);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PanelAdmin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(PanelAdmin.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Dialog myDialog;
        Dialog myDialog2;
        Dialog myDialog3;



// Inicjalizacja dialogu
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialog_layout);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.setCancelable(true);

        ListView listView = myDialog.findViewById(R.id.listview_przepisy);
        Cursor cursor = db.rawQuery("SELECT * FROM przepisy", null);
        ArrayList<String> items = new ArrayList<>();

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



// ustawienie OnItemClickListenera dla listy
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String przepisId = String.valueOf(id+1);
                Cursor cursordoprzepisow = db.rawQuery("SELECT * FROM Przepisy WHERE id=?", new String[]{przepisId});

                if (cursordoprzepisow.moveToFirst()) {
                    String wierszid = cursordoprzepisow.getString(0);
                    String wiersznazwa = cursordoprzepisow.getString(1);
                    String wierszprodukt1 = cursordoprzepisow.getString(2);
                    String wierszprodukt2 = cursordoprzepisow.getString(3);
                    String wierszprodukt3 = cursordoprzepisow.getString(4);


                    Intent intent = new Intent(PanelAdmin.this, EdycjaPrzepisu.class);
                    intent.putExtra("id", wierszid);
                    intent.putExtra("nazwa", wiersznazwa);
                    intent.putExtra("produkt1", wierszprodukt1);
                    intent.putExtra("produkt2", wierszprodukt2);
                    intent.putExtra("produkt3", wierszprodukt3);
                    startActivity(intent);

                }
            }
        });


        myDialog2 = new Dialog(this);
        myDialog2.setContentView(R.layout.dialog_layoutprodukty);
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog2.setCancelable(true);

        myDialog3 = new Dialog(this);
        myDialog3.setContentView(R.layout.dialog_layoutusers);
        myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog3.setCancelable(true);

// Ustawienie zaokrąglonych rogów
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedrogidialog));
        }

// Wywołanie dialogu po kliknięciu przycisku
        Button myButton = findViewById(R.id.buttonlistaprzepisow);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show();



            }
        });
        Button myButton2 = findViewById(R.id.buttonlistaproduktow);
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.show();
            }
        });
        Button myButton3 = findViewById(R.id.buttonlistauser);
        myButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog3.show();
            }
        });
    }
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

