package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owlia on 11/17/2016.
 */

public class ShowResourceFragment extends Fragment {
    View rootView;
    Resource resource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frag_show_resource, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();


        Realm realm = Realm.getDefaultInstance();

        RealmResults<Resource> realmResults = realm.where(Resource.class).equalTo("title", mainActivity.resourceToShowTitle).findAll();

        resource = realmResults.get(0);

        TextView textView = (TextView) rootView.findViewById(R.id.show_resource_title);
        textView.setText(resource.title);

        textView = (TextView) rootView.findViewById(R.id.show_resource_content);
        textView.setText(resource.content);

        textView = (TextView) rootView.findViewById(R.id.show_resource_keywords);
        textView.setText("Keywords: " + resource.keywords);

        textView = (TextView) rootView.findViewById(R.id.show_resource_criteria);
        textView.setText("Criteria: " + resource.criteria);

        if (resource.getLocationLat()==0){
            rootView.findViewById(R.id.buttom_menu_res_map).setAlpha(0.75f);
            rootView.findViewById(R.id.map_btn).setClickable(false);

        }

        else{
            rootView.findViewById(R.id.buttom_menu_res_map).setAlpha(0);
            rootView.findViewById(R.id.map_btn).setClickable(true);
        }



        if (!resource.getCalendarDate().equals(null) && !resource.getCalendarDate().equals("")) {
            rootView.findViewById(R.id.buttom_menu_res_calendar).setAlpha(0);


            textView = (TextView) rootView.findViewById(R.id.show_resource_calander_title);
            textView.setText(resource.locationName);

            int year = Integer.parseInt(resource.calendarDate.split("/")[0]);
            int month = Integer.parseInt(resource.calendarDate.split("/")[1]) - 1;
            int day = Integer.parseInt(resource.calendarDate.split("/")[2]);
            int hour = Integer.parseInt(resource.calendarDate.split("/")[3]);
            int minute = Integer.parseInt(resource.calendarDate.split("/")[4]);
            textView = (TextView) rootView.findViewById(R.id.show_resource_calander_date);
            textView.setText(year + "/" + (month + 1) + "/" + day + " " + hour + ":" + minute);


            Button imgCalander = (Button) rootView.findViewById(R.id.calendar_add_btn);
            imgCalander.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onAddEventClicked();
                }
            });
        }
        else
        {
            rootView.findViewById(R.id.buttom_menu_res_calendar).setAlpha(0.75f);
            rootView.findViewById(R.id.calendar_btn).setClickable(false);
        }
        return rootView;
    }

    public void onAddEventClicked() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");

        Calendar cal = Calendar.getInstance();


        intent.putExtra(CalendarContract.Events.TITLE, resource.title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, resource.locationName);

        int year = Integer.parseInt(resource.calendarDate.split("/")[0]);
        int month = Integer.parseInt(resource.calendarDate.split("/")[1]) - 1;
        int day = Integer.parseInt(resource.calendarDate.split("/")[2]);
        int hour = Integer.parseInt(resource.calendarDate.split("/")[3]);
        int minute = Integer.parseInt(resource.calendarDate.split("/")[4]);

        GregorianCalendar calDate = new GregorianCalendar(year, month, day, hour, minute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        startActivity(intent);
    }

}
