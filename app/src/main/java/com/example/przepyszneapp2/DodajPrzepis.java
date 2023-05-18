package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DodajPrzepis extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaDodaj;
    private EditText etProdukt1Dodaj;
    private EditText etProdukt2Dodaj;
    private EditText etProdukt3Dodaj;
    private Button buttonZapiszDodaj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_przepis);

        dbHelper = new DatabaseHelper(this);
        etNazwaDodaj = findViewById(R.id.etNazwaDodajUser);
        etProdukt1Dodaj = findViewById(R.id.etProdukt1Dodaj);
        etProdukt2Dodaj = findViewById(R.id.etProdukt2Dodaj);
        etProdukt3Dodaj = findViewById(R.id.etProdukt3Dodaj);
        buttonZapiszDodaj = findViewById(R.id.buttonZapiszDodajUser);




        Spinner spinnerProdukt1 = findViewById(R.id.spinnerProdukt1);
        Spinner spinnerProdukt2 = findViewById(R.id.spinnerProdukt2);
        Spinner spinnerProdukt3 = findViewById(R.id.spinnerProdukt3);

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


        buttonZapiszDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwaDodaj = String.valueOf(etNazwaDodaj.getText());
                String produkt1Dodaj = String.valueOf(etProdukt1Dodaj.getText());
                String produkt2Dodaj = String.valueOf(etProdukt2Dodaj.getText());
                String produkt3Dodaj = String.valueOf(etProdukt3Dodaj.getText());

                dbHelper = new DatabaseHelper(DodajPrzepis.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();


                if (nazwaDodaj.isEmpty()) {
                    Toast.makeText(DodajPrzepis.this, "Wprowadź nazwę przepisu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinnerProdukt1.getSelectedItem().toString().isEmpty() &&
                        spinnerProdukt2.getSelectedItem().toString().isEmpty() &&
                        spinnerProdukt3.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(DodajPrzepis.this, "Wybierz przynajmniej jeden produkt.", Toast.LENGTH_SHORT).show();
                    return;


            } else {
                    db.execSQL("INSERT INTO Przepisy (nazwa, produkt1, produkt2, produkt3) VALUES (?, ?, ?, ?)", new String[] {nazwaDodaj, produkt1Dodaj, produkt2Dodaj, produkt3Dodaj});
                    Toast.makeText(DodajPrzepis.this, "Przepis został dodany.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DodajPrzepis.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
        });
    }
    }