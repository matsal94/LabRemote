package com.obd.infrared.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Database extends SQLiteAssetHelper
{

    private static final String DATABASE_NAME = "baza.db";
    private static final int DATABASE_VERSION = 3;
    private static final String database_table = "Sale";
    private static final String database_table2 = "Sygnały";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public int pobierzCzestotliwosc(String przycisk, String sala)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        System.out.println("pobierzCzestotliwosc()");


        //if(producent.equals("Acer") && przycisk.equals("Menu"))
            //przycisk = "Cursor Enter";

        if((sala.equals("L1") || sala.equals("L2") || sala.equals("L3") || sala.equals("L4") || sala.equals("L5") || sala.equals("L6") || sala.equals("L7") ||
                sala.equals("L9") || sala.equals("L10") || sala.equals("L15") || sala.equals("S2") || sala.equals("S3")) && przycisk.equals("Menu"))
            przycisk = "Cursor Enter";



        Cursor kursor = null;
        //kursor = db.rawQuery("SELECT CZESTOTLIWOŚĆ FROM " + database_table2 + " WHERE PRZYCISK = ? AND PRODUCENT = ?", new String[]{przycisk, producent});
        kursor = db.rawQuery("SELECT CZESTOTLIWOŚĆ FROM Sygnały WHERE PRZYCISK = ? AND PRODUCENT IN (SELECT PRODUCENT FROM Sale WHERE NAZWA = ?)", new String[]{przycisk, sala});


        System.out.println("pobierzczestotliwosc(Przycisk: " + przycisk + ")");
        System.out.println("pobierzczestotliwosc(Sala: " + sala + ")");

        int freq = 0;

        if (kursor != null && kursor.getCount() > 0) {
            System.out.println("pobierzczestotliwosc() Kursor niepusty!");

            while (kursor.moveToNext()) {
                freq = kursor.getInt(kursor.getColumnIndex("CZESTOTLIWOŚĆ"));
                System.out.println("Pobierzczestotliwosc(CZESTOTLIWOSC=  " + freq + ")");
            }

            kursor.close();
        }
        else System.out.println("pobierzczestotliwosc Kursor pusty!");

        db.endTransaction();
        db.close();
        return freq;
    }


    public int[] pobierzKody(String przycisk, String sala)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        System.out.println("pobierzKody()");


        //if(producent.equals("Acer") && przycisk.equals("Menu"))
            //przycisk = "Cursor Enter";

        //w wypadku, gdy w sali bedzie ACER - ustawione ręcznie, aby Menu i Cursor Enter były jako jeden przycisk
        if((sala.equals("L1") || sala.equals("L2") || sala.equals("L3") || sala.equals("L4") || sala.equals("L5") || sala.equals("L6") || sala.equals("L7") ||
                sala.equals("L9") || sala.equals("L10") || sala.equals("L15") || sala.equals("S2") || sala.equals("S3")) && przycisk.equals("Menu"))
            przycisk = "Cursor Enter";



        Cursor kursor = null;
        //kursor = db.rawQuery("SELECT KOD FROM " + database_table2 + " WHERE PRZYCISK = ? AND PRODUCENT = ?", new String[]{przycisk, producent});
        kursor = db.rawQuery("SELECT KOD FROM Sygnały WHERE PRZYCISK = ? AND PRODUCENT IN (SELECT PRODUCENT FROM Sale WHERE NAZWA = ?)", new String[]{przycisk, sala});


        String kod = "";

        if (kursor != null && kursor.getCount() > 0) {
            System.out.println("pobierzKody() Kursor niepusty!");

            while (kursor.moveToNext()) {
                kod = kursor.getString(kursor.getColumnIndex("KOD"));
                System.out.println("pobierzKody(Kod=  " + kod + ")");
            }
            kursor.close();
        }
        else System.out.println("pobierzKody() Kursor pusty!");

        List<String> list = new ArrayList<String>(Arrays.asList(kod.split(",")));
        int[] kody = new int[list.size()];
        System.out.println("list " + list);

        for (int i = 0; i < list.size(); i++)
        {
            kody[i] = Integer.parseInt(list.get(i));
            //System.out.println("tablica[i] " + kody[i]);
            //System.out.println("list.get(i) " + list.get(i));
        }

        db.endTransaction();
        db.close();
        return kody;
    }


    public int[] pobierzInput(int licznik)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        System.out.println("pobierzInput()");
        String przycisk = "";

        switch (licznik)
        {
            case 0:
                przycisk = "Input Computer";
                System.out.println("Case 0");
                break;
            case 1:
                przycisk = "Input Component";
                System.out.println("Case 1");
                break;
            case 2:
                przycisk = "Input Component 1";
                System.out.println("Case 2");
                break;
            case 3:
                przycisk = "Input Component 2";
                System.out.println("Case 3");
                break;
            case 4:
                przycisk = "Input HDMI 1";
                System.out.println("Case 4");
                break;
            //case 5:
                //przycisk = "Input HDMI 2";
                //System.out.println("Case 5");
                //break;
            case 5:
                przycisk = "Input Video";
                System.out.println("Case 5");
                break;
        }


        Cursor kursor = null;
        kursor = db.rawQuery("SELECT KOD FROM " + database_table2 + " WHERE PRZYCISK = ? AND PRODUCENT = ?", new String[]{przycisk, "Sanyo"});


        String kod = "";

        if (kursor != null && kursor.getCount() > 0) {
            System.out.println("pobierzInput() Kursor niepusty!");

            while (kursor.moveToNext()) {
                kod = kursor.getString(kursor.getColumnIndex("KOD"));
                System.out.println("pobierzInput(Kod=  " + kod + ")");
            }
            kursor.close();
        }
        else System.out.println("pobierzInput() Kursor pusty!");


        List<String> list = new ArrayList<String>(Arrays.asList(kod.split(",")));
        int[] kody = new int[list.size()];
        System.out.println("list " + list);

        for (int i = 0; i < list.size(); i++)
            kody[i] = Integer.parseInt(list.get(i));


        db.endTransaction();
        db.close();
        return kody;
    }


    public String pobierzProducenta(String sala)
    {
        if (sala == null)
            return "null";

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        System.out.println("pobierzProducenta(" + sala + ")");


        Cursor kursor = null;
        kursor = db.rawQuery("SELECT PRODUCENT FROM " + database_table + " WHERE NAZWA = ?", new String[]{sala});


        String producent = "";

        if (kursor != null && kursor.getCount() > 0)
        {

            System.out.println("pobierzProducenta(" + sala + ") Kursor niepusty!");

            if (kursor.moveToFirst())
            {
                do {
                    producent = kursor.getString(kursor.getColumnIndex("PRODUCENT"));
                    System.out.println("pobierzProducenta(" + sala + "): " + producent);

                } while (kursor.moveToNext());
            }

            kursor.close();
        }
        else
            System.out.println("pobierzProducenta(" + sala + ") Kursor pusty!");


        db.endTransaction();
        db.close();

        return producent;
    }

    public List<String> pobierzSale()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();

        System.out.println("pobierzsale()");

        Cursor kursor = null;
        kursor = db.rawQuery("SELECT NAZWA FROM " + database_table + " WHERE PRODUCENT <> 'brak rzutnika' ", null );


        List<String> sale = new ArrayList<String>();

        if (kursor != null && kursor.getCount() > 0) {
            int i = 0;
            System.out.println("pobierzsale() Kursor niepusty!");

            while (kursor.moveToNext()) {
                sale.add(kursor.getString(kursor.getColumnIndex("NAZWA")));
                //System.out.println("PobierzSale SALE=  " + sale.get(i));
                i++;
            }

            kursor.close();
        }
        else
            System.out.println("pobierzsale() Kursor pusty!");

        db.endTransaction();
        db.close();
        return sale;
    }


}
