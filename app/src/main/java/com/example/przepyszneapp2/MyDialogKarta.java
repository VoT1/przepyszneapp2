package com.example.przepyszneapp2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class MyDialogKarta {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private int przepisId;

    public MyDialogKarta(Context context, int przepisId) {
        this.context = context;
        this.przepisId = przepisId;
        databaseHelper = new DatabaseHelper(context);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_karta_produkt, null);

        TextView nazwaTextView = dialogView.findViewById(R.id.nazwaTextView);
        TextView produkt1TextView = dialogView.findViewById(R.id.produkt1TextView);
        TextView produkt2TextView = dialogView.findViewById(R.id.produkt2TextView);
        TextView produkt3TextView = dialogView.findViewById(R.id.produkt3TextView);
        TextView opisTextView = dialogView.findViewById(R.id.opisTextView);
        AppCompatImageView grafikaImageView = dialogView.findViewById(R.id.recipe_image);
        Button buttonClose = dialogView.findViewById(R.id.button_close);
        NestedScrollView scrollView = dialogView.findViewById(R.id.scroll_view);

        db = databaseHelper.getReadableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = {String.valueOf(przepisId)};
        String query = "SELECT * FROM Przepisy WHERE " + selection;
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
            String opis = cursor.getString(cursor.getColumnIndexOrThrow("opis"));
            String grafika = cursor.getString(cursor.getColumnIndexOrThrow("grafika"));

            nazwaTextView.setText(nazwa);
            produkt1TextView.setText(produkt1);
            produkt2TextView.setText(produkt2);
            produkt3TextView.setText(produkt3);
            opisTextView.setText(opis);

            try {
                AssetManager assetManager = context.getAssets();
                List<String> files = Arrays.asList(assetManager.list(""));
                if (files.contains(grafika)) {
                    InputStream inputStream = assetManager.open(grafika);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    grafikaImageView.setImageBitmap(bitmap);
                } else {
                    InputStream defaultInputStream = assetManager.open("brak.jpg");
                    Bitmap defaultBitmap = BitmapFactory.decodeStream(defaultInputStream);
                    grafikaImageView.setImageBitmap(defaultBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cursor.close();
        db.close();

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });
    }
}

