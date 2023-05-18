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
    private EditText etNazwa;
    private EditText etProdukt1;
    private EditText etProdukt2;
    private EditText etProdukt3;
    private Button buttonZapisz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_przepisu);


        dbHelper = new DatabaseHelper(this);
        etNazwa = findViewById(R.id.etNazwa);
        etProdukt1 = findViewById(R.id.etProdukt1);
        etProdukt2 = findViewById(R.id.etProdukt2);
        etProdukt3 = findViewById(R.id.etProdukt3);
        buttonZapisz = findViewById(R.id.buttonZapisz);


        Intent intent = getIntent();

        String wierszid = intent.getStringExtra("id");
        String nazwa = intent.getStringExtra("nazwa");
        String produkt1 = intent.getStringExtra("produkt1");
        String produkt2 = intent.getStringExtra("produkt2");
        String produkt3 = intent.getStringExtra("produkt3");

        etNazwa.setText(nazwa);
        etProdukt1.setText(produkt1);
        etProdukt2.setText(produkt2);
        etProdukt3.setText(produkt3);


        // zapisanie zmian po kliknięciu przycisku
        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = String.valueOf(etNazwa.getText());
                String produkt1 = String.valueOf(etProdukt1.getText());
                String produkt2 = String.valueOf(etProdukt2.getText());
                String produkt3 = String.valueOf(etProdukt3.getText());

                dbHelper = new DatabaseHelper(EdycjaProdukt.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE Przepisy SET nazwa = ?, produkt1 = ?, produkt2 = ?, produkt3 = ? WHERE id = ?", new String[] {nazwa, produkt1, produkt2, produkt3, wierszid});




                if (nazwa.isEmpty() || produkt1.isEmpty() || produkt2.isEmpty() || produkt3.isEmpty()) {
                    // poinformuj użytkownika o błędzie
                    Toast.makeText(EdycjaProdukt.this, "Wszystkie pola muszą być wypełnione.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // poinformuj użytkownika o sukcesie
                    Toast.makeText(EdycjaProdukt.this, "Przepis został zaktualizowany.", Toast.LENGTH_SHORT).show();
                    // przejdź do panelu administratora
                    Intent intent = new Intent(EdycjaProdukt.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
            });
    }
}
