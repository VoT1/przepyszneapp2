package com.example.przepyszneapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

        String[] adminOptions = {"", "true", "false"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdminDodajUser.setAdapter(spinnerAdapter);

        etNazwaDodajUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String nazwa = etNazwaDodajUser.getText().toString();
                    String lowercaseNazwa = nazwa.toLowerCase();
                    etNazwaDodajUser.setText(lowercaseNazwa);
                }
            }
        });

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

                    Cursor cursor = db.rawQuery("SELECT * FROM Uzytkownicy WHERE nazwa=?", new String[]{nazwaDodaj});
                    if (cursor.moveToFirst()) {
                        Toast.makeText(DodajUser.this, "Użytkownik o podanej nazwie już istnieje.", Toast.LENGTH_SHORT).show();
                    } else {
                        String hashedPassword = hashPassword(hasloDodaj); // Zahasłowanie hasła

                        db.execSQL("INSERT INTO Uzytkownicy (nazwa, haslo, admin) VALUES (?, ?, ?)", new String[]{nazwaDodaj, hashedPassword, adminDodaj});
                        Toast.makeText(DodajUser.this, "Użytkownik został dodany.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DodajUser.this, PanelAdmin.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            byte[] passwordBytes = password.getBytes();

            byte[] hashedBytes = messageDigest.digest(passwordBytes);

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
