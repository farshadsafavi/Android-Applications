package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.journaldev.recyclerviewmultipleviewtype.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class SleepGameActivity extends Activity implements Animation.AnimationListener {
    // UI Views
    TextView     sleepTextView;
    NumberPicker numberPicker;
    Animation    animZoomIn;
    ProgressBar  progressBar;
    ImageView    dog_image;
    BarChart     barChart;

    long    hour_milisecond;
    int     expectedSleep;
    int     progressStatus = 0;

    // Realm Object
    private Realm realm;

    // Constants
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    int TodayYear, TodayMonth, TodayDay;


    //***********************************************************************
    //initialization
    //***********************************************************************
    public void initialization(){
        // Today CalanderFragment
        Calendar c = Calendar.getInstance();
        TodayYear = c.get(Calendar.YEAR);
        TodayMonth = c.get(Calendar.MONTH);
        TodayDay = c.get(Calendar.DAY_OF_MONTH);
        // Realm Configuration
        ///////////////////////////////////////////////////////////////
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        //////////////////////////////////////////////////////////////

        sleepTextView  = (TextView) findViewById(R.id.sleepText);
        animZoomIn     =  AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animZoomIn.setAnimationListener(this);
        sleepTextView .startAnimation(animZoomIn);
        // Progress Bar initialization
        progressBar    = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setMax((int) hour_milisecond);
        progressStatus = 0;
        // Number Picker initialization
        numberPicker   = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setValue(get_last_target_from_db());
        numberPicker.setEnabled(false);
        // Image View initialization
        dog_image      = (ImageView) findViewById(R.id.sleep);
        // Barchart initialization
        barChart       = (BarChart) findViewById(R.id.barChart);
        barChart.setVisibility(View.GONE);

    }

    //***********************************************************************
    //onCreate
    //***********************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_game);

        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);

        initialization();


    }
    //************************************************************************************************
    // Update
    //************************************************************************************************
    public void handleClickUpdate(View view){
        readFile();
    }

    //************************************************************************************************
    // Score
    //************************************************************************************************
    public void handleClickScore(View view){
        score_method();
    }

    //************************************************************************************************
    // Save
    //************************************************************************************************
    public void handleClickSave(View view){
        save_method();
    }

    //************************************************************************************************
    // History
    //************************************************************************************************
    public void handleClickHistory(View view){
        changeFragment(new SleepGameListFragment());;
    }


    //************************************************************************************************
    // Save Method
    //************************************************************************************************
    public void score_method () {
        expectedSleep = numberPicker.getValue();
        hour_milisecond =  expectedSleep * 3600000;
        progressBar.setMax((int) hour_milisecond);
        progressStatus = (int)(get_last_item_from_max_db());
        progressBar.setProgress(progressStatus);
        String time_diff = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(get_last_item_from_max_db()),
                TimeUnit.MILLISECONDS.toMinutes(get_last_item_from_max_db()) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(get_last_item_from_max_db())),
                TimeUnit.MILLISECONDS.toSeconds(get_last_item_from_max_db()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(get_last_item_from_max_db())));
        sleepTextView.setText( "Max Achieved: "  + (time_diff));
        sleepTextView .startAnimation(animZoomIn);
        dog_image.setVisibility(View.GONE);
        refresh_views_max();
//        refresh_views();
        updateBarChart();
    }

    //************************************************************************************************
    // Update Barchart
    //************************************************************************************************
    void updateBarChart() {
        initialization();
        barChart.setVisibility(View.VISIBLE);
        Realm realm = Realm.getDefaultInstance();

        final RealmResults<ScreenOffData> resultsScreenOffData = realm.where(ScreenOffData.class).findAll();

        BarChart barChart = (BarChart) findViewById(R.id.barChart);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int i = 0;

        for (ScreenOffData screenOffData : resultsScreenOffData) {
            if (screenOffData.getScreenOffDay() == TodayDay &&
                    screenOffData.getScreenOffMonth() == TodayMonth &&
                    screenOffData.getScreenOffYear() == TodayYear) {
                entries.add(new BarEntry(screenOffData.screenOffDuration / 60000, i));
                labels.add(String.valueOf(screenOffData.getScreenOffhour()) + ":" + String.valueOf(screenOffData.getScreenOffMinute()));
                i++;
            }

        }
        BarDataSet dataset = new BarDataSet(entries, "Screen Off Duration in Minutes");
        BarData data_1 = new BarData(labels, dataset);
        barChart.setData(data_1);
        barChart.animateY(1500);
    }

    //************************************************************************************************
    // Clear Database
    //************************************************************************************************
    private void clear_DB() {
        // obtain the results of a query
        final RealmResults<ScreenOffData> results = realm.where(ScreenOffData.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

    //************************************************************************************************
    // Clear Database
    //************************************************************************************************
    private void clear_screenOn_DB() {
        // obtain the results of a query
        final RealmResults<ScreenOnData> results = realm.where(ScreenOnData.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

    //************************************************************************************************
    // Clear Database
    //************************************************************************************************
    private void clear_DB_max() {
        // obtain the results of a query
        final RealmResults<ScreenOffDataMax> results = realm.where(ScreenOffDataMax.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }


    //************************************************************************************************
    //Read line by line
    //************************************************************************************************
    public void readFile() {
        long timediffMax = 0;
        long screenOffTimeMax = get_last_max_screenOff_from_db();
        int  screenOffYearMax  = get_last_max_year_from_db();
        int  screenOffMonthMax = get_last_max_mounth_from_db();
        int  screenOffDayMax   = get_last_max_day_from_db();
        int  screenOffhourMax  = get_last_max_hour_from_db();
        int  screenOffMinuteMax= get_last_max_minute_from_db();
        int  screenOffSecondMax= get_last_max_second_from_db();
        // Values from last items in the database
        int  screenOffYear     = get_last_item_year_from_db();
        int  screenOffMonth    = get_last_item_mounth_from_db();
        int  screenOffDay      = get_last_item_day_from_db();
        int  screenOffhour     = get_last_item_hour_from_db();
        int  screenOffMinute   = get_last_item_minute_from_db();
        int  screenOffSecond   = get_last_item_second_from_db();
        long screenOffTime     = get_last_item_screenoff_from_db();
        long timediff          = get_last_timediff_from_db();
        // Values from last items in the database
        int  screenOnYear        = get_last_screenOn_year_from_db();
        int  screenOnMonth       = get_last_screenOn_month_from_db();
        int  screenOnDay         = get_last_screenOn_day_from_db();
        int  screenOnhour        = get_last_screenOn_hour_from_db();
        int  screenOnMinute      = get_last_screenOn_minute_from_db();
        int  screenOnSecond      = get_last_screenOn_second_from_db();
        long screenOnCurrentTime = get_last_screenOn_currentTime_from_db();
        long screenOnDuration    = get_last_screenOn_duration_from_db();

        // This will reference one line at a time
        String line = null;
        String line_screenOn = null;

        // If Screen On file is empty initial current day
        Calendar c      = Calendar.getInstance();
        screenOnYear   = c.get(Calendar.YEAR);
        screenOnMonth  = c.get(Calendar.MONTH);
        screenOnDay    = c.get(Calendar.DAY_OF_MONTH);

        // The name of the file to open.
        File fileName = new File(android.os.Environment.getExternalStorageDirectory() + "/ScreenOff_Info.txt");
        File fileScreenOn = new File(android.os.Environment.getExternalStorageDirectory() + "/ScreenOn_Info.txt");



        refresh_views_max();
        if (fileName.exists() && fileScreenOn.exists()) {

            // Read ScreenOns
            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader =
                        new FileReader(fileScreenOn);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while ((line_screenOn = bufferedReader.readLine()) != null) {
                    String CurrentString = line_screenOn;
                    String[] separated = CurrentString.split("-");
                    screenOnCurrentTime = Long.valueOf(separated[0]);
                    screenOnDuration = Long.valueOf(separated[1]);
                    screenOnYear = Integer.valueOf(separated[2]);
                    screenOnMonth = Integer.valueOf(separated[3]);
                    screenOnDay = Integer.valueOf(separated[4]);
                    screenOnhour = Integer.valueOf(separated[5]);
                    screenOnMinute = Integer.valueOf(separated[6]);
                    screenOnSecond = Integer.valueOf(separated[7]);
                }
                if( screenOnYear   == 0 &&
                    screenOnMonth  == 0 &&
                    screenOnDay    == 0 &&
                    screenOnhour   == 0 &&
                    screenOnMinute == 0 &&
                    screenOnSecond == 0 ){
                    Log.v("RaedFile", "Not acceptable");
                } else {
                    save_into_screenOn_database(
                            screenOnCurrentTime,
                            screenOnDuration,
                            screenOnYear,
                            screenOnMonth,
                            screenOnDay,
                            screenOnhour,
                            screenOnMinute,
                            screenOnSecond);
                }
                // Always close files.
                bufferedReader.close();
            } catch (FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            } catch (IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
                // Or we could just do this:
                // ex.printStackTrace();
            }
            // Read ScreenOffs
            try {
                // FileReader reads text files in the default encoding.
                FileReader fileReader =
                        new FileReader(fileName);

                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    String CurrentString = line;
                    String[] separated = CurrentString.split("-");
                    screenOffTime = Long.valueOf(separated[0]);
                    timediff = Long.valueOf(separated[1]);
                    screenOffYear = Integer.valueOf(separated[2]);
                    screenOffMonth = Integer.valueOf(separated[3]);
                    screenOffDay = Integer.valueOf(separated[4]);
                    screenOffhour = Integer.valueOf(separated[5]);
                    screenOffMinute = Integer.valueOf(separated[6]);
                    screenOffSecond = Integer.valueOf(separated[7]);
                    // If the the gap between screen off is less than 3 minutes it assumes screen off activity is
                    // continuous.
                    if (screenOnYear == 0 &&
                            screenOnMonth == 0 &&
                            screenOnDay == 0 &&
                            screenOnhour == 0 &&
                            screenOnMinute == 0 &&
                            screenOnSecond == 0) {
                        Log.v("RaedFile", "Not acceptable");
                    } else {
                        if (screenOnYear == get_last_screenOn_year_from_db() &&
                                screenOnMonth == get_last_item_mounth_from_db() &&
                                screenOnDay == get_last_item_day_from_db() &&
                                screenOnhour == get_last_screenOn_hour_from_db() &&
                                screenOnDuration <= 180 * 1000) {
                            screenOffTime   = get_last_item_screenoff_from_db();
                            timediff        = timediff + get_last_timediff_from_db();
                            screenOffYear   = get_last_item_year_from_db();
                            screenOffMonth  = get_last_item_mounth_from_db();
                            screenOffDay    = get_last_item_day_from_db();
                            screenOffhour   = get_last_item_hour_from_db();
                            screenOffMinute = get_last_item_minute_from_db();
                            screenOffSecond = get_last_item_second_from_db();

                        }
                    }
                    save_into_database(
                            screenOffTime,
                            timediff,
                            screenOffYear,
                            screenOffMonth,
                            screenOffDay,
                            screenOffhour,
                            screenOffMinute,
                            screenOffSecond);


                    // Capture The Max time the device is screen off
                    if (timediff > timediffMax) {
                        timediffMax = timediff;
                        screenOffTimeMax = screenOffTime;
                        screenOffYearMax = screenOffYear;
                        screenOffMonthMax = screenOffMonth;
                        screenOffDayMax = screenOffDay;
                        screenOffhourMax = screenOffhour;
                        screenOffMinuteMax = screenOffMinute;
                        screenOffSecondMax = screenOffSecond;
                    }

                }
                // Always close files.
                bufferedReader.close();
            } catch (FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            } catch (IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
                // Or we could just do this:
                // ex.printStackTrace();
            }

            // ScreenOn values are saved when the last time screen is on
            if (TodayYear == get_last_item_year_from_db() &&
                    TodayMonth == get_last_item_mounth_from_db() &&
                    TodayDay== get_last_item_day_from_db() ) {
                if (timediffMax > get_last_item_from_max_db()) {
                    remove_last_max_from_DB();
                    save_into_ScreenOffDataMax_database(
                            numberPicker.getValue(),
                            screenOffTimeMax,
                            timediffMax,
                            screenOffYearMax,
                            screenOffMonthMax,
                            screenOffDayMax,
                            screenOffhourMax,
                            screenOffMinuteMax,
                            screenOffSecondMax);

                }
                Log.v("[BroadcastReceiver]", "Removed, last item is: " + get_last_item_from_max_db());
            }
            else {
                Log.v("[BroadcastReceiver]", "Not Removed, last item is: " + get_last_item_from_max_db());
                save_into_ScreenOffDataMax_database(
                        numberPicker.getValue(),
                        screenOffTimeMax,
                        timediffMax,
                        screenOffYearMax,
                        screenOffMonthMax,
                        screenOffDayMax,
                        screenOffhourMax,
                        screenOffMinuteMax,
                        screenOffSecondMax);
//                clear_DB();
//                clear_screenOn_DB();
            }
            boolean deleted = fileName.delete();
            if (deleted) {
                Log.v("ReadFile", "File Deleted!");
            }
            boolean deleted_1 = fileScreenOn.delete();
            if (deleted_1) {
                Log.v("ReadFile", "File Deleted!");
            }
        } else {
            Log.v("ReadFile", "Files not exist!");
        }
        final RealmResults<ScreenOffDataMax> resultsScreenOffData = realm.where(ScreenOffDataMax.class).findAll();
        if (resultsScreenOffData.size() > 31){
            clear_DB_max();
        }

        final RealmResults<ScreenOffData> resultsScreenOff = realm.where(ScreenOffData.class).findAll();
        if (resultsScreenOffData.size() > 80){
            clear_DB();
            clear_screenOn_DB();
        }
    }

    //************************************************************************************************
    // Save Method
    //************************************************************************************************
    public void save_method (){
        expectedSleep = 0;
        progressStatus = 0;
        numberPicker.setEnabled(false);

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to save " + numberPicker.getValue() + " hours as a target?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                numberPicker.setEnabled(false);
                progressBar.setMax((int) hour_milisecond);
                progressBar.setProgress(progressStatus);
                remove_last_target_from_DB();
                save_into_targetsleephours_database(numberPicker.getValue());
                sleepTextView.setText("Target Sleep " + numberPicker.getValue()+ " Hours");
                modifySave(numberPicker.getValue());
                sleepTextView .startAnimation(animZoomIn);
            }

        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numberPicker.setEnabled(true);
            }
        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    //************************************************************************************************
    // Save into Screen Off Data
    //************************************************************************************************
    private void save_into_database(
                                    final long currentTime1,
                                    final long screenOffDuration1,
                                    final int screenOffYear1,
                                    final int screenOffMonth1,
                                    final int screenOffDay1,
                                    final int screenOffhour1,
                                    final int screenOffMinute1,
                                    final int screenOffSecond1) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ScreenOffData screenOffData = bgRealm.createObject(ScreenOffData.class);
                screenOffData.setCurrentTime(currentTime1);
                screenOffData.setScreenOffDuration(screenOffDuration1);
                screenOffData.setScreenOffYear(screenOffYear1);
                screenOffData.setScreenOffMonth(screenOffMonth1);
                screenOffData.setScreenOffDay(screenOffDay1);
                screenOffData.setScreenOffhour(screenOffhour1);
                screenOffData.setScreenOffMinute(screenOffMinute1);
                screenOffData.setScreenOffSecond(screenOffSecond1);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //adapter.notifyDataSetChanged();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("save_into_database", error.getMessage());
            }
        });

    }

    //************************************************************************************************
    // Save into Screen On Data
    //************************************************************************************************
    private void save_into_screenOn_database(
            final long currentTime1,
            final long screenOnDuration1,
            final int screenOnYear1,
            final int screenOnMonth1,
            final int screenOnDay1,
            final int screenOnhour1,
            final int screenOnMinute1,
            final int screenOnSecond1) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ScreenOnData screenOnData = bgRealm.createObject(ScreenOnData.class);
                screenOnData.setCurrentTime(currentTime1);
                screenOnData.setScreenOnDuration(screenOnDuration1);
                screenOnData.setScreenOnYear(screenOnYear1);
                screenOnData.setScreenOnMonth(screenOnMonth1);
                screenOnData.setScreenOnDay(screenOnDay1);
                screenOnData.setScreenOnhour(screenOnhour1);
                screenOnData.setScreenOnMinute(screenOnMinute1);
                screenOnData.setScreenOnSecond(screenOnSecond1);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //adapter.notifyDataSetChanged();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("save_into_database", error.getMessage());
            }
        });

    }


    //************************************************************************************************
    // Save into Screen Off Data Max
    //************************************************************************************************
    private void save_into_ScreenOffDataMax_database(final int  targetSleep,
                                                     final long currentTimeLongMax,
                                                     final long screenOffDuration,
                                                     final int screenOffYear,
                                                     final int screenOffMonth,
                                                     final int screenOffDay,
                                                     final int screenOffhour,
                                                     final int screenOffMinute,
                                                     final int screenOffSecond) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ScreenOffDataMax screenOffDataMax = bgRealm.createObject(ScreenOffDataMax.class);
                screenOffDataMax.setTargetSleep(targetSleep);
                screenOffDataMax.setCurrentTime(currentTimeLongMax);
                screenOffDataMax.setScreenOffDurationMax(screenOffDuration);
                screenOffDataMax.setScreenOffYearMax(screenOffYear);
                screenOffDataMax.setScreenOffMonthMax(screenOffMonth);
                screenOffDataMax.setScreenOffDayMax(screenOffDay);
                screenOffDataMax.setScreenOffhourMax(screenOffhour);
                screenOffDataMax.setScreenOffMinuteMax(screenOffMinute);
                screenOffDataMax.setScreenOffSecondMax(screenOffSecond);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //adapter.notifyDataSetChanged();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("save_ScreenOffDataMax", error.getMessage());
            }
        });

    }


    //************************************************************************************************
    // See log view of database
    //************************************************************************************************
    private void refresh_views() {
        // read from DB > show in textView_log
        RealmResults<ScreenOffData> ScreenOffDataRealmResults = realm.where(ScreenOffData.class).findAll();
        String output = "";
        for (ScreenOffData screenOffData : ScreenOffDataRealmResults) {
            output = screenOffData.toString();
            Log.v("output ", output);
        }
    }

    //************************************************************************************************
    // See log view of  max database
    //************************************************************************************************
    private void refresh_views_max() {
        // read from DB > show in textView_log
        RealmResults<ScreenOffDataMax> Results = realm.where(ScreenOffDataMax.class).findAll();
        Log.v("refresh_views_max ", Results.toString());
        String output = "";
        for (ScreenOffDataMax screenOffDataMax : Results) {
            output = screenOffDataMax.toString();
            Log.v("output ", output);
        }
    }

    //************************************************************************************************
    //get_last_item_from_max_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_item_from_max_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long ScreenOffDurationMax = allTransactions.last().getScreenOffDurationMax();
        return ScreenOffDurationMax;
    }

    //************************************************************************************************
    //int get_first_item_year_from_max_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_first_item_year_from_max_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int year= allTransactions.first().getScreenOffYearMax();
        return year;
    }

    //************************************************************************************************
    //get_first_item_month_from_max_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_first_item_month_from_max_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int month= allTransactions.first().getScreenOffMonthMax();
        return month;
    }

    //************************************************************************************************
    //get_first_item_month_from_max_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_first_item_day_from_max_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int day= allTransactions.first().getScreenOffDayMax();
        return day;
    }


    //************************************************************************************************
    //get_last_item_year_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_year_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_year = allTransactions.last().getScreenOffYear();
        return last_year;
    }

    //************************************************************************************************
    //get_last_item_mounth_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_mounth_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_month = allTransactions.last().getScreenOffMonth();
        return last_month;
    }

    //************************************************************************************************
    //get_last_item_day_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_day_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_day = allTransactions.last().getScreenOffDay();
        return last_day;
    }

    //************************************************************************************************
    //get_last_item_hour_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_hour_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_hour= allTransactions.last().getScreenOffhour();
        return last_hour;
    }

    //************************************************************************************************
    //get_last_item_minute_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_minute_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_minute =  allTransactions.last().getScreenOffMinute();
        return last_minute;
    }

    //************************************************************************************************
    //get_last_item_second_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_item_second_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_second =  allTransactions.last().getScreenOffSecond();
        return last_second;
    }

    //************************************************************************************************
    //get_last_item_screenOff_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_item_screenoff_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long currentTime =  allTransactions.last().getCurrentTime();
        return currentTime;
    }

    //************************************************************************************************
    //get_first_item_currentTime_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_first_item_currentTime_from_max_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long currentTime =  allTransactions.first().getCurrentTime();
        return currentTime;
    }

    //************************************************************************************************
    //get_last_screenOn_year_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_year_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_year = allTransactions.last().getScreenOnYear();
        return last_year;
    }

    //************************************************************************************************
    //get_last_screenOn_mounth_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_month_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_month = allTransactions.last().getScreenOnMonth();
        return last_month;
    }

    //************************************************************************************************
    //get_last_screenOn_day_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_day_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_day = allTransactions.last().getScreenOnDay();
        return last_day;
    }

    //************************************************************************************************
    //get_last_screenOn_hour_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_hour_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_hour= allTransactions.last().getScreenOnhour();
        return last_hour;
    }

    //************************************************************************************************
    //get_last_screenOn_minute_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_minute_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_minute =  allTransactions.last().getScreenOnMinute();
        return last_minute;
    }

    //************************************************************************************************
    //get_last_screenOn_second_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_screenOn_second_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_second =  allTransactions.last().getScreenOnSecond();
        return last_second;
    }

    //************************************************************************************************
    //get_last_screenOn_currentTime_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_screenOn_currentTime_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long currentTime =  allTransactions.last().getCurrentTime();
        return currentTime;
    }

    //************************************************************************************************
    //get_last_screenOn_duration_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_screenOn_duration_from_db() {
        RealmResults<ScreenOnData> allTransactions = realm.where(ScreenOnData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long duration =  allTransactions.last().getCurrentTime();
        return duration;
    }

    //************************************************************************************************
    //get_last_item_target_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_target_from_db() {
        RealmResults<TargetSleepHours> allTransactions = realm.where(TargetSleepHours.class).findAll();
        if (allTransactions.isEmpty()){
            return 8;
        }
        //If you have an incrementing id column, do this
        int last_target = allTransactions.last().getTargetSleepHours();
        return last_target;
    }


    //************************************************************************************************
    //remove_last_item_from_targethours_db
    //************************************************************************************************
    private void remove_last_target_from_DB() {
        // obtain the results of a query
        final RealmResults<TargetSleepHours> results = realm.where(TargetSleepHours.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteLastFromRealm();
            }
        });
    }

    //************************************************************************************************
    //remove_last_item_from_max_db
    //************************************************************************************************
    private void remove_last_max_from_DB() {
        // obtain the results of a query
        final RealmResults<ScreenOffDataMax> results = realm.where(ScreenOffDataMax.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteLastFromRealm();
            }
        });
    }

    //************************************************************************************************
    //remove_last_item_from_db
    //************************************************************************************************
    private void remove_last_item_from_DB() {
        // obtain the results of a query
        final RealmResults<ScreenOffData> results = realm.where(ScreenOffData.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteLastFromRealm();
            }
        });
    }


    //************************************************************************************************
    //remove_first_item_from_max_db
    //************************************************************************************************
    private void remove_first_item_max_from_DB() {
        // obtain the results of a query
        final RealmResults<ScreenOffDataMax> results = realm.where(ScreenOffDataMax.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFirstFromRealm();
            }
        });
    }

    //************************************************************************************************
    //get_last_item_timediff_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_timediff_from_db() {
        RealmResults<ScreenOffData> allTransactions = realm.where(ScreenOffData.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long last_timediff = allTransactions.last().getScreenOffDuration();
        return last_timediff;
    }

    //************************************************************************************************
    //remove_last_item_from_max_db
    //************************************************************************************************
    private void save_into_targetsleephours_database(
                                                     final int TargetSleepHours) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking.TargetSleepHours targetSleepHours = bgRealm.createObject(com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking.TargetSleepHours.class);
                targetSleepHours.setTargetSleepHours(TargetSleepHours);

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //adapter.notifyDataSetChanged();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("Save_taget", error.getMessage());
            }
        });

    }


    //************************************************************************************************
    //Change Fragment
    //************************************************************************************************
    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //************************************************************************************************
    // get_last_max_year_from_db()
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_year_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_year = allTransactions.last().getScreenOffYearMax();
        return last_year;
    }

    //************************************************************************************************
    //get_last_max_mounth_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_mounth_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_month = allTransactions.last().getScreenOffMonthMax();
        return last_month;
    }

    //************************************************************************************************
    //get_last_max_day_from_db()
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_day_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_day = allTransactions.last().getScreenOffDayMax();
        return last_day;
    }

    //************************************************************************************************
    //get_last_max_hour_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_hour_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_hour= allTransactions.last().getScreenOffhourMax();
        return last_hour;
    }

    //************************************************************************************************
    //get_last_max_minute_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_minute_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_minute =  allTransactions.last().getScreenOffMinuteMax();
        return last_minute;
    }

    //************************************************************************************************
    //get_last_max_second_from_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_max_second_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int last_second =  allTransactions.last().getScreenOffSecondMax();
        return last_second;
    }

    //************************************************************************************************
    //get_last_max_second_from_db
    //************************************************************************************************
    //after commitTransaction,
    private long get_last_max_screenOff_from_db() {
        RealmResults<ScreenOffDataMax> allTransactions = realm.where(ScreenOffDataMax.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        long last_screen_off=  allTransactions.last().getCurrentTime();
        return last_screen_off;
    }

    //************************************************************************************************
    //askForPermission
    //************************************************************************************************
    private void modifySave(int newTarget) {
        int  target_sleep_hours = newTarget;
        long screenOffTimeMax = get_last_max_screenOff_from_db();
        long screenOfftimediffMax = get_last_item_from_max_db();
        int screenOffYearMax  = get_last_max_year_from_db();
        int screenOffMonthMax = get_last_max_mounth_from_db();
        int screenOffDayMax   = get_last_max_day_from_db();
        int screenOffhourMax  = get_last_max_hour_from_db();
        int screenOffMinuteMax= get_last_max_minute_from_db();
        int screenOffSecondMax= get_last_max_second_from_db();
        if (screenOffTimeMax != 0) {
            remove_last_max_from_DB();
            save_into_ScreenOffDataMax_database(
                    target_sleep_hours,
                    screenOffTimeMax,
                    screenOfftimediffMax,
                    screenOffYearMax,
                    screenOffMonthMax,
                    screenOffDayMax,
                    screenOffhourMax,
                    screenOffMinuteMax,
                    screenOffSecondMax
            );
        }

    }



    //************************************************************************************************
    //askForPermission
    //************************************************************************************************
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            //    Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }


    //************************************************************************************************
    //onRequestPermissionsResult
    //************************************************************************************************
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    //************************************************************************************************
    //onRequestPermissionsResult
    //************************************************************************************************
    private void clear_first_data() {

        Realm realm = Realm.getDefaultInstance();



    }


    //************************************************************************************************
    // BackPress Method
    //************************************************************************************************
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //************************************************************************************************
    // Animation Methods
    //************************************************************************************************
    @Override
    public void onAnimationEnd(Animation animation) {}
    @Override
    public void onAnimationRepeat(Animation animation) {}
    @Override
    public void onAnimationStart(Animation animation) {}


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test Bench
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void test_bench() {
        clear_DB();
        clear_DB_max();
        clear_screenOn_DB();

        Calendar c = Calendar.getInstance();
        long currentTimeLong = System.currentTimeMillis() - (3600_000) * 24 * 2;
        int screenOffYear = TodayYear;
        int screenOffMonth = TodayMonth;
        int screenOffDay = TodayDay;
        int screenOffhour = 8;
        int screenOffMinute = 45;
        int screenOffSecond = 0;
        int timediff = 15_000_500;

        for (int i = 20; i > 5; i--) {
            TodayYear = c.get(Calendar.YEAR);
            TodayMonth = c.get(Calendar.MONTH);
            TodayDay = c.get(Calendar.DAY_OF_MONTH) - i;
            screenOffYear = TodayYear;
            screenOffMonth = 10;
            screenOffDay = 30;
            screenOffhour = 8;
            screenOffMinute = 45;
            screenOffSecond = 0;
            int hours = i % 10;
            timediff = ((3600_000 * (hours + 2))) ;
            for (int j = 1; j< 4; j++) {
                save_into_database(currentTimeLong - (3600_000) * 24 * i,
                        timediff + 3600000*j,
                        screenOffYear,
                        screenOffMonth,
                        screenOffDay - i,
                        screenOffhour + j*2,
                        screenOffMinute,
                        screenOffSecond);
                save_into_screenOn_database(currentTimeLong - (3600_000) * 24 * i,
                        timediff + 3600000*j ,
                        screenOffYear,
                        screenOffMonth,
                        screenOffDay - i,
                        screenOffhour + j*2,
                        screenOffMinute,
                        screenOffSecond);

            }
            int time_picker = i % 2;
            save_into_ScreenOffDataMax_database(12 - time_picker,
                    currentTimeLong - (3600_000) * 24 * i,
                    timediff + 723456,
                    screenOffYear,
                    screenOffMonth,
                    screenOffDay - i,
                    screenOffhour,
                    screenOffMinute,
                    screenOffSecond);

        }

//
//        TodayYear = c.get(Calendar.YEAR);
//        TodayMonth = c.get(Calendar.MONTH);
//        TodayDay = c.get(Calendar.DAY_OF_MONTH) - 6 ;
//        screenOffYear = TodayYear;
//        screenOffMonth = TodayMonth;
//        screenOffDay = TodayDay;
//        screenOffhour = 10;
//        screenOffMinute = 45;
//        screenOffSecond = 0;
//        timediff = 8_000_500;
//        for (int j = 7; j > 0; j--) {
//            save_into_database(currentTimeLong - (3600_000) * 24 * 6,
//                    timediff + 3600000*j,
//                    screenOffYear,
//                    screenOffMonth,
//                    screenOffDay,
//                    screenOffhour - 3*j,
//                    screenOffMinute,
//                    screenOffSecond);
//        }
//
//        save_into_ScreenOffDataMax_database( numberPicker.getValue(),
//                currentTimeLong - (3600_000) * 24 * 6,
//                timediff,
//                screenOffYear,
//                screenOffMonth,
//                screenOffDay,
//                screenOffhour,
//                screenOffMinute,
//                screenOffSecond
//        );
//        TodayYear = c.get(Calendar.YEAR);
//        TodayMonth = c.get(Calendar.MONTH);
//        TodayDay = c.get(Calendar.DAY_OF_MONTH) - 5;
//        screenOffYear = TodayYear;
//        screenOffMonth = TodayMonth;
//        screenOffDay = TodayDay;
//        screenOffhour = 9;
//        screenOffMinute = 45;
//        screenOffSecond = 0;
//        timediff = 10_000_500;
//        for (int j = 7; j > 0; j--) {
//            save_into_database(currentTimeLong - (3600_000) * 24 * 5,
//                    timediff + + 3600000*j,
//                    screenOffYear,
//                    screenOffMonth,
//                    screenOffDay,
//                    screenOffhour + 3*j,
//                    screenOffMinute,
//                    screenOffSecond);
//        }
//
//        save_into_ScreenOffDataMax_database( numberPicker.getValue(),
//                currentTimeLong - (3600_000) * 24 * 5,
//                timediff,
//                screenOffYear,
//                screenOffMonth,
//                screenOffDay,
//                screenOffhour,
//                screenOffMinute,
//                screenOffSecond
//        );
//
    }



}