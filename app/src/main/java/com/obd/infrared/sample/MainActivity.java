package com.obd.infrared.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
//import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.obd.infrared.InfraRed;
import com.obd.infrared.log.LogToTextView;
import com.obd.infrared.patterns.PatternAdapter;
import com.obd.infrared.patterns.PatternConverter;
import com.obd.infrared.patterns.PatternConverterUtils;
import com.obd.infrared.patterns.PatternType;
import com.obd.infrared.transmit.TransmitInfo;
import com.obd.infrared.transmit.TransmitterType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private LogToTextView log;
    private InfraRed infraRed;
    private TransmitInfo[] patterns;
    private boolean wejscie = true;
    private String sala, producent, imie, nazwisko, login;
    private int currentPattern = 0, godzina, minuta, sekunda, dzien, numerTygodnia;
    private int licznik = 0;

    ActionBar actionBar;
    Vibrator vibrator;
    EditText console;
    Button transmit_button, input, freeze, blank, noshow, zoom, menu, back, up, down, left, right, enter;
    RadioGroup rg;
    RadioButton rb;

    TransmitterType transmitterType;
    PatternAdapter patternAdapter;

    final Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        String TAG = "IR";

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#101010"))); //#161616


        vibrator = (Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);

        //Button transmitButton = (Button) this.findViewById(R.id.transmit_button);
        //transmitButton.setOnClickListener(this);

        // Log messages print to EditText
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeueUltraLight.ttf");
        console = (EditText) this.findViewById(R.id.console);
        console.setTypeface(type);
        log = new LogToTextView(console, TAG);


        // Log messages print with Log.d(), Log.w(), Log.e()
        // LogToConsole log = new LogToConsole(TAG);

        // Turn off log
        // LogToAir log = new LogToAir(TAG);

        infraRed = new InfraRed(this.getApplication(), log);
        // detect transmitter type
        transmitterType = infraRed.detect();
        //log.log("TransmitterType: " + transmitterType);

        // initialize transmitter by type
        infraRed.createTransmitter(transmitterType);
/*
        // initialize raw patterns
        List<PatternConverter> rawPatterns = new ArrayList<>();

        // Pentax AF
        rawPatterns.add(new PatternConverter(PatternType.Cycles, 40000, 1, 69, 340, 171, 21, 21, 21, 21, 21, 65, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 65, 21, 65, 21, 21, 21, 65, 21, 65, 21, 65, 21, 65, 21, 65, 21, 21, 21, 21, 21, 21, 21, 65, 21, 21, 21, 21, 21, 21, 21, 21, 21, 65, 21, 65, 21, 65, 21, 21, 21, 65, 21, 65, 21, 65, 21, 65, 21, 1555, 340, 86, 21, 3678));
        //38000,1,69,340,171,21,21,21,21,21,65,21,21,21,21,21,21,21,21,21,21,21,65,21,65,21,21,21,65,21,65,21,65,21,65,21,65,21,21,21,21,21,21,21,65,21,21,21,21,21,21,21,21,21,65,21,65,21,65,21,21,21,65,21,65,21,65,21,65,21,1555,340,86,21,3678
        //rawPatterns.add(new PatternConverter(PatternType.Cycles, 38000,1,69,343,172,21,22,21,22,21,65,21,22,21,22,21,22,21,22,21,22,21,65,21,65,21,22,21,65,21,65,21,65,21,65,21,65,21,22,21,22,21,65,21,22,21,22,21,22,21,65,21,65,21,65,21,65,21,22,21,65,21,65,21,65,21,22,21,22,21,1673,343,86,21,3732));
        // Canon
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 33000, 500, 7300, 500, 200));
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 33000, 555, 5250, 555, 7143, 555, 200));
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 33000, 555, 5250, 555, 7143, 555, 1000, 555, 5250, 555, 7143, 555, 1000));

//        // Nikon v.1
//        rawPatterns.add(new PatternConverter(PatternType.Cycles, 38400, 1, 105, 5, 1, 75, 1095, 20, 60, 20, 140, 15, 2500, 80, 1));
//        // Nikon v.2
//        rawPatterns.add(new PatternConverter(PatternType.Cycles, 38400, 77, 1069, 16, 61, 16, 137, 16, 2427, 77, 1069, 16, 61, 16, 137, 16));
//        // Nikon v.3
//        rawPatterns.add(new PatternConverter(PatternType.Intervals, 38000, 2000, 27800, 400, 1600, 400, 3600, 400, 200));
//        // Nikon v.3 fromString
//        rawPatterns.add(PatternConverterUtils.fromString(PatternType.Intervals, 38000, "2000, 27800, 400, 1600, 400, 3600, 400, 200"));
//        // Nikon v.3 fromHexString
//        rawPatterns.add(PatternConverterUtils.fromHexString(PatternType.Intervals, 38000, "0x7d0 0x6c98 0x190 0x640 0x190 0xe10 0x190 0xc8"));
//        // Nikon v.3 fromHexString without 0x
        //rawPatterns.add(PatternConverterUtils.fromHexString(PatternType.Intervals, 38000, "0000 006C 0022 0002 015B 00AD 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 06FB 015B 0057 0016 0E6C"));

        logDeviceInfo();

        // adapt the patterns for the device that is used to transmit the patterns
        PatternAdapter patternAdapter = new PatternAdapter(log, transmitterType);
        TransmitInfo[] transmitInfoArray = new TransmitInfo[rawPatterns.size()];

        for (int i = 0; i < transmitInfoArray.length; i++) {
            transmitInfoArray[i] = patternAdapter.createTransmitInfo(rawPatterns.get(i));
        }
        this.patterns = transmitInfoArray;

        for (TransmitInfo transmitInfo : this.patterns) {
            log.log(transmitInfo.toString());
        }*/

/*        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                onStart();
                Toast.makeText(MainActivity.this, "onStart()", Toast.LENGTH_SHORT).show();
            }
        }, 10000);*/



        menu = (Button) findViewById(R.id.menuBtn);
        back = (Button) findViewById(R.id.backBtn);
        up = (Button) findViewById(R.id.upBtn);
        down = (Button) findViewById(R.id.downBtn);
        left = (Button) findViewById(R.id.leftBtn);
        right = (Button) findViewById(R.id.rightBtn);
        enter = (Button) findViewById(R.id.enterBtn);

        transmit_button = (Button) findViewById(R.id.transmit_button);
        input = (Button) findViewById(R.id.inputBtn);
        freeze = (Button) findViewById(R.id.freezeBtn);
        blank = (Button) findViewById(R.id.blankBtn);
        noshow = (Button) findViewById(R.id.noshowBtn);
        zoom = (Button) findViewById(R.id.zoomBtn);


        type = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeue.ttf");

        menu.setTypeface(type);
        back.setTypeface(type);
        enter.setTypeface(type);

        transmit_button.setTypeface(type);
        input.setTypeface(type);
        freeze.setTypeface(type);
        blank.setTypeface(type);
        noshow.setTypeface(type);
        zoom.setTypeface(type);


        //logDeviceInfo();
        setNaviBarColor();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Brak dostępu do pamięci wewnętrznej telefonu", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void logDeviceInfo() {
        /*
        log.log("Użytkownik - " + imie + " " + nazwisko);
        log.log("Przedmiot - " + przedmiot);
        log.log("Rodzaj zajęć - " + typ);
        log.log("Sala - " + sala);
        log.log("Producent - " + producent);

        log.log("BRAND: " + Build.BRAND);
        log.log("DEVICE: " + Build.DEVICE);
        log.log("MANUFACTURER: " + Build.MANUFACTURER);
        log.log("MODEL: " + Build.MODEL);
        log.log("PRODUCT: " + Build.PRODUCT);
        log.log("CODENAME: " + Build.VERSION.CODENAME);
        log.log("RELEASE: " + Build.VERSION.RELEASE);
        log.log("SDK_INT: " + Build.VERSION.SDK_INT);*/
    }
/*
    public void rbClick(View v) {

        rb = null;
        int radiobuttonid = rg.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(radiobuttonid);
        Toast.makeText(getBaseContext(), rb.getText(), Toast.LENGTH_SHORT).show();

        if (rb.getText().equals("Dell")) {
            zoom.setVisibility(View.INVISIBLE);
            freeze.setVisibility(View.INVISIBLE);
        }
        else if (rb.getText().equals("Sanyo")) {
            freeze.setVisibility(View.VISIBLE);
            zoom.setVisibility(View.VISIBLE);
        }
        else {
            freeze.setVisibility(View.VISIBLE);
            zoom.setVisibility(View.VISIBLE);
        }

    }
*/

    public void setNaviBarColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary)); //status bar or the time bar at the top
        }

    }


    @Override
    protected void onStart()
    {
        super.onStart();
        System.out.println("OnStart()");
        console.setText("");


        @SuppressLint("SimpleDateFormat") DateFormat h = new SimpleDateFormat("H");
        String time = h.format(Calendar.getInstance().getTime());
        godzina = zamienNaParzysta(Integer.parseInt(time));

        @SuppressLint("SimpleDateFormat") DateFormat m = new SimpleDateFormat("m");
        String min = m.format(Calendar.getInstance().getTime());
        minuta = Integer.parseInt(min);

        @SuppressLint("SimpleDateFormat") DateFormat s = new SimpleDateFormat("s");
        String sec = s.format(Calendar.getInstance().getTime());
        sekunda = Integer.parseInt(sec);

        @SuppressLint("SimpleDateFormat") DateFormat dfo = new SimpleDateFormat("EEE");
        String day = dfo.format(Calendar.getInstance().getTime());
        dzien = zamienDzien(day);

        Calendar calender = Calendar.getInstance();
        numerTygodnia = calender.get(Calendar.WEEK_OF_YEAR); // do zrobienia!


        DBController db = new DBController(this);

        if (db.sprawdzWBazie("Sciezka2") && db.sprawdzWBazie("Login"))
        {
            login = db.pobierzZBazy("Login");
            imie = db.pobierzDane("Imie", login);
            nazwisko = db.pobierzDane("Nazwisko", login);
        }

        Database dba = new Database(this);

        if (db.pobierzZBazy("Przelacznik").equals("false")) // kiedy przelacznik jest ustawiony na automat
        {
            String[] plan = db.pobierzPlan(login, String.valueOf(dzien), String.valueOf(godzina));

            String typ = plan[0];
            sala = plan[1];
            String coDrugi = plan[2];
            String przedmiot = plan[3];
            producent = dba.pobierzProducenta(sala);
            numerTygodnia = zamienNaParzysta(numerTygodnia);


            if (wejscie)
            {
                try {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            onStart();
                            //Toast.makeText(MainActivity.this, "onStart()", Toast.LENGTH_SHORT).show();
                            handler.postDelayed(this, 3600000);
                        }
                    };
                    handler.postDelayed(runnable, odmierzCzas());

                } catch (RuntimeException e) {
                    Toast.makeText(MainActivity.this, "Handler sie posypał", Toast.LENGTH_SHORT).show();
                }
                wejscie = false;
            }


            switch (producent) {
                case "null":
                    blokadaPrzyciskow(false); //false blokuje true odblokowuje

                    Toast.makeText(this, "Przyciski nieaktywne", Toast.LENGTH_LONG).show();
                    log.log("Użytkownik - " + imie + " " + nazwisko);
                    log.log("Brak zajęć - przejdź na tryb ręczny.");
                    log.log("Przyciski nieaktywne.");

                    //log.log("Do pełnej - " + odmierzCzas()/1000 + "sekund");
                    break;

                case "brak rzutnika":
                    blokadaPrzyciskow(false); //false blokuje true odblokowuje

                    Toast.makeText(this, "Przyciski nieaktywne", Toast.LENGTH_LONG).show();
                    log.log("Użytkownik - " + imie + " " + nazwisko);
                    log.log("Przedmiot - " + przedmiot);
                    log.log("Rodzaj zajęć - " + zamienTyp(typ));
                    log.log("Sala - " + sala);
                    log.log("Sala nie posiada rzutnika.");
                    break;

                case "":
                    blokadaPrzyciskow(false); //false blokuje true odblokowuje

                    Toast.makeText(this, "Przyciski nieaktywne", Toast.LENGTH_LONG).show();
                    log.log("Użytkownik - " + imie + " " + nazwisko);
                    log.log("Przedmiot - " + przedmiot);
                    log.log("Rodzaj zajęć - " + zamienTyp(typ));
                    log.log("Sala - " + sala + " nie jest obsługiwana przez aplikację.");
                    break;

                default:
                    //logDeviceInfo();
                    blokadaPrzyciskow(true); //false blokuje true odblokowuje
                    Toast.makeText(this, "Przyciski aktywne", Toast.LENGTH_LONG).show();

                    log.log("Użytkownik - " + imie + " " + nazwisko);
                    log.log("Przedmiot - " + przedmiot);
                    log.log("Rodzaj zajęć - " + zamienTyp(typ));
                    log.log("Sala - " + sala);
                    break;
            }

        }
        else // kiedy przelacznik jest ustawiony na ręczne
        {
            blokadaPrzyciskow(true); //false blokuje true odblokowuje

            sala = db.pobierzZBazy("Spinner"); //pobranie nazwy sali ustawionej w spinnerze
            producent = dba.pobierzProducenta(sala);
            log.log("Tryb ręczny - włączony");
            log.log("Sala - " + sala);
        }


        //infraRed.start();
        updatePrzyciskow();
        db.close();
        dba.close();

    }


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Na pewno chcesz wyjść z aplikacji?");
        builder.setCancelable(true);
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy()");
        infraRed.stop();

        if (log != null) {
            log.destroy();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


//    @Override
//    public void onBackPressed() {
//        finish();
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            handler.removeCallbacksAndMessages(null);
            wejscie = true;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private long odmierzCzas()
    {
        long sekundy;
        sekundy = 1000 * (60 - sekunda);
        sekundy = sekundy + (59 - minuta) * 60000;
        return sekundy;
    }


    private String zamienTyp(String typ)
    {
        switch (typ) {
            case "c":
                return "Ćwiczenia";
            case "l":
                return "Laboratorium";
            default:
                return "Wykład";
        }
    }


    private int zamienNaParzysta(int godzina)
    {
        if (godzina %2 == 0)
            return godzina;
        else
            return godzina-1;
    }


    private int zamienDzien(String dzien)
    {
        switch (dzien)
        {
            case "pon.":
                return 1;
            case "wt.":
                return 2;
            case "śr.":
                return 3;
            case "czw.":
                return 4;
            case "pt.":
                return 5;
            case "sob.":
                return 6;
            default:
                return 7;
        }
    }


    public void blokadaPrzyciskow(Boolean zmienna)
    {
        transmit_button.setEnabled(zmienna);
        input.setEnabled(zmienna);
        freeze.setEnabled(zmienna);
        blank.setEnabled(zmienna);
        noshow.setEnabled(zmienna);
        zoom.setEnabled(zmienna);
        menu.setEnabled(zmienna);
        back.setEnabled(zmienna);
        up.setEnabled(zmienna);
        down.setEnabled(zmienna);
        left.setEnabled(zmienna);
        right.setEnabled(zmienna);
        enter.setEnabled(zmienna);

    }


    public void updatePrzyciskow()
    {
        System.out.println("updatePrzyciskow()" + producent);

        switch (producent) {
            case "Dell":
                freeze.setVisibility(View.INVISIBLE);
                zoom.setVisibility(View.INVISIBLE);
                noshow.setVisibility(View.INVISIBLE);
                break;

            case "Sony":
                freeze.setVisibility(View.INVISIBLE);
                zoom.setVisibility(View.INVISIBLE);
                noshow.setVisibility(View.INVISIBLE);
                break;

            case "Infocus":
                freeze.setVisibility(View.INVISIBLE);
                zoom.setVisibility(View.INVISIBLE);
                noshow.setVisibility(View.INVISIBLE);
                break;

            case "Sanyo":
                freeze.setVisibility(View.VISIBLE);
                zoom.setVisibility(View.INVISIBLE);
                noshow.setVisibility(View.VISIBLE);
                break;

            default:
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$8");
                freeze.setVisibility(View.VISIBLE);
                zoom.setVisibility(View.VISIBLE);
                noshow.setVisibility(View.INVISIBLE);
                break;
        }

        //db.close();
        //dba.close();
    }


    @Override
    public void onClick(View v)
    {

        vibrator.vibrate(80);
        List<PatternConverter> rawPatterns = new ArrayList<>();

        Database db = new Database(this);
        //System.out.println("RadioButton: " + rb);

        int frequency;
        int[] codes;

        switch (v.getId())
        {
            case R.id.transmit_button:
                System.out.println("powerBtn ");

                frequency = db.pobierzCzestotliwosc("Power", sala);
                codes = db.pobierzKody("Power", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                //wysyłanie dla sony
                // rawPatterns.add(PatternConverterUtils.fromHexString(PatternType.Intervals, 38000, "015b 00ad 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0016 0016 0016 0016 0016 0016 0041 0016 0041 0016 0041 0016 06fb 015b 0057 0016 0e6c"));

                //log.log(rb.getText() + ": On/Off");
                log.log(producent  + ": On/Off");
                break;

            case R.id.inputBtn:
                System.out.println("inputBtn ");


                if (producent.equals("Sanyo"))
                {
                    if(licznik == 6)
                        licznik = 0;
                    frequency = db.pobierzCzestotliwosc("Input Computer", sala);
                    codes = db.pobierzInput(licznik);
                    log.log("Input " + licznik);
                    licznik++;

                }
                else
                {
                    frequency = db.pobierzCzestotliwosc("Input Scroll", sala);
                    codes = db.pobierzKody("Input Scroll", sala);
                }
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Input");
                break;

            case R.id.freezeBtn:
                System.out.println("freezeBtn ");
                frequency = db.pobierzCzestotliwosc("Freeze", sala);
                codes = db.pobierzKody("Freeze", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Freeze");
                break;

            case R.id.zoomBtn:
                System.out.println("zoomBtn ");
                frequency = db.pobierzCzestotliwosc("Zoom", sala);
                codes = db.pobierzKody("Zoom", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Zoom");
                break;

            case R.id.noshowBtn:
                System.out.println("noshowBtn ");
                frequency = db.pobierzCzestotliwosc("No Show", sala);
                codes = db.pobierzKody("No Show", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": No Show");
                break;

            case R.id.blankBtn:
                System.out.println("blankBtn ");
                frequency = db.pobierzCzestotliwosc("Blank", sala);
                codes = db.pobierzKody("Blank", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Blank");
                break;

            case R.id.upBtn:
                System.out.println("upBtn ");
                frequency = db.pobierzCzestotliwosc("Cursor Up", sala);
                codes = db.pobierzKody("Cursor Up", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Cursor Up");
                break;

            case R.id.downBtn:
                System.out.println("downBtn ");
                frequency = db.pobierzCzestotliwosc("Cursor Down", sala);
                codes = db.pobierzKody("Cursor Down", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Cursor Down");
                break;

            case R.id.leftBtn:
                System.out.println("leftBtn ");
                frequency = db.pobierzCzestotliwosc("Cursor Left", sala);
                codes = db.pobierzKody("Cursor Left", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Cursor Left");
                break;

            case R.id.rightBtn:
                System.out.println("rightBtn ");
                frequency = db.pobierzCzestotliwosc("Cursor Right", sala);
                codes = db.pobierzKody("Cursor Right", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Cursor Right");
                break;

            case R.id.enterBtn:
                System.out.println("enterBtn ");
                frequency = db.pobierzCzestotliwosc("Cursor Enter", sala);
                codes = db.pobierzKody("Cursor Enter", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Cursor Enter");
                break;

            case R.id.menuBtn:
                System.out.println("menuBtn ");
                frequency = db.pobierzCzestotliwosc("Menu", sala);
                codes = db.pobierzKody("Menu", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Menu");
                break;

            case R.id.backBtn:
                System.out.println("backBtn ");
                frequency = db.pobierzCzestotliwosc("Back", sala);
                codes = db.pobierzKody("Back", sala);
                rawPatterns.add(new PatternConverter(PatternType.Cycles, frequency, codes));

                log.log(producent + ": Back");
                break;
        }


        // adapt the patterns for the device that is used to transmit the patterns
        patternAdapter = new PatternAdapter(log, transmitterType);

        TransmitInfo[] transmitInfoArray = new TransmitInfo[rawPatterns.size()];
        for (int i = 0; i < transmitInfoArray.length; i++) {
            transmitInfoArray[i] = patternAdapter.createTransmitInfo(rawPatterns.get(i));
        }
        this.patterns = transmitInfoArray;

        for (TransmitInfo transmitInfo : this.patterns) {
            //log.log(transmitInfo.toString());
        }

        //log.log("Version: " + currentPattern);
        TransmitInfo transmitInfo = patterns[currentPattern++];
        if (currentPattern >= patterns.length) currentPattern = 0;
        infraRed.transmit(transmitInfo);
    }

}