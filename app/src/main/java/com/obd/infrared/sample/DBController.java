package com.obd.infrared.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class DBController extends SQLiteOpenHelper
{
    private static final String LOGCAT = null;
    private static final String DATABASE_NAME = "baza2.db";
    private static final int DATABASE_VERSION = 3;
    private static final String database_table = "Plany";
    private static final String database_table2 = "Pracownicy";
    private static final String database_table3 = "Ustawienia";


    public DBController(Context applicationcontext)
    {
        super(applicationcontext, DATABASE_NAME, null, DATABASE_VERSION);  // creating DATABASE
        System.out.println("DBController Konstruktor()");

        Log.d(LOGCAT, "Utworzono bazę danych");

    }


    @Override
    public void onCreate(SQLiteDatabase database) {

        System.out.println("DBController onCreate()");

        String query;
        query = "CREATE TABLE IF NOT EXISTS " + database_table + " (IdZajec INTEGER PRIMARY KEY, Przedmiot TEXT, KodPracownika INTEGER, Sala TEXT, " +
                "Dzien INTEGER, Godz INTEGER, Typ TEXT, CoDrugi INTEGER)";
        database.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + database_table2 + " (KodPracownika INTEGER PRIMARY KEY, Email TEXT, Nazwisko TEXT, Imie TEXT, " +
                "FOREIGN KEY(KodPracownika) REFERENCES " + database_table + " (KodPracownika))";
        database.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + database_table3 + " (Login TEXT, Sciezka TEXT, Sciezka2 TEXT, Przelacznik BOOLEAN PRIMARY KEY, Spinner TEXT)";
        database.execSQL(query);

        query = "INSERT INTO " + database_table3 + " (Przelacznik, Spinner) VALUES ('true', 'L1')";
        database.execSQL(query);

    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        System.out.println("DBController onUpdate()");

        String query;
        query = "DROP TABLE IF EXISTS " + database_table;
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS " + database_table2;
        database.execSQL(query);

        query = "DROP TABLE IF EXISTS " + database_table3;
        database.execSQL(query);

        onCreate(database);

    }


    public void pobierzcsv()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT IdZajec FROM " + database_table + " WHERE IdZajec > 0", null);


        String IdZajec = "";

        if (kursor != null && kursor.getCount() > 0)
        {
            System.out.println("pobierzcsv() Kursor niepusty!");

            if (kursor.moveToFirst()) {
                do {
                    IdZajec = kursor.getString(kursor.getColumnIndex("IdZajec"));
                    System.out.println("Pobierzcsv(IdZajec)=  " + IdZajec);
                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else System.out.println("pobierzcsv() Kursor pusty!");

    }


    public void pobierzcsv2()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT KodPracownika FROM " + database_table2, null);


        String KodPracownika;

        if (kursor != null && kursor.getCount() > 0)
        {
            System.out.println("pobierzcsv2() Kursor niepusty!");

            if (kursor.moveToFirst()) {
                do {
                    KodPracownika = kursor.getString(kursor.getColumnIndex("KodPracownika"));
                    System.out.println("Pobierzcsv2(KodPracownika)=  " + KodPracownika);
                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else System.out.println("pobierzcsv2() Kursor pusty!");

    }


    public boolean znajdzWBazie(String nazwa)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        Cursor kursor = null;

        try{
            kursor = db.rawQuery("SELECT EMAIL FROM " + database_table2 + " WHERE EMAIL = ?", new String []{nazwa});
        }
        catch(Exception e)
        {
            return false;
        }

        if (kursor != null && kursor.getCount() > 0)
        {

            System.out.println("znajdzWBazie(" + nazwa + ") Kursor niepusty!");

            if (kursor.moveToFirst())
                System.out.println("znajdzWBazie(" + nazwa + ")");


            kursor.close();
            db.endTransaction();
            db.close();
            return true;
        }

        System.out.println("znajdzWBazie(" + nazwa + ") Kursor pusty!");
        db.endTransaction();
        db.close();

        return false;
    }


    public void zapiszDoBazy(String nazwa, String wartosc)
    {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(nazwa, wartosc);

        db.update(database_table3, contentValues, null, null);
        db.close();

    }


    public String pobierzDane(String nazwa, String login)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT " + nazwa + " FROM " + database_table2 + " WHERE Email = ?", new String[]{login});

        String wartosc = "";

        if (kursor != null && kursor.getCount() > 0)
        {

            System.out.println("pobierzDane(" + nazwa + ") Kursor niepusty!");

            if (kursor.moveToFirst())
            {
                do {
                    wartosc = kursor.getString(kursor.getColumnIndex(nazwa));
                    System.out.println("pobierzDane(" + nazwa + "): " + wartosc);

                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else
            System.out.println("pobierzDane(" + nazwa + ") Kursor pusty!");


        db.endTransaction();
        db.close();

        return wartosc;
    }


    public String[] pobierzPlan(String login, String dzien, String godzina)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        String[] plan = {null, null, null, null};
        Cursor kursor = null;
        kursor = db.rawQuery("SELECT Typ, Sala, CoDrugi, Przedmiot FROM " + database_table + " WHERE Dzien = ? AND Godz = ? AND KodPracownika " +
                "IN (SELECT KodPracownika FROM " + database_table2 + " WHERE Email = ?)", new String[]{dzien, godzina, login});


        if (kursor != null && kursor.getCount() > 0)
        {
            System.out.println("pobierzPlan(" + login + ", " + dzien + ", " + godzina + ") Kursor niepusty!");

            if (kursor.moveToFirst())
            {
                do {
                    plan[0] = kursor.getString(kursor.getColumnIndex("Typ"));
                    plan[1] = kursor.getString(kursor.getColumnIndex("Sala"));
                    plan[2] = kursor.getString(kursor.getColumnIndex("CoDrugi"));
                    plan[3] = kursor.getString(kursor.getColumnIndex("Przedmiot"));
                    System.out.println("pobierzPlan(Typ): " + plan[0] + " (Sala): " + plan[1] + " (CoDrugi): " + plan[2] + " (Przedmiot): " + plan[3]);

                } while (kursor.moveToNext());
            }
            kursor.close();
        }
        else
            System.out.println("pobierzPlan(" + login + ", " + dzien + ", " + godzina + ") Kursor pusty!");


        db.endTransaction();
        db.close();

        return plan;
    }


    public String pobierzZBazy(String nazwa) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT " + nazwa + " FROM " + database_table3, null);


        String wartosc = "";

        if (kursor != null && kursor.getCount() > 0)
        {

            System.out.println("pobierzZBazy(" + nazwa + ") Kursor niepusty!");

            if (kursor.moveToFirst())
            {
                do {
                    wartosc = kursor.getString(kursor.getColumnIndex(nazwa));
                    System.out.println("pobierzZBazy(" + nazwa + "): " + wartosc);

                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else
            System.out.println("pobierzZBazy(" + nazwa + ") Kursor pusty!");


        db.endTransaction();
        db.close();

        return wartosc;
    }


    public boolean sprawdzWBazie(String nazwa)
    {

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT " + nazwa + " FROM " + database_table3, null);


        if (kursor.moveToFirst())
        {
            if(kursor.getString(kursor.getColumnIndex(nazwa)) == null) {
                kursor.close();
                db.endTransaction();
                db.close();
                return false;
            }

            System.out.println("sprawdzWBazie(" + nazwa + ") Jest!");
            kursor.close();
            db.endTransaction();
            db.close();
            return true;

        }
        else
        {
            System.out.println("sprawdzWBazie(" + nazwa + ") Nie ma!");
            db.endTransaction();
            db.close();
            return false;
        }


    }


    public void wypiszBaze()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT * FROM " + database_table3, null);


        if (kursor != null && kursor.getCount() > 0)
        {
            System.out.println("wypiszBaze() Kursor niepusty!");

            if (kursor.moveToFirst()) {
                do {

                    System.out.println("wypiszBaze(Login)= " + kursor.getString(kursor.getColumnIndex("Login")));
                    System.out.println("wypiszBaze(Sciezka)= " + kursor.getString(kursor.getColumnIndex("Sciezka")));
                    System.out.println("wypiszBaze(Sciezka2)= " + kursor.getString(kursor.getColumnIndex("Sciezka2")));
                    System.out.println("wypiszBaze(Przelacznik)= " + kursor.getString(kursor.getColumnIndex("Przelacznik")));
                    System.out.println("wypiszBaze(Spinner)= " + kursor.getString(kursor.getColumnIndex("Spinner")));

                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else System.out.println("wypiszBaze() Kursor pusty!");
    }


}
