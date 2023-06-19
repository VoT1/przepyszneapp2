package com.example.przepyszneapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbpyszneapp2.db";
    private static final int DATABASE_VERSION = 31;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Uzytkownicy (id INTEGER PRIMARY KEY, nazwa TEXT, haslo TEXT, admin BOOLEAN)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Przepisy (id INTEGER PRIMARY KEY, nazwa TEXT, produkt1 TEXT, produkt2 TEXT, produkt3 TEXT,opis TEXT, grafika TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Produkty (id INTEGER PRIMARY KEY, nazwa TEXT, waga TEXT,kcal TEXT )");
        db.execSQL("INSERT INTO Uzytkownicy (id, nazwa, haslo, admin) VALUES (1, 'admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'true')");
        db.execSQL("INSERT INTO Uzytkownicy (id, nazwa, haslo, admin) VALUES (2, 'user', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'false')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (1, 'pomidor', '160g', '33kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (2, 'passata pomidorowa', '300ml', '111kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (3, 'ogorek', '170g', '25kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (4, 'makaron spaghetti', '300g', '450kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (5, 'czosnek', '50g', '67kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (6, 'mielona wołowina', '300g', '750kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (7, 'jajko', '50g', '78kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (8, 'szynka', '100g', '145kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (9, 'cebula', '70g', '32kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (10, 'kurczak', '200g', '250kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (11, 'pieczarki', '150g', '22kcal')");
        db.execSQL("INSERT INTO Produkty (id, nazwa, waga, kcal) VALUES (12, 'śmietana', '50g', '120kcal')");
        db.execSQL("INSERT INTO Przepisy (id,nazwa,produkt1,produkt2,produkt3,opis,grafika)VALUES (1,'Spaghetti bolognese','passata pomidorowa','makaron spaghetti','mielona wołowina','1.Na głębokiej patelni rozgrzej około 2 łyżki oliwy z oliwek.\n" +
                "2.Na rozgrzaną patelnię wrzuć czosnek i cebulę, a po chwili dodaj mięso, rozdrabniaj je np. widelcem, tak aby nie powstały grube mięsne grudki.\n" +
                "3.Do mięsa dodaj zioła oraz koncentrat. Całość podgrzewaj przez chwilę, dodaj passatę (przecier pomidorowy), gotuj na małym ogniu około 30 minut.\n" +
                "4.Makaron ugotuj al dente, podawaj go z sosem, serem, i bazylią.','spaghetti_bolognese.jpg')");
        db.execSQL("INSERT INTO Przepisy (id, nazwa, produkt1, produkt2, produkt3, opis, grafika) VALUES (2, 'Jajecznica', 'jajko', 'szynka', 'cebula', '1. W misce roztrzep jajka za pomocą widelca.\n" +
                "2. Dodaj mleko do roztrzepanych jajek i ponownie wymieszaj, aby wszystko się połączyło. Dopraw solą i pieprzem według własnego smaku.\n" +
                "3. Rozgrzej patelnię na średnim ogniu i dodaj masło lub olej.\n" +
                "4. Jeśli chcesz dodać dodatki, takie jak cebula czy pomidor, podsmaż je na patelni przez kilka minut, aż staną się miękkie.\n" +
                "5. Gdy dodatki się zeszkliły, wlej roztrzepane jajka na patelnię.\n" +
                "6. Przy pomocy łopatki lub widelca mieszaj jajka na patelni, delikatnie podnosząc je od spodu, aby umożliwić równomierne smażenie.\n" +
                "7. Kontynuuj mieszanie jajek na patelni przez kilka minut, aż zaczną się ściągać i zetnie się do odpowiedniej konsystencji (możesz dopasować czas smażenia, aby uzyskać preferowaną konsystencję jajecznicy).\n" +
                "8. Gdy jajecznica jest gotowa, przekładaj ją na talerz i podawaj od razu. Możesz również dodać starty ser żółty na wierzch jajecznicy, aby się rozpuścił.', 'jajecznica.png')");
        db.execSQL("INSERT INTO Przepisy (id, nazwa, produkt1, produkt2, produkt3, opis, grafika) VALUES (3, 'Kurczak w sosie śmietanowo-pieczarkowym', 'kurczak', 'pieczarki', 'śmietana', '1. Pokrój kurczaka na kawałki i lekko posól oraz popieprz.\n" +
                "2. Rozgrzej patelnię i dodaj masło lub olej. Smaż kurczaka na patelni, aż będzie złoty i dobrze przyrumieniony z każdej strony.\n" +
                "3. Dodaj pokrojone pieczarki do kurczaka i smaż przez kilka minut, aż pieczarki zmiękną.\n" +
                "4. Wlej śmietanę do patelni z kurczakiem i pieczarkami. Dopraw solą, pieprzem i innymi ulubionymi przyprawami do smaku.\n" +
                "5. Gotuj na średnim ogniu przez około 10 minut, aż sos zgęstnieje i kurczak będzie dobrze ugotowany.\n" +
                "6. Podawaj kurczaka w sosie śmietanowo-pieczarkowym na talerzach, najlepiej z dodatkiem ulubionych warzyw lub ryżu.', 'kurczak_pieczarki.png')");




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Uzytkownicy");
        db.execSQL("DROP TABLE IF EXISTS Przepisy");
        db.execSQL("DROP TABLE IF EXISTS Produkty");
        onCreate(db);
    }



}