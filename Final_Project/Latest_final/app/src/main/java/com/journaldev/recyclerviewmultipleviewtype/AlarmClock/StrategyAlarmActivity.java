package com.journaldev.recyclerviewmultipleviewtype.AlarmClock;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.journaldev.recyclerviewmultipleviewtype.R;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class StrategyAlarmActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener, Animation.AnimationListener {

    AlarmManager alarmManager;
    private PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private static StrategyAlarmActivity inst;
    private TextView alarmTextView;

    private StrategyAlarmReceiver alarm;
    private Context context;
    Spinner spinner;
    int quote = 0;

    TextView btnTimePicker;
    Button Back;
    TextView txtTime;
    public int mHour, mMinute;
    private int setHoure, setMinute;


    ImageView imgPoster;
    Button btnStart;

    // Animation
    Animation animZoomIn;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Realm Configuration
        ///////////////////////////////////////////////////////////////
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        //////////////////////////////////////////////////////////////

        //alarm = new StrategyAlarmReceiver();
        alarmTextView = (TextView) findViewById(R.id.alarmText);


        // load the animation
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);

        // set animation listener
        animZoomIn.setAnimationListener(this);


        // start the animation
        alarmTextView .startAnimation(animZoomIn);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
//        setSupportActionBar(toolbar);


        this.context = this;


        final Intent myIntent = new Intent(this.context, StrategyAlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.SECOND, 3);
//        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);

        //spinner creation
        Spinner spinner = (Spinner) findViewById(R.id.richard_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sounds, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);


        Button start_alarm= (Button) findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {
                    // Bug : setHoure != 0 && setMinute != 0 was in if statement
                    if (true) {
                        calendar.set(Calendar.HOUR_OF_DAY, setHoure);
                        calendar.set(Calendar.MINUTE, setMinute);
                        final int hour = setHoure;
                        final int minute = setMinute;
//                        save_sleep_database(setHoure, setMinute);

                        String minute_string = String.valueOf(minute);
                        String hour_string = String.valueOf(hour);

                        if (minute < 10) {
                            minute_string = "0" + String.valueOf(minute);
                        }

                        if (hour > 12) {
                            hour_string = String.valueOf(hour - 12);
                        }


                        myIntent.putExtra("extra", "yes");
                        myIntent.putExtra("quote id", String.valueOf(quote));
                        pending_intent = PendingIntent.getBroadcast(StrategyAlarmActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                        setAlarmText("Alarm set to " + hour_string + ":" + minute_string);
                    } else {
                        CharSequence text = "Set The Clock First!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
            }

        });

        txtTime=(TextView)findViewById(R.id.in_time);
//        txtTime.setText("Last Alarm is set to " + get_last_hours_db() + ":" + get_last_minutes_db());


        btnTimePicker=(TextView) findViewById(R.id.in_time_2);
        btnTimePicker.setOnClickListener(this);

        Back = (Button) findViewById(R.id.back);
        Back.setOnClickListener(this);


        Button stop_alarm= (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTime.setText("Reset! ");
                if (setHoure != 0 && setMinute != 0) {
                    myIntent.putExtra("extra", "no");
                    myIntent.putExtra("quote id", String.valueOf(quote));
                    sendBroadcast(myIntent);

                    alarmManager.cancel(pending_intent);
                    setAlarmText("Alarm canceled");


                    //setAlarmText("ID is " + richard_quote);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
            if (v == btnTimePicker) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                setHoure = hourOfDay;
                                setMinute = minute;
                                save_sleep_database(
                                        setHoure,
                                        setMinute);
                                String minute_string = "" + minute;
                                String hour_string = "" + hourOfDay;
                                if (minute < 10) {
                                    minute_string = "0" + String.valueOf(minute);
                                }
                                if (hourOfDay > 12) {
                                    hour_string = String.valueOf(hourOfDay - 12);
                                }
                                txtTime.setText("Set Alarm to " + hour_string + ":" + minute_string + " ?");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            } else if (v == Back) {
                onBackPressed();
            }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(parent.getContext(), "Spinner item 3!" + id, Toast.LENGTH_SHORT).show();
        quote = (int) id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animZoomIn) {
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //************************************************************************************************
    // Save into Screen Off Data
    //************************************************************************************************
    private void save_sleep_database(
            final int SleepHours,
            final int SleepMinutes) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                BedTimeHours bedTimeHours = bgRealm.createObject(BedTimeHours.class);
                bedTimeHours.setSleepHours(SleepHours);
                bedTimeHours.setSleepMinutes(SleepMinutes);
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
    //get_last_item_from_max_db
    //************************************************************************************************
    Realm realm;
    //after commitTransaction,
    private int get_last_hours_db() {
        RealmResults<BedTimeHours> allTransactions = realm.where(BedTimeHours.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int hours= allTransactions.last().getSleepHours();
        return hours;
    }

    //************************************************************************************************
    //get_last_item_from_max_db
    //************************************************************************************************
    //after commitTransaction,
    private int get_last_minutes_db() {
        RealmResults<BedTimeHours> allTransactions = realm.where(BedTimeHours.class).findAll();
        if (allTransactions.isEmpty()){
            return 0;
        }
        //If you have an incrementing id column, do this
        int minutes= allTransactions.last().getSleepMinutes();
        return minutes;
    }

    //************************************************************************************************
    // Clear Database
    //************************************************************************************************
    private void clear_DB() {
        // obtain the results of a query
        final RealmResults<BedTimeHours> results = realm.where(BedTimeHours.class).findAll();
        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}