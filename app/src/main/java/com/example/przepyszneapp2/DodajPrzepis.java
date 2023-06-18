package com.example.przepyszneapp2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DodajPrzepis extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaDodaj;
    private EditText etProdukt1Dodaj;
    private EditText etProdukt2Dodaj;
    private EditText etProdukt3Dodaj;
    private Button buttonZapiszDodaj;
    private String selectedImagePath;
    private ImageView imageselect;

    int defaultImageResource = R.drawable.default_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_przepis);

        dbHelper = new DatabaseHelper(this);
        etNazwaDodaj = findViewById(R.id.etNazwaDodajPrzepis);
        etProdukt1Dodaj = findViewById(R.id.etProdukt1Dodaj);
        etProdukt2Dodaj = findViewById(R.id.etProdukt2Dodaj);
        etProdukt3Dodaj = findViewById(R.id.etProdukt3Dodaj);
        buttonZapiszDodaj = findViewById(R.id.buttonZapiszDodajPrzepis);
        imageselect = findViewById(R.id.imageViewSelectedImage);

        Spinner spinnerProdukt1 = findViewById(R.id.spinnerProdukt1);
        Spinner spinnerProdukt2 = findViewById(R.id.spinnerProdukt2);
        Spinner spinnerProdukt3 = findViewById(R.id.spinnerProdukt3);
        imageselect.setImageResource(defaultImageResource);

        List<String> listaProduktow = new ArrayList<>();
        listaProduktow.add("");
        dbHelper = new DatabaseHelper(DodajPrzepis.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT nazwa FROM produkty", null);
        while (cursor.moveToNext()) {
            String nazwaProduktu = cursor.getString(0);
            listaProduktow.add(nazwaProduktu);
        }
        cursor.close();

        ArrayAdapter<String> adapterProdukty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProduktow);
        spinnerProdukt1.setAdapter(adapterProdukty);
        spinnerProdukt2.setAdapter(adapterProdukty);
        spinnerProdukt3.setAdapter(adapterProdukty);
        spinnerProdukt1.setSelection(0);
        spinnerProdukt2.setSelection(0);
        spinnerProdukt3.setSelection(0);

        spinnerProdukt1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etProdukt1Dodaj.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerProdukt2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etProdukt2Dodaj.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerProdukt3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etProdukt3Dodaj.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        imageselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageList();
            }
        });

        buttonZapiszDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwaDodaj = etNazwaDodaj.getText().toString();
                String produkt1Dodaj = etProdukt1Dodaj.getText().toString();
                String produkt2Dodaj = etProdukt2Dodaj.getText().toString();
                String produkt3Dodaj = etProdukt3Dodaj.getText().toString();

                if (TextUtils.isEmpty(nazwaDodaj)) {
                    Toast.makeText(DodajPrzepis.this, "Wprowadź nazwę przepisu.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbHelper = new DatabaseHelper(DodajPrzepis.this);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.execSQL("INSERT INTO Przepisy (nazwa, produkt1, produkt2, produkt3, grafika) VALUES (?, ?, ?, ?, ?)",
                                new String[]{nazwaDodaj, produkt1Dodaj, produkt2Dodaj, produkt3Dodaj, selectedImagePath});
                        db.close();
                        Intent intent = new Intent(DodajPrzepis.this, PanelAdmin.class);
                        startActivity(intent);

                    }
                }).start();

                Toast.makeText(DodajPrzepis.this, "Przepis został zapisany.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openImageList() {
        try {
            String[] assetList = getAssets().list("");
            List<String> imageNames = new ArrayList<>();

            if (assetList != null) {
                for (String assetName : assetList) {
                    if (!assetName.contains(".")) {
                        continue;
                    }
                    imageNames.add(assetName);
                }
            }

            if (!imageNames.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Wybierz zdjęcie");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, imageNames);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedImageName = imageNames.get(which);
                        selectedImagePath = selectedImageName;
                        try {
                            InputStream inputStream = getAssets().open(selectedImageName);
                            Bitmap selectedImageBitmap = BitmapFactory.decodeStream(inputStream);
                            imageselect.setImageBitmap(selectedImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}