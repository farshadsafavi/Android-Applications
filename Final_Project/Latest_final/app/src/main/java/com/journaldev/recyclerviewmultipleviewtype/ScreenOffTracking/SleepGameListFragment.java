package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.journaldev.recyclerviewmultipleviewtype.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepGameListFragment extends Fragment {
    Context mcontext;

    public SleepGameListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcontext = getActivity();

        View rootView = inflater.inflate(R.layout.frag_list_screenoff, container, false);
        ArrayList<Screenoff> screenoffs = QueryUtils.extractScreenoffs();

        // Create a new {@link ArrayAdapter} of earthquakes
        final ScreenoffAdapter itemAdapter = new ScreenoffAdapter(getActivity(), screenoffs);
//
        // Find a reference to the {@link ListView} in the layout
        ListView screenoffListView = (ListView) rootView.findViewById(R.id.list);

        // so the list can be populated in the user interface
        screenoffListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Screenoff currentScreenoffs = itemAdapter.getItem(position);
                Date dateObject = new Date(currentScreenoffs.getCurrentTime());
                String date = (formatDate(dateObject));

                String time_diff = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(currentScreenoffs.getScreenOffDuration()),
                        TimeUnit.MILLISECONDS.toMinutes(currentScreenoffs.getScreenOffDuration()) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(currentScreenoffs.getScreenOffDuration())),
                        TimeUnit.MILLISECONDS.toSeconds(currentScreenoffs.getScreenOffDuration()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentScreenoffs.getScreenOffDuration())));

                int minute_1 = (int) Math.floor(currentScreenoffs.getScreenOffDuration()/60000);
                int target = (int ) Math.ceil(((currentScreenoffs.getTargetSleep()*60) ));
                double percentInt = ((minute_1* 10)/target) ;
                int percent = (int) (percentInt*10);
                int targetSleep = (int) currentScreenoffs.getTargetSleep();
                int day = (int) currentScreenoffs.getScreenOffDay();
                int month = (int) currentScreenoffs.getScreenOffMonth();
                int year = (int) currentScreenoffs.getScreenOffYear();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
                Bundle args = new Bundle();
                args.putString("date", date);
                args.putString("time_diff", time_diff);
                args.putInt("percent", percent);
                args.putInt("targetSleep", targetSleep);
                args.putInt("day", day);
                args.putInt("month", month);
                args.putInt("year", year);
                ScreenOffGraphFragment frag = new ScreenOffGraphFragment();
                frag.setArguments(args);
                fragmentTransaction.replace(android.R.id.content, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//
//                String url = currentEarthquake.getUrl();
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setData(Uri.parse(url));
//                startActivity(i);
            }
        });
        screenoffListView.setAdapter(itemAdapter);
        return rootView;
    }

    private String formatDate(Date dateObject)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
        String dateToDisplay = dateFormatter.getDateInstance().format(dateObject);
        return dateToDisplay;
    }
    //
    private String formatTime(Date dateObject)
    {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormatter.format(dateObject);
        return timeToDisplay;
    }

}

