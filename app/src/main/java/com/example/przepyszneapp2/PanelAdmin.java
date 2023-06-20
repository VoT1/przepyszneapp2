package com.example.przepyszneapp2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class PanelAdmin extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listview;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paneladmin);
        Button button = findViewById(R.id.buttonwyloguj);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PanelAdmin.this, MainActivity.class);
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(PanelAdmin.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Dialog myDialog;


        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialog_layout);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.setCancelable(true);

        Dialog myDialog2;


        myDialog2 = new Dialog(this);
        myDialog2.setContentView(R.layout.dialog_layout2);
        myDialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog2.setCancelable(true);

        Dialog myDialog3;


        myDialog3 = new Dialog(this);
        myDialog3.setContentView(R.layout.dialog_layout3);
        myDialog3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog3.setCancelable(true);


        ListView listView3 = myDialog3.findViewById(R.id.listview_produkt);
        Cursor cursor3 = db.rawQuery("SELECT * FROM Produkty", null);
        ArrayList<String> items3 = new ArrayList<>();
        while (cursor3.moveToNext()) {
            String id = cursor3.getString(cursor3.getColumnIndexOrThrow("id"));
            String nazwa = cursor3.getString(cursor3.getColumnIndexOrThrow("nazwa"));
            String waga = cursor3.getString(cursor3.getColumnIndexOrThrow("waga"));
            String kcal = cursor3.getString(cursor3.getColumnIndexOrThrow("kcal"));
            String item3 = ": " + id + ", " + nazwa + ", " + waga + ", " + kcal;
            items3.add(item3);
        }
        cursor3.close();
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items3);
        listView3.setAdapter(adapter3);




        ListView listView2 = myDialog2.findViewById(R.id.listview_user);
        Cursor cursor2 = db.rawQuery("SELECT * FROM Uzytkownicy", null);
        ArrayList<String> items2 = new ArrayList<>();
        while (cursor2.moveToNext()) {
            String id = cursor2.getString(cursor2.getColumnIndexOrThrow("id"));
            String nazwa = cursor2.getString(cursor2.getColumnIndexOrThrow("nazwa"));
            String haslo = cursor2.getString(cursor2.getColumnIndexOrThrow("haslo"));
            String admin = cursor2.getString(cursor2.getColumnIndexOrThrow("admin"));
            String item = ": " + id + ", " + nazwa + ", " + haslo + ", " + admin;
            items2.add(item);
        }
        cursor2.close();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items2);
        listView2.setAdapter(adapter2);

        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = String.valueOf(id + 1);
                Cursor cursor = db.rawQuery("SELECT * FROM Uzytkownicy WHERE id=?", new String[]{userId});

                if (cursor.moveToFirst()) {
                    String wiersziduser = cursor.getString(0);
                    String wiersznazwauser = cursor.getString(1);


                    new AlertDialog.Builder(PanelAdmin.this)
                            .setTitle("Usuwanie przepisu")
                            .setMessage("Czy na pewno chcesz usunąć uzytkownika: " + wiersznazwauser + "?")
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("Uzytkownicy", "id=?", new String[]{wiersziduser});
                                    Toast.makeText(PanelAdmin.this, "Uzytkownik został usunięty.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadPanelAdmin();
                                }
                                private void reloadPanelAdmin() {
                                    Intent intent = new Intent(PanelAdmin.this, PanelAdmin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(PanelAdmin.this, "Usunięcie przepisu zostało anulowane.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String wiersziduser = String.valueOf(id+1);
                Cursor cursordouser = db.rawQuery("SELECT * FROM Uzytkownicy WHERE id=?", new String[]{wiersziduser});

                if (cursordouser.moveToFirst()) {
                    String wierszid = cursordouser.getString(0);
                    String wiersznazwa = cursordouser.getString(1);
                    String wierszhaslo = cursordouser.getString(2);
                    String wierszadmin = cursordouser.getString(3);


                    Intent intent = new Intent(PanelAdmin.this, EdycjaUser.class);
                    intent.putExtra("id", wierszid);
                    intent.putExtra("nazwa", wiersznazwa);
                    intent.putExtra("haslo", wierszhaslo);
                    intent.putExtra("admin", wierszadmin);
                    startActivity(intent);

                }
            }
        });
        listView3.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String produktId = String.valueOf(id + 1);
                Cursor cursor = db.rawQuery("SELECT * FROM Produkty WHERE id=?", new String[]{produktId});

                if (cursor.moveToFirst()) {
                    String wierszidprodukt = cursor.getString(0);
                    String wiersznazwaprodukt = cursor.getString(1);


                    new AlertDialog.Builder(PanelAdmin.this)
                            .setTitle("Usuwanie przepisu")
                            .setMessage("Czy na pewno chcesz usunąć uzytkownika: " + wiersznazwaprodukt + "?")
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("Produkty", "id=?", new String[]{wierszidprodukt});
                                    Toast.makeText(PanelAdmin.this, "Produkt został usunięty.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadPanelAdmin();
                                }
                                private void reloadPanelAdmin() {
                                    Intent intent = new Intent(PanelAdmin.this, PanelAdmin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(PanelAdmin.this, "Usunięcie produktu zostało anulowane.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                return true;
            }
        });
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String wierszidprodukt = String.valueOf(id+1);
                Cursor cursordoprodukt = db.rawQuery("SELECT * FROM Produkty WHERE id=?", new String[]{wierszidprodukt});

                if (cursordoprodukt.moveToFirst()) {
                    String wierszid = cursordoprodukt.getString(0);
                    String wiersznazwa = cursordoprodukt.getString(1);
                    String wierszwaga = cursordoprodukt.getString(2);
                    String wierszkcal = cursordoprodukt.getString(3);


                    Intent intent3 = new Intent(PanelAdmin.this, EdycjaProdukt.class);
                    intent3.putExtra("id", wierszid);
                    intent3.putExtra("nazwa", wiersznazwa);
                    intent3.putExtra("waga", wierszwaga);
                    intent3.putExtra("kcal", wierszkcal);

                    startActivity(intent3);

                }
            }
        });

        ListView listView = myDialog.findViewById(R.id.listview_przepisy);
        Cursor cursor = db.rawQuery("SELECT * FROM przepisy", null);
        ArrayList<String> items = new ArrayList<>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String nazwa = cursor.getString(cursor.getColumnIndexOrThrow("nazwa"));
            String produkt1 = cursor.getString(cursor.getColumnIndexOrThrow("produkt1"));
            String produkt2 = cursor.getString(cursor.getColumnIndexOrThrow("produkt2"));
            String produkt3 = cursor.getString(cursor.getColumnIndexOrThrow("produkt3"));
            String item =": "+ id + ", " + nazwa + ", " + produkt1 + ", " + produkt2 + ", " + produkt3;
            items.add(item);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String przepisId = String.valueOf(id + 1);
                Cursor cursor = db.rawQuery("SELECT * FROM Przepisy WHERE id=?", new String[]{przepisId});

                if (cursor.moveToFirst()) {
                    String wierszid = cursor.getString(0);
                    String wiersznazwa = cursor.getString(1);

                    new AlertDialog.Builder(PanelAdmin.this)
                            .setTitle("Usuwanie przepisu")
                            .setMessage("Czy na pewno chcesz usunąć przepis: " + wiersznazwa + "?")
                            .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.delete("Przepisy", "id=?", new String[]{wierszid});
                                    Toast.makeText(PanelAdmin.this, "Przepis został usunięty.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadPanelAdmin();
                                }
                                private void reloadPanelAdmin() {
                                    Intent intent = new Intent(PanelAdmin.this, PanelAdmin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(PanelAdmin.this, "Usunięcie przepisu zostało anulowane.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String przepisId = String.valueOf(id+1);
                Cursor cursordoprzepisow = db.rawQuery("SELECT * FROM Przepisy WHERE id=?", new String[]{przepisId});

                if (cursordoprzepisow.moveToFirst()) {
                    String wierszid = cursordoprzepisow.getString(0);
                    String wiersznazwa = cursordoprzepisow.getString(1);
                    String wierszprodukt1 = cursordoprzepisow.getString(2);
                    String wierszprodukt2 = cursordoprzepisow.getString(3);
                    String wierszprodukt3 = cursordoprzepisow.getString(4);
                    String wierszopis = cursordoprzepisow.getString(5);


                    Intent intent = new Intent(PanelAdmin.this, EdycjaPrzepisu.class);
                    intent.putExtra("id", wierszid);
                    intent.putExtra("nazwa", wiersznazwa);
                    intent.putExtra("produkt1", wierszprodukt1);
                    intent.putExtra("produkt2", wierszprodukt2);
                    intent.putExtra("produkt3", wierszprodukt3);
                    intent.putExtra("opis", wierszopis);
                    startActivity(intent);

                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedrogidialog));
            myDialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedrogidialog));
            myDialog3.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedrogidialog));
        }


        Button myButton = findViewById(R.id.buttonlistaprzepisow);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.show();

                Button btnAdd = myDialog.findViewById(R.id.buttondodajprzepis);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PanelAdmin.this, DodajPrzepis.class);
                        startActivity(intent);
                        myDialog.dismiss();
                    }
                });
            }
        });
        Button myButton3 = findViewById(R.id.buttonlistaproduktow);
        myButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog3.show();
                Button btnAdd3 = myDialog3.findViewById(R.id.buttondodajprodukt);
                btnAdd3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PanelAdmin.this, DodajProdukt.class);
                        startActivity(intent);
                        myDialog3.dismiss();
                    }
                });
            }
        });
        Button myButton2 = findViewById(R.id.buttonlistauser);
        myButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog2.show();
                Button btnAdd2 = myDialog2.findViewById(R.id.buttondodajuser);
                btnAdd2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PanelAdmin.this, DodajUser.class);
                        startActivity(intent);
                        myDialog2.dismiss();
                    }
                });
            }
        });
    }
    public void onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed();
        } else {
            isBackPressed = true;
            new AlertDialog.Builder(this)
                    .setMessage("Czy na pewno chcesz zamknąć aplikację?")
                    .setCancelable(false)
                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isBackPressed = false;
                        }
                    })
                    .show();
        }
    }

}

