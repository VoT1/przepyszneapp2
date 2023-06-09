package com.example.przepyszneapp2;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;
    private DatabaseHelper dbHelper;
    private ProgressBar progressBar;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text1 = findViewById(R.id.editTextTextPersonName);
        TextView text2 = findViewById(R.id.editTextTextPassword);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    MY_PERMISSIONS_REQUEST_STORAGE);
        }

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        text1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String text = text1.getText().toString();
                    String lowercaseText = text.toLowerCase();
                    text1.setText(lowercaseText);
                }
            }
        });

        Button button = findViewById(R.id.buttonlogowanie);
        Animation pulseAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pulse_animation);
        button.startAnimation(pulseAnimation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1String = text1.getText().toString().trim();
                String text2String = text2.getText().toString().trim();

                if (text1String.isEmpty()) {
                    text1.setError("Nazwa użytkownika jest wymagana");
                    return;
                }

                if (text2String.isEmpty()) {
                    text2.setError("Hasło jest wymagane");
                    return;
                }

                dbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                if (db != null) {
                    Cursor cursor = db.rawQuery("SELECT * FROM Uzytkownicy WHERE nazwa=? AND haslo=?", new String[]{text1String, hashPassword(text2String)});
                    Cursor cursor2 = db.rawQuery("SELECT * FROM Uzytkownicy WHERE nazwa=? AND haslo=? AND admin=?", new String[]{text1String, hashPassword(text2String), "true"});
                    if (cursor2.moveToFirst()) {
                        Intent intent2 = new Intent(MainActivity.this, PanelAdmin.class);
                        progressBar.setVisibility(View.VISIBLE);
                        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                        progressAnimator.setDuration(2000);
                        progressAnimator.setInterpolator(new DecelerateInterpolator());
                        progressAnimator.start();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, PanelAdmin.class);
                                startActivity(intent);
                                Toast.makeText(MainActivity.this, "Poprawnie zalogowano na konto administratora", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }, 1500);
                    } else {
                        if (cursor.moveToFirst()) {
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            progressBar.setVisibility(View.VISIBLE);
                            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                            progressAnimator.setDuration(2000);
                            progressAnimator.setInterpolator(new DecelerateInterpolator());
                            progressAnimator.start();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                                    startActivity(intent);
                                    Toast.makeText(MainActivity.this, "Poprawnie zalogowano", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }, 1000);
                        } else {
                            Toast.makeText(MainActivity.this, "Niepoprawna nazwa użytkownika lub hasło", Toast.LENGTH_SHORT).show();
                        }
                        db.close();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Nie można połączyć z bazą danych", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : hashBytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij ponownie, aby wyjść z aplikacji", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
