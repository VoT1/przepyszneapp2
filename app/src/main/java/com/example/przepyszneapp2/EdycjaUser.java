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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EdycjaUser extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNazwaUser;
    private EditText etHasloUser;

    private Spinner spinnerAdminEdytujUser;
    private Button buttonZapiszUser;

    private String wierszid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edycja_user);

        dbHelper = new DatabaseHelper(this);
        etNazwaUser = findViewById(R.id.etNazwaUser);
        etHasloUser = findViewById(R.id.etHasloUser);
        spinnerAdminEdytujUser = findViewById(R.id.spinnerAdminEdytujUser);
        buttonZapiszUser = findViewById(R.id.buttonZapiszUser);

        Intent intent = getIntent();
        wierszid = intent.getStringExtra("id");
        String wiersznazwa = intent.getStringExtra("nazwa");
        String wierszhaslo = intent.getStringExtra("haslo");
        String wierszadmin = intent.getStringExtra("admin");

        etNazwaUser.setText(wiersznazwa);
        etHasloUser.setText(wierszhaslo);

        String[] adminOptions = {"","true", "false"};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, adminOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdminEdytujUser.setAdapter(spinnerAdapter);

        buttonZapiszUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazwa = etNazwaUser.getText().toString();
                String haslo = etHasloUser.getText().toString();
                String admin = spinnerAdminEdytujUser.getSelectedItem().toString();

                if (nazwa.isEmpty() || haslo.isEmpty() || admin.isEmpty()) {
                    Toast.makeText(EdycjaUser.this, "Wszystkie pola muszą być wypełnione.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHelper = new DatabaseHelper(EdycjaUser.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String hashedPassword = hashPassword(haslo);

                db.execSQL("UPDATE Uzytkownicy SET nazwa = ?, haslo = ?, admin = ? WHERE id = ?", new String[]{nazwa, hashedPassword, admin, wierszid});

                Toast.makeText(EdycjaUser.this, "Przepis został zaktualizowany.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EdycjaUser.this, PanelAdmin.class);
                startActivity(intent);
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
