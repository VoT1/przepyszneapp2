package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EdycjaPrzepisu extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwa;
    private EditText etProdukt1;
    private EditText etProdukt2;
    private EditText etProdukt3;
    private Button buttonZapisz;
    private EditText etOpis;
    private ImageView imageViewSelectedImage;

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
        etOpis = findViewById(R.id.etOpis);


        Intent intent = getIntent();

        String wierszid = intent.getStringExtra("id");
        String nazwa = intent.getStringExtra("nazwa");
        String produkt1 = intent.getStringExtra("produkt1");
        String produkt2 = intent.getStringExtra("produkt2");
        String produkt3 = intent.getStringExtra("produkt3");
        String opis = intent.getStringExtra("opis");

        etNazwa.setText(nazwa);
        etProdukt1.setText(produkt1);
        etProdukt2.setText(produkt2);
        etProdukt3.setText(produkt3);
        etOpis.setText(opis);

        buttonZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = etNazwa.getText().toString();
                String produkt1 = etProdukt1.getText().toString();
                String produkt2 = etProdukt2.getText().toString();
                String produkt3 = etProdukt3.getText().toString();
                String opis = etOpis.getText().toString();

                dbHelper = new DatabaseHelper(EdycjaPrzepisu.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE Przepisy SET nazwa = ?, produkt1 = ?, produkt2 = ?, produkt3 = ?, opis = ? WHERE id = ?",
                        new String[]{nazwa, produkt1, produkt2, produkt3, opis, wierszid});

                if (nazwa.isEmpty() || produkt1.isEmpty() || produkt2.isEmpty() || produkt3.isEmpty() || opis.isEmpty()) {
                    Toast.makeText(EdycjaPrzepisu.this, "Wszystkie pola muszą być wypełnione.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EdycjaPrzepisu.this, "Przepis został zaktualizowany.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EdycjaPrzepisu.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
        });
    }
}
