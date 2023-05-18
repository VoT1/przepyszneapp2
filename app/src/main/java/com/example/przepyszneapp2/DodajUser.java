package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DodajUser extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaDodajUser;
    private EditText etHasloDodajUser;
    private Spinner spinnerAdminDodajUser;
    private Button buttonZapiszDodajUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj_user);

        dbHelper = new DatabaseHelper(this);
        etNazwaDodajUser = findViewById(R.id.etNazwaDodajUser);
        etHasloDodajUser = findViewById(R.id.etHasloDodajUser);
        spinnerAdminDodajUser = findViewById(R.id.spinnerAdminDodajUser);
        buttonZapiszDodajUser = findViewById(R.id.buttonZapiszDodajUser);

        // Define the options programmatically
        String[] adminOptions = {"","true", "false"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdminDodajUser.setAdapter(spinnerAdapter);

        buttonZapiszDodajUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwaDodaj = etNazwaDodajUser.getText().toString().trim();
                String hasloDodaj = etHasloDodajUser.getText().toString().trim();
                String adminDodaj = spinnerAdminDodajUser.getSelectedItem().toString();

                if (nazwaDodaj.isEmpty() || hasloDodaj.isEmpty() || adminDodaj.isEmpty()) {
                    Toast.makeText(DodajUser.this, "Wprowadź wszystkie dane.", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper = new DatabaseHelper(DodajUser.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    db.execSQL("INSERT INTO Uzytkownicy (nazwa, haslo, admin) VALUES (?, ?, ?)", new String[]{nazwaDodaj, hasloDodaj, adminDodaj});
                    Toast.makeText(DodajUser.this, "Użytkownik został dodany.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DodajUser.this, PanelAdmin.class);
                    startActivity(intent);
                }
            }
        });
    }
}