package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking.ServiceScreen;
import com.journaldev.recyclerviewmultipleviewtype.Strategy.StrategyFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    public String TAG="OOO";

    public String resourceToShowTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start Caoture Screen Off Activity
        stopService(new Intent(this, ServiceScreen.class));
        startService(new Intent(this, ServiceScreen.class));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        ClearResourcesFromDB();

        UpdateResourcesInDB();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MainFrag());
            fragmentTransaction.commit();

            RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_home);
            RL.setBackgroundResource(R.color.colorBottomMenuActiveIcon);

        }
    }


    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void handleClickHex(View view){
        switch (view.getId()){
            case R.id.hex_sleep:
                if (findViewById(R.id.hex_btn_add_subbmission).getVisibility() == View.GONE){
                    findViewById(R.id.hex_btn_add_subbmission).setVisibility(View.VISIBLE);
                    findViewById(R.id.hex_btn_add_strategy).setVisibility(View.VISIBLE);
                    findViewById(R.id.hex_image).animate().alpha(0.5f).setDuration(1000);
                }
                else{
                    findViewById(R.id.hex_btn_add_subbmission).setVisibility(View.GONE);
                    findViewById(R.id.hex_btn_add_strategy).setVisibility(View.GONE);
                    findViewById(R.id.hex_image).animate().alpha(1).setDuration(1000);
                }
                break;
            case R.id.hex_network:
                Toast.makeText(this,"Network Clicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /*public void askIfNewSubmission(final Fragment fragment, final int color ){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("What do you want to do?");
        alertDialogBuilder.setPositiveButton("Fill out a new submission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                changeFragment(fragment);
                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                myToolbar.setBackgroundResource(color);
            }

        });

        alertDialogBuilder.setNegativeButton("Set a new strategy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: add review submissions
                changeFragment(new StrategyFragment());
                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                myToolbar.setBackgroundResource(color);

            }
        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }*/

    public void makeMenusNotActive(){
        RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_home);
        RL.setBackgroundResource(R.color.colorBottomMenuNotActiveIcon);

        RL= (RelativeLayout) findViewById(R.id.buttom_menu_trend);
        RL.setBackgroundResource(R.color.colorBottomMenuNotActiveIcon);

        RL= (RelativeLayout) findViewById(R.id.buttom_menu_resources);
        RL.setBackgroundResource(R.color.colorBottomMenuNotActiveIcon);

        RL= (RelativeLayout) findViewById(R.id.buttom_menu_more);
        RL.setBackgroundResource(R.color.colorBottomMenuNotActiveIcon);
    }

    public void handleClickMenuMain(View view){
        changeFragment(new MainFrag());
        makeMenusNotActive();
        RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_home);
        RL.setBackgroundResource(R.color.colorBottomMenuActiveIcon);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundResource(R.color.colorPrimary);
    }

    public void handleClickMenuTrend(View view){
        changeFragment(new ShowTrendFrag());
        makeMenusNotActive();
        RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_trend);
        RL.setBackgroundResource(R.color.colorBottomMenuActiveIcon);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundResource(R.color.colorPrimary);
    }

    public void handleClickMenuResources(View view){
        changeFragment(new ResourcesFrag());
        makeMenusNotActive();
        RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_resources);
        RL.setBackgroundResource(R.color.colorBottomMenuActiveIcon);
    }

    public void handleClickMenuMore(View view){
        changeFragment(new SettingsFrag());
        makeMenusNotActive();
        RelativeLayout RL= (RelativeLayout) findViewById(R.id.buttom_menu_more);
        RL.setBackgroundResource(R.color.colorBottomMenuActiveIcon);
    }

    public void handleClickSettingsClearHistory(View view){
        Realm realm = Realm.getDefaultInstance();

        //obtain the results of a query
        final RealmResults<SleepData> results = realm.where(SleepData.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    public void handleClickSettingsAddFakeData(View view){
        int[] hours={5,4,5,6,5,7,8,4,3,3};
        int[] moods={6,6,7,6,5,9,9,2,2,1};
        Realm realm = Realm.getDefaultInstance();

        for (int day=0;day<10;day++) {
            realm.beginTransaction();
            SleepData sleepData = realm.createObject(SleepData.class); // Create a new object
            sleepData.setYear(2016);
            sleepData.setMonth(10);
            sleepData.setDay(day);
            sleepData.setTimeOfSleep("10:20pm");
            if (day < 8)
                sleepData.setNeedSuggestions(true);
            else
                sleepData.setNeedSuggestions(false);

            if (day < 5)
                sleepData.setSleepiness("Somewhat");
            else if (day < 8)
                sleepData.setSleepiness("Very");
            else
                sleepData.setSleepiness("Not at all");


            sleepData.setHoursOfSleep(hours[day]);
            realm.commitTransaction();
            realm.beginTransaction();
            MoodData moodData = realm.createObject(MoodData.class); // Create a new object
            moodData.setYear(2016);
            moodData.setMonth(10);
            moodData.setDay(day);
            moodData.setSatisfaction(moods[day]);
            realm.commitTransaction();

        }



        realm.close();

    }

    public void handleClickSettingsEditPersonalInventory(View view){
        changeFragment(new PersonalInventoryFrag());
    }

    public void ClearResourcesFromDB(){
        Realm realm = Realm.getDefaultInstance();

        //obtain the results of a query
        final RealmResults<Resource> results = realm.where(Resource.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
        realm.close();
    }



    public void UpdateResourcesInDB(){
        Resources res = getResources();
        String[] titles = res.getStringArray(R.array.title_array);
        String[] contents = res.getStringArray(R.array.content_array);
        String[] keywords = res.getStringArray(R.array.keywords_array);
        String[] criteria = res.getStringArray(R.array.criteria_array);
        String[] locationS = res.getStringArray(R.array.location_array);
        String[] calendarDates = res.getStringArray(R.array.date_array);
        String[] locationNames=new String[calendarDates.length];

        List<Location> location=new ArrayList<Location>();
        int i=0;
        for (String string:locationS) {
            if (!string.equals("")) {
                Location loc = new Location("Dummy");
                loc.setLatitude(Double.parseDouble(string.split(":")[0]));
                loc.setLongitude(Double.parseDouble(string.split(":")[1]));
                location.add(loc);
                locationNames[i]=string.split(":")[2];
            }
            else {
                location.add(null);
                locationNames[i]="None";
            }
            i++;
        }
        for (i = 0; i < titles.length; i++) {
            save_into_database("sleep", titles[i], contents[i] , keywords[i],criteria[i],location.get(i),calendarDates[i],locationNames[i]);
        }
    }

    private void save_into_database(
            final String component,
            final String title,
            final String content,
            final String keywords, final String criteria, final Location location,
            final String calendarDate, final String locationName){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Resource sleepResource = bgRealm.createObject(Resource.class);
                sleepResource.setComponent(component);
                sleepResource.setTitle(title);
                sleepResource.setContent(content);
                sleepResource.setKeywords(keywords);
                sleepResource.setCriteria(criteria);
                if (location!=null) {
                    sleepResource.setLocationLat(location.getLatitude());
                    sleepResource.setLocationLog(location.getLongitude());
                }
                else
                {
                    sleepResource.setLocationLat(0.0);
                    sleepResource.setLocationLog(0.0);
                }
                sleepResource.setCalendarDate(calendarDate);
                sleepResource.setLocationName(locationName);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //adapter.notifyDataSetChanged();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e(TAG,error.getMessage());
            }
        });
    }

    public void handleClickToggleMapOrCalendar(View view){
        findViewById(R.id.ll_res_menu_holder).getLayoutParams().height=getPx(15);

        if (view.getId()==R.id.map_btn)
            findViewById(R.id.map_holder).setVisibility(View.VISIBLE);
        else if (view.getId()==R.id.calendar_btn)
            findViewById(R.id.calendar_holder).setVisibility(View.VISIBLE);

        findViewById(R.id.buttom_menu_res_collapse).setVisibility(View.VISIBLE);

        findViewById(R.id.buttom_menu_res_map_holder).setVisibility(View.GONE);
        findViewById(R.id.buttom_menu_res_calendar_holder).setVisibility(View.GONE);
    }



    public void handleClickCollapse(View view){
        findViewById(R.id.ll_res_menu_holder).getLayoutParams().height=getPx(40);

        findViewById(R.id.map_holder).setVisibility(View.GONE);
        findViewById(R.id.calendar_holder).setVisibility(View.GONE);

        findViewById(R.id.buttom_menu_res_collapse).setVisibility(View.GONE);

        findViewById(R.id.buttom_menu_res_map_holder).setVisibility(View.VISIBLE);
        findViewById(R.id.buttom_menu_res_calendar_holder).setVisibility(View.VISIBLE);

    }


    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    String findTrend(int[] data){

        double xbar=0;
        double ybar=0;
        double x2bar=0;
        double y2bar=0;
        double xybar=0;
        int x=0;
        for (int y:data){
            x++;

            xybar+=x*y;
            xbar+=x;
            ybar+=y;
            x2bar+=x*x;
            y2bar+=y*y;
        }
        xybar=xybar/x;
        xbar=xbar/x;
        ybar=ybar/x;
        x2bar=x2bar/x;
        y2bar=y2bar/x;
        double r=(xybar-(xbar*ybar))/sqrt((x2bar-(xbar*xbar))*(y2bar-(ybar*ybar)));
        //double r2=r*r;
        if (abs(r)>0.3)//Linear
        {
            double slopeTreshold=0.1;
            double slope=(xybar-(xbar*ybar))/(x2bar-(xbar*xbar));
            if (slope>slopeTreshold)
                return "Increasing";
            else if (slope<-slopeTreshold)
                return "Decreasing";
            else
                return "Steady";
        }

        double avgFirstSet=0;
        double avgSecondSet=0;
        int i;
        for (i=0;i<(data.length/2);i++){
            avgFirstSet+=data[i];
        }
        avgFirstSet=avgFirstSet/i;

        int j=0;
        for (;i<data.length;i++){
            avgSecondSet+=data[i];
            j++;
        }
        avgSecondSet=avgSecondSet/j;

        double difference=abs(avgFirstSet/avgSecondSet);
        if (difference>1.5)
        {
            return "Sudden";
        }
        return "Floating";
    }
}
