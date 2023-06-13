package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DodajProdukt extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaDodajProdukt;

    private EditText etwagaProdukt;

    private EditText etkcalDodajProdukt;
    private Button buttonZapiszDodajProdukt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_produkt);

        dbHelper = new DatabaseHelper(this);
        etNazwaDodajProdukt = findViewById(R.id.etNazwaDodajProdukt);
        etwagaProdukt = findViewById(R.id.etwagaProdukt);
        etkcalDodajProdukt = findViewById(R.id.etkcalDodajProdukt);
        buttonZapiszDodajProdukt = findViewById(R.id.buttonZapiszDodajProdukt);


        buttonZapiszDodajProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwaDodaj = etNazwaDodajProdukt.getText().toString().trim();
                String wagaDodaj = etwagaProdukt.getText().toString().trim();
                String kcalDodaj = etkcalDodajProdukt.getText().toString().trim();

                if (nazwaDodaj.isEmpty() || wagaDodaj.isEmpty() || kcalDodaj.isEmpty()) {
                    Toast.makeText(DodajProdukt.this, "Wprowadź wszystkie dane.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper = new DatabaseHelper(DodajProdukt.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.execSQL("INSERT INTO Produkty (nazwa, waga, kcal) VALUES (?, ?, ?)", new String[]{nazwaDodaj, wagaDodaj, kcalDodaj});
                    Toast.makeText(DodajProdukt.this, "Produkt został dodany.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DodajProdukt.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
        });
    }
}