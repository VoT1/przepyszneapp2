package com.example.przepyszneapp2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button buttonwyszukaj;
    private Button buttonrefresh;
    private View rootView;

    private ImageView recipeImage;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        buttonwyszukaj = findViewById(R.id.buttonwyszukaj);
        buttonrefresh = findViewById(R.id.buttonrefresh);
        ListView listViewfiltr = findViewById(R.id.listviewfiltr);
        ListView listView = findViewById(R.id.listview);
        rootView = findViewById(android.R.id.content);

        dbHelper = new DatabaseHelper(MainActivity2.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Animation pulseAnimation = AnimationUtils.loadAnimation(MainActivity2.this, R.anim.pulse_animation);
        buttonwyszukaj.startAnimation(pulseAnimation);
        BlurBuilder blurBuilder = new BlurBuilder();

        Dialog myDialogKarta = new Dialog(MainActivity2.this);
        myDialogKarta.setContentView(R.layout.dialog_karta_produkt);
        myDialogKarta.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialogKarta.setCancelable(true);
        recipeImage = myDialogKarta.findViewById(R.id.recipe_image);

        Button buttonClose = myDialogKarta.findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogKarta.dismiss(); // Zamknij dialog po kliknięciu przycisku
            }
        });

        buttonrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
                progressDialog.setMessage("Trwa odświeżanie danych...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Zakończ odświeżanie i ukryj pasek postępu
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Add this flag to close previous activities
                        startActivity(intent);
                        Toast.makeText(MainActivity2.this, "Rozpoczęto odświeżanie danych", Toast.LENGTH_SHORT).show();
                        finish(); // Finish the current activity
                        Toast.makeText(MainActivity2.this, "Dane zostały odświeżone", Toast.LENGTH_SHORT).show();

                    }
                }, 1500); // 3000 ms = 3 sekundy
            }

        });


        buttonwyszukaj.setOnClickListener(new View.OnClickListener() {
            private boolean isButtonClicked = false;

            @Override
            public void onClick(View v) {
                listView.setVisibility(View.GONE);
                if (!isButtonClicked) {
                    isButtonClicked = true;
                    buttonwyszukaj.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    buttonwyszukaj.setText("Aktywne wyszukiwanie");

                } else {
                    Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                Cursor cursorprodukty = db.rawQuery("SELECT * FROM Produkty", null);

                ArrayList<String> itemsprodukty = new ArrayList<>();
                while (cursorprodukty.moveToNext()) {
                    String nazwa = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("nazwa"));
                    String waga = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("waga"));
                    String item = nazwa;
                    itemsprodukty.add(item);
                }
                cursorprodukty.close();
                final String[] produktyArray = itemsprodukty.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this, R.style.StylDialoguProdukty);
                builder.setCancelable(false);
                builder.setTitle("Wybierz produkty:");

                boolean[] checkedItems = new boolean[produktyArray.length];

                builder.setMultiChoiceItems(produktyArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Ustawienie tła dla wybranego elementu
                            View view = ((AlertDialog) dialog).getListView().getChildAt(which);
                            if (view != null) {
                                view.setBackgroundColor(Color.parseColor("#FFFFE0"));
                            }
                        } else {
                            // Usunięcie tła dla odznaczonego elementu
                            View view = ((AlertDialog) dialog).getListView().getChildAt(which);
                            if (view != null) {
                                view.setBackgroundResource(android.R.color.transparent);
                            }
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> selectedItems = new ArrayList<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                selectedItems.add(produktyArray[i]);
                            }
                        }

                        if (!selectedItems.isEmpty()) {
                            StringBuilder selection = new StringBuilder();
                            if (selectedItems.size() > 1) {
                                // Jeśli wybrano więcej niż jedną opcję, wyświetlaj przepisy, które zawierają wszystkie wybrane opcje
                                for (int i = 0; i < selectedItems.size(); i++) {
                                    selection.append("produkt1 = '").append(selectedItems.get(i)).append("'")
                                            .append(" OR produkt2 = '").append(selectedItems.get(i)).append("'")
                                            .append(" OR produkt3 = '").append(selectedItems.get(i)).append("'");
                                    if (i < selectedItems.size() - 1) {
                                        selection.append(" AND ");
                                    }
                                }
                            } else {
                                // Jeśli wybrano tylko jedną opcję, wyświetlaj przepisy, które zawierają tę opcję
                                String selectedItem = selectedItems.get(0);
                                selection.append("produkt1 = '").append(selectedItem).append("'")
                                        .append(" OR produkt2 = '").append(selectedItem).append("'")
                                        .append(" OR produkt3 = '").append(selectedItem).append("'");
                            }

                            ArrayList<String> items = new ArrayList<>();
                            Cursor cursor = db.rawQuery("SELECT * FROM Przepisy WHERE " + selection.toString(), null);
                            while (cursor.moveToNext()) {
                                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                                String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
                                String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
                                String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
                                String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
                                String item = ": " + id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
                                items.add(item);
                            }
                            cursor.close();
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, items);
                            listViewfiltr.setAdapter(adapter);
                        } else {
                            Toast.makeText(MainActivity2.this, "Nie wybrano żadnych produktów", Toast.LENGTH_SHORT).show();
                            ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
                            progressDialog.setMessage("Trwa odświeżanie danych...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Zakończ odświeżanie i ukryj pasek postępu
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Add this flag to close previous activities
                                    startActivity(intent);
                                    Toast.makeText(MainActivity2.this, "Rozpoczęto odświeżanie danych", Toast.LENGTH_SHORT).show();
                                    finish(); // Finish the current activity
                                    Toast.makeText(MainActivity2.this, "Dane zostały odświeżone", Toast.LENGTH_SHORT).show();

                                }
                            }, 1500); // 3000 ms = 3 sekundy

                        }
                    }
                });

                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity2.this, "Wybor anulowany", Toast.LENGTH_SHORT).show();
                        ProgressDialog progressDialog = new ProgressDialog(MainActivity2.this);
                        progressDialog.setMessage("Trwa odświeżanie danych...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Zakończ odświeżanie i ukryj pasek postępu
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Add this flag to close previous activities
                                startActivity(intent);
                                Toast.makeText(MainActivity2.this, "Rozpoczęto odświeżanie danych", Toast.LENGTH_SHORT).show();
                                finish(); // Finish the current activity
                                Toast.makeText(MainActivity2.this, "Dane zostały odświeżone", Toast.LENGTH_SHORT).show();

                            }
                        }, 1500);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        listViewfiltr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split(", ");
                String productId = itemParts[0];
                Toast.makeText(MainActivity2.this, "Product ID: " + productId, Toast.LENGTH_SHORT).show();

                byte[] imageBytes = getImageFromDatabase(productId);
                if (imageBytes != null) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeImage.setImageBitmap(imageBitmap);
                }

                // Wyświetlanie okienka dialogowego
                myDialogKarta.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                String[] itemParts = selectedItem.split(", ");
                String przepisId = itemParts[0];
                Toast.makeText(MainActivity2.this, "Przepis ID: " + przepisId, Toast.LENGTH_SHORT).show();

                // Pobierz obrazek z bazy danych
                byte[] imageBytes = getImageFromDatabase(przepisId);
                if (imageBytes != null) {
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeImage.setImageBitmap(imageBitmap);
                }

                // Wyświetlanie okienka dialogowego
                myDialogKarta.show();
            }
        });

        Cursor cursorprodukty = db.rawQuery("SELECT * FROM Produkty", null);

        ArrayList<String> itemsprodukty = new ArrayList<>();
        while (cursorprodukty.moveToNext()) {
            String nazwa = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("nazwa"));
            String waga = cursorprodukty.getString(cursorprodukty.getColumnIndexOrThrow("waga"));
            String item = nazwa + "," + waga;
            itemsprodukty.add(item);
        }

        cursorprodukty.close();
        ArrayList<String> items = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Przepisy", null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
            String item = ": " + id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
            items.add(item);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

    }

    private byte[] getImageFromDatabase(String przepisId) {
        byte[] imageBytes = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT grafika FROM Przepisy WHERE id = ?", new String[]{przepisId});
        if (cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("grafika"));
        }
        cursor.close();
        return imageBytes;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity2.this);
        builder.setTitle("Potwierdzenie wyjścia");
        builder.setMessage("Czy na pewno chcesz wyjść z aplikacji?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity2.super.onBackPressed();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void applyBlurEffect() {
        BitmapDrawable drawable = (BitmapDrawable) rootView.getBackground();
        Bitmap blurredBitmap = BlurBuilder.blur(MainActivity2.this, drawable.getBitmap());
        rootView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
    }
}
