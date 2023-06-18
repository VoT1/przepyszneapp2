package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EdycjaProdukt extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaProdukt;
    private EditText etkcalProdukt;

    private EditText etwagaProdukt;
    private Button buttonZapiszProdukt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_produkt);


        dbHelper = new DatabaseHelper(this);
        etNazwaProdukt = findViewById(R.id.etNazwaProdukt);
        etkcalProdukt = findViewById(R.id.etkcalProdukt);
        etwagaProdukt = findViewById(R.id.etwagaProdukt);
        buttonZapiszProdukt = findViewById(R.id.buttonZapiszProdukt);


        Intent intent = getIntent();

        String wierszid = intent.getStringExtra("id");
        String wiersznazwa = intent.getStringExtra("nazwa");
        String wierszwaga = intent.getStringExtra("waga");
        String wierszkcal = intent.getStringExtra("kcal");

        etNazwaProdukt.setText(wiersznazwa);
        etwagaProdukt.setText(wierszwaga);
        etkcalProdukt.setText(wierszkcal);


        buttonZapiszProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wiersznazwa = String.valueOf(etNazwaProdukt.getText());
                String wierszwaga = String.valueOf(etwagaProdukt.getText());
                String wierszkcal = String.valueOf(etkcalProdukt.getText());


                dbHelper = new DatabaseHelper(EdycjaProdukt.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE Produkty SET nazwa = ?, waga = ?, kcal = ? WHERE id = ?", new String[] {wiersznazwa, wierszwaga, wierszkcal, wierszid});




                if (wiersznazwa.isEmpty() || wierszwaga.isEmpty() || wierszkcal.isEmpty() ) {
                    Toast.makeText(EdycjaProdukt.this, "Wszystkie pola muszą być wypełnione.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(EdycjaProdukt.this, "Przepis został zaktualizowany.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EdycjaProdukt.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
            });
    }
}
