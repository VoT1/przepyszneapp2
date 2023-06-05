package com.example.przepyszneapp2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button buttonwyszukaj;

    private ImageView recipeImage;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonwyszukaj = findViewById(R.id.buttonwyszukaj);
        ListView listViewfiltr = findViewById(R.id.listviewfiltr);
        ListView listView = findViewById(R.id.listview);

        dbHelper = new DatabaseHelper(MainActivity2.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Dialog myDialogKarta = new Dialog(MainActivity2.this);
        myDialogKarta.setContentView(R.layout.dialog_karta_produkt);
        myDialogKarta.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialogKarta.setCancelable(true);
        recipeImage = myDialogKarta.findViewById(R.id.recipe_image);

        Button buttonClose = myDialogKarta.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogKarta.dismiss(); // Zamknij dialog po kliknięciu przycisku
            }
        });

        buttonwyszukaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.GONE);

                Cursor cursorprodukty = db.rawQuery("SELECT * FROM Produkty", null);

                ArrayList<String> itemsprodukty = new ArrayList<>();
                while (cursorprodukty.moveToNext()) {
                    String nazwa = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("nazwa"));
                    String waga = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("waga"));
                    String item = nazwa + ", " + waga;

                    itemsprodukty.add(item);
                }

                cursorprodukty.close();
                final String[] produktyArray = itemsprodukty.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this, R.style.StylDialoguProdukty);
                builder.setTitle("Wybierz produkty:");

                boolean[] checkedItems = new boolean[produktyArray.length];

                builder.setMultiChoiceItems(produktyArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Ustawienie tła dla wybranego elementu
                            View view = ((AlertDialog) dialog).getListView().getChildAt(which);
                            if (view != null) {
                                view.setBackgroundColor(Color.parseColor("#FFFFE0"));
                            }
                        } else {
                            // Usunięcie tła dla odznaczonego elementu
                            View view = ((AlertDialog) dialog).getListView().getChildAt(which);
                            if (view != null) {
                                view.setBackgroundResource(android.R.color.transparent);
                            }
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> items = new ArrayList<>();
                        Cursor cursor = db.rawQuery("SELECT * FROM Przepisy ", null);
                        while (cursor.moveToNext()) {
                            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
                            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
                            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
                            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
                            String item = ": " + id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
                            items.add(item);
                        }
                        cursor.close();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, items);
                        listViewfiltr.setAdapter(adapter);
                    }
                });

                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity2.this, "Wybor anulowany", Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.VISIBLE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        listViewfiltr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split(", ");
                String productId = itemParts[0];
                Toast.makeText(MainActivity2.this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();

                byte[] imageBytes = getImageFromDatabase(productId);
                if (imageBytes != null) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeImage.setImageBitmap(imageBitmap);
                }

                // Wyświetlanie okienka dialogowego
                myDialogKarta.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split(", ");
                String productId = itemParts[0];
                Toast.makeText(MainActivity2.this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();

                // Pobierz obrazek z bazy danych
                byte[] imageBytes = getImageFromDatabase(productId);
                if (imageBytes != null) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeImage.setImageBitmap(imageBitmap);
                }

                // Wyświetlanie okienka dialogowego
                myDialogKarta.show();
            }
        });

        Cursor cursorprodukty = db.rawQuery("SELECT * FROM Produkty", null);

        ArrayList<String> itemsprodukty = new ArrayList<>();
        while (cursorprodukty.moveToNext()) {
            String nazwa = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("nazwa"));
            String waga = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("waga"));
            String item = nazwa + ", " + waga;
            itemsprodukty.add(item);
        }

        cursorprodukty.close();
        ArrayList<String> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Przepisy", null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
            String item = ": " + id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
            items.add(item);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        dbHelper.close();
    }

    private byte[] getImageFromDatabase(String przepisId) {
        byte[] imageBytes = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT grafika FROM Przepisy WHERE id = ?", new String[]{przepisId});
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("grafika"));
        }
        cursor.close();
        return imageBytes;
    }

    @Override
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
        } else {
            Toast.makeText(MainActivity2.this, "Naciśnij ponownie, aby wyjść", Toast.LENGTH_SHORT).show();
            isBackPressed = true;
        }
    }
}
