package com.obd.infrared.sample;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements PopupDialog.PopupDialogListener
{
    private static final String database_table = "Plany";
    private static final String database_table2 = "Pracownicy";
    private static final int requestcode = 1;
    private static final int requestcode2 = 2;

    ActionBar actionBar;
    Button zaimportuj, login;
    Spinner spinner;
    Switch przelacznik;
    TextView sciezka, sciezka2, saleTV;
    Vibrator vibrator;


//
    DBController controller = new DBController(this);
    final Context context = this;
    ListAdapter adapter;


//

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //strzałka wstecz na appbar
        setNaviBarColor();

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#101010")));

        vibrator = (Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);

        zaimportuj = (Button) findViewById(R.id.importBtn);
        login = (Button) findViewById(R.id.loginBtn);
        sciezka = (TextView) findViewById(R.id.sciezkaTV);
        sciezka2 = (TextView) findViewById(R.id.sciezka2TV);
        spinner = (Spinner) findViewById(R.id.saleSpn);
        przelacznik = (Switch) findViewById(R.id.przelacznikSW);


        Database db = new Database(this);
        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, db.pobierzSale());
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(spinnerAdapter);


        DBController dba = new DBController(this);

        if (dba.sprawdzWBazie("Spinner"))
        {
            System.out.println("SPINNER Sala: " + dba.pobierzZBazy("Spinner"));

            int spinnerPosition = spinnerAdapter.getPosition(dba.pobierzZBazy("Spinner"));
            spinner.setSelection(spinnerPosition);

        }

        if (!(dba.sprawdzWBazie("Przelacznik")))
        {
            przelacznik.setActivated(true);
            przelacznik.setChecked(true);
            System.out.println("Przelacznik w bazie");

        }

        if (dba.sprawdzWBazie("Login"))
            login.setText(dba.pobierzZBazy("Login"));

        if (dba.sprawdzWBazie("Sciezka"))
            sciezka.setText(String.format("Zaimportowano: %s", dba.pobierzZBazy("Sciezka")));


        if (dba.sprawdzWBazie("Sciezka2"))
            sciezka2.setText(String.format("Zaimportowano: %s", dba.pobierzZBazy("Sciezka2")));



        if(dba.pobierzZBazy("Przelacznik").equals("true"))
        {
            System.out.println("UWAGA true: " + dba.pobierzZBazy("Przelacznik"));
            przelacznik.setActivated(true);
            przelacznik.setChecked(true);
            spinner.setVisibility(View.VISIBLE);
            dba.zapiszDoBazy("Spinner", spinner.getSelectedItem().toString());

        }
        else
        {
            System.out.println("UWAGA false: " + dba.pobierzZBazy("Przelacznik"));
            przelacznik.setActivated(false);
            przelacznik.setChecked(false);
            spinner.setVisibility(View.INVISIBLE);
        }

        dba.close();
        controller.wypiszBaze();

/*
        zaimportuj.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                //fileintent.setType("gagt/sdf");
                fileintent.setType("text/csv");
                fileintent.addCategory(Intent.CATEGORY_OPENABLE);
                try
                {
                    //funkcja();
                    startActivityForResult(fileintent, requestcode);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(SettingsActivity.this, "Nie można załadować pliku", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                ((TextView)arg0.getChildAt(0)).setTextColor(Color.rgb(40, 90, 255));
                String sala = spinner.getSelectedItem().toString();
                System.out.println("SPINNER: " + sala);
                controller.zapiszDoBazy("Spinner", sala);
                controller.wypiszBaze();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }


    public void setNaviBarColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary)); //status bar or the time bar at the top
        }

    }


    public void PopupClick(View view)
    {
        PopupDialog popupDialog = new PopupDialog();
        popupDialog.show(getSupportFragmentManager(), "popup dialog");
    }


    @Override
    public void applyTexts(String username)
    {
        if(controller.znajdzWBazie(username) && !(username.isEmpty())) {
            login.setText(username);
            controller.zapiszDoBazy("Login", username);
        }
        else if(controller.sprawdzWBazie("Sciezka2"))
            Toast.makeText(SettingsActivity.this, "Nie ma takiego loginu w bazie", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(SettingsActivity.this, "Zaimportuj plik z danymi pracowników", Toast.LENGTH_SHORT).show();

    }


    public void ZaimportujClick(View view)
    {

        Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
        fileintent.setType("text/csv");
        fileintent.addCategory(Intent.CATEGORY_OPENABLE);

        vibrator.vibrate(80);

        DBController dba = new DBController(this);
        //dba.pobierzcsv2();
        //dba.pobierzcsv();


        try
        {
            switch(view.getId()) {
                case R.id.importBtn:
                    System.out.println("importBtn");
                    startActivityForResult(fileintent, 1);
                    break;
                case R.id.import2Btn:
                    System.out.println("import2Btn");
                    startActivityForResult(fileintent, 2);
                    break;

            }
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(SettingsActivity.this, "Nie można załadować pliku", Toast.LENGTH_SHORT).show();
        }

        //dba.pobierzcsv();
        //dba.pobierzcsv2();
        dba.close();

    }


    public void swClick(View view)
    {

        if (przelacznik.isChecked()) {

            //vibrator.vibrate(80);
            spinner.setVisibility(View.VISIBLE);
            controller.zapiszDoBazy("Przelacznik", "true");
            controller.zapiszDoBazy("Spinner", spinner.getSelectedItem().toString());
        }
        else
        {
            if(!(controller.sprawdzWBazie("Login") && controller.sprawdzWBazie("Sciezka") && controller.sprawdzWBazie("Sciezka2")))
            {
                vibrator.vibrate(80);
                przelacznik.setChecked(true);
                spinner.setVisibility(View.VISIBLE);
                controller.zapiszDoBazy("Przelacznik", "true");
                controller.zapiszDoBazy("Spinner", spinner.getSelectedItem().toString());
                Toast.makeText(SettingsActivity.this, "Podaj login oraz zaimportuj pliki", Toast.LENGTH_SHORT).show();

            }
            else {

                //vibrator.vibrate(80);
                spinner.setVisibility(View.INVISIBLE);
                controller.zapiszDoBazy("Przelacznik", "false");
            }
        }


    }





//
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        String filepath = data.getData().getPath();

        controller = new DBController(getApplicationContext());
        SQLiteDatabase db = controller.getWritableDatabase();


        switch (requestCode)
        {
            case requestcode:

                System.out.println("requestcode: " + requestcode);

                db.execSQL("delete from " + database_table);

                try {
                    System.out.println("1 ");
                    if (resultCode == RESULT_OK) {
                        System.out.println("2 ");
                        try {
                            System.out.println("3.0 " + filepath);
                            FileReader file = new FileReader(filepath);
                            System.out.println("3.1 ");
                            BufferedReader buffer = new BufferedReader(file);
                            System.out.println("3.2 ");
                            ContentValues contentValues = new ContentValues();
                            System.out.println("3.3 ");
                            String line = "";
                            System.out.println("3.4 ");
                            db.beginTransaction();
                            System.out.println("3.5 ");

                            while ((line = buffer.readLine()) != null) {

                                System.out.println("#####################################################");
                                String[] str = line.split(";", 24);  // defining 3 columns with null or blank field //values acceptance

                                String Przedmiot = str[0];
                                String KodPracownika = str[1]; //int
                                String Sala = str[2];
                                String Dzien = str[3]; //int
                                String Godz = str[4]; //int
                                String Typ = str[5];
                                String CoDrugi = str[8]; //int
                                String IdZajec = str[13]; //int

                                System.out.println("IdZajec: " + IdZajec);
                                System.out.println("Przedmiot: " + Przedmiot);
                                System.out.println("KodPracownika: " + KodPracownika);
                                System.out.println("Sala: " + Sala);
                                System.out.println("Dzien: " + Dzien);
                                System.out.println("Godz: " + Godz);
                                System.out.println("Typ: " + Typ);
                                System.out.println("CoDrugi: " + CoDrugi);

                                contentValues.put("IdZajec", IdZajec);
                                contentValues.put("Przedmiot", Przedmiot);
                                contentValues.put("KodPracownika", KodPracownika);
                                contentValues.put("Sala", Sala);
                                contentValues.put("Dzien", Dzien);
                                contentValues.put("Godz", Godz);
                                contentValues.put("Typ", Typ);
                                contentValues.put("CoDrugi", CoDrugi);

                                db.insert(database_table, null, contentValues);

                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            Toast.makeText(this, "Pomyślnie zaimportowano " + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();


                        }
                        catch (FileNotFoundException e)
                        {
                            if (db.inTransaction())
                                db.endTransaction();

                            Toast.makeText(this, "Nie udało się zaimportować " + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();
                            System.out.println("Nie udało się zaimportować");
                            Dialog d = new Dialog(this);

                            d.setTitle(e.getMessage().toString() + "first");
                            d.show();
                            db.endTransaction();
                            System.out.println("5 ");
                        }
                    } else {
                        if (db.inTransaction())
                            db.endTransaction();

                        Dialog d = new Dialog(this);
                        d.setTitle("Only CSV files allowed");
                        d.show();
                        System.out.println("6 ");
                    }

                } catch (Exception ex) {
                    if (db.inTransaction())
                        db.endTransaction();

                    Toast.makeText(this, "Nie udało się zaimportować1" + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();
                    System.out.println("Nie udało się zaimportować1");

                    Dialog d = new Dialog(this);
                    d.setTitle(ex.getMessage().toString() + "second");
                    d.show();
                    db.endTransaction();
                    System.out.println("7 ");
                }

                sciezka.setText(String.format("Zaimportowano: %s", filepath));
                controller.zapiszDoBazy("Sciezka", filepath);
                break;



            case requestcode2:

                System.out.println("requestcode2: " + requestcode2);


                //filepath = data.getData().getPath();
                //tableName = "Pracownicy";
                db.execSQL("delete from " + database_table2);

                //Toast.makeText(this, "OnActivityResult", Toast.LENGTH_SHORT).show();
                try
                {
                    System.out.println("1 ");
                    if (resultCode == RESULT_OK)
                    {
                        System.out.println("2 ");
                        try
                        {
                            System.out.println("3.0 " + filepath);
                            FileReader file = new FileReader(filepath);
                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            String line = "";
                            db.beginTransaction();

                            while ((line = buffer.readLine()) != null)
                            {

                                System.out.println("#####################################################");
                                String[] str = line.split(";", 4);  // defining 3 columns with null or blank field //values acceptance

                                String KodPracownika = str[0]; //int
                                String Email = str[1];
                                String Nazwisko = str[2];
                                String Imie = str[3];


                                System.out.println("KodPracownika: " + KodPracownika);
                                System.out.println("Email: " + Email);
                                System.out.println("Nazwisko: " + Nazwisko);
                                System.out.println("Imie: " + Imie);


                                contentValues.put("KodPracownika", KodPracownika);
                                contentValues.put("Email", Email);
                                contentValues.put("Nazwisko", Nazwisko);
                                contentValues.put("Imie", Imie);

                                db.insert(database_table2, null, contentValues);

                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            Toast.makeText(this, "Pomyślnie zaimportowano " + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();


                        }
                        catch (IOException e)
                        {
                            if (db.inTransaction())
                                db.endTransaction();

                            Toast.makeText(this, "Nie udało się zaimportować2" + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();
                            System.out.println("Nie udało się zaimportować2");

                            Dialog d = new Dialog(this);

                            d.setTitle(e.getMessage().toString() + "first");
                            d.show();
                            db.endTransaction();
                            System.out.println("5 ");
                        }
                    }
                    else
                    {
                        if (db.inTransaction())
                            db.endTransaction();

                        Dialog d = new Dialog(this);
                        d.setTitle("Only CSV files allowed");
                        d.show();
                        System.out.println("6 ");
                    }

                }
                catch (Exception ex)
                {
                    if (db.inTransaction())
                        db.endTransaction();

                    Toast.makeText(this, "Nie udało się zaimportować3" + data.getData().getLastPathSegment(), Toast.LENGTH_SHORT).show();
                    System.out.println("Nie udało się zaimportować3");

                    Dialog d = new Dialog(this);
                    d.setTitle(ex.getMessage().toString() + "second");
                    d.show();
                    db.endTransaction();
                    System.out.println("7 ");
                }
                sciezka2.setText(String.format("Zaimportowano: %s", filepath));
                controller.zapiszDoBazy("Sciezka2", filepath);
                break;
        }
        db.close();
        controller.close();
    }


}
