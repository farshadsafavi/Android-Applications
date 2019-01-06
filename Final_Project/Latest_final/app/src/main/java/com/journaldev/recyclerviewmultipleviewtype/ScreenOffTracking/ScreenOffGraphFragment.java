package com.journaldev.recyclerviewmultipleviewtype.ScreenOffTracking;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.journaldev.recyclerviewmultipleviewtype.R;
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.DonutChart;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenOffGraphFragment extends Fragment {

    View rootView;
    int day;
    int month;
    int year;
    BarChart barChart;
    LineChart lineChart;

    public ScreenOffGraphFragment() {
        // Required empty public constructor
    }
    /**
     * Static factory method that takes an int parameter,
     * initializes the fragment's arguments, and returns the
     * new fragment to the client.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String strDate = getArguments().getString("date");
        String strTimeDiff = getArguments().getString("time_diff");
        int percent = getArguments().getInt("percent");
        int targetSleep = getArguments().getInt("targetSleep");
        day = getArguments().getInt("day");
        month = getArguments().getInt("month");
        year  = getArguments().getInt("year");
        rootView = inflater.inflate(R.layout.frag_screen_off_graph, container, false);
        updateBarChart();

        lineChart = (LineChart) rootView.findViewById(R.id.lineChart_id);
        lineChart.setVisibility(View.GONE);

        final Button Trend = (Button) rootView.findViewById(R.id.trend_id);
        Trend.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
//                ((SleepGameActivity)getActivity()).test_bench();
                barChart.setVisibility(View.GONE);
                updateLineChart();
//                updateBarChart();
            }
        });

        final Button Update = (Button) rootView.findViewById(R.id.update_id);
        Update.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
//                ((SleepGameActivity)getActivity()).test_bench();
                lineChart.setVisibility(View.GONE);
                updateBarChart();
//                updateBarChart();
            }
        });

        TextView TxtDate  = (TextView) rootView.findViewById(R.id.calander_text);
        TxtDate.setText(strDate);

        TextView TxtTimeDiff  = (TextView) rootView.findViewById(R.id.timeDiffTxt);
        TxtTimeDiff.setText(strTimeDiff);

        TextView TxtPercent  = (TextView) rootView.findViewById(R.id.percentText);
        TxtPercent.setText(percent + "%");
        TxtPercent.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                // obtain the results of a query
                ((SleepGameActivity)getActivity()).test_bench();
            }
        });

            TextView TxttargetSleep  = (TextView) rootView.findViewById(R.id.targetSleepTxt);
        TxttargetSleep.setText("Goal " + targetSleep + " hours");

        DonutChart donut = (DonutChart) rootView.findViewById(R.id.piegraph);
        ChartData values = new ChartData();
        values.setSectorValue(percent);

        donut.addSector(values);

        values = new ChartData();
        values.setSectorValue(100 - percent);
        donut.addSector(values);

        Button Clear  = (Button) rootView.findViewById(R.id.clear_id);
        Clear.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                clear_detail_data();
                getActivity().onBackPressed();

            }
        });


        return rootView;
    }

    void updateBarChart()
    {

        Realm realm = Realm.getDefaultInstance();

        final RealmResults<ScreenOffData> resultsScreenOffData= realm.where(ScreenOffData.class).findAll();

        barChart = (BarChart) rootView.findViewById(R.id.barChart_id);
        barChart.setVisibility(View.VISIBLE);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int i = 0;
        for (ScreenOffData screenOffData: resultsScreenOffData) {
            if (    screenOffData.getScreenOffDay() == day &&
                    screenOffData.getScreenOffMonth() == month &&
                    screenOffData.getScreenOffYear() == year) {
                entries.add(new BarEntry(screenOffData.screenOffDuration / 60000, i));
                labels.add(String.valueOf(screenOffData.getScreenOffhour()) + ":" + String.valueOf(screenOffData.getScreenOffMinute()));
                i++;
            }
        }

        BarDataSet dataset = new BarDataSet(entries, "Screen Off Duration in Minutes");
        BarData data_1 = new BarData(labels, dataset);
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        barChart.setData(data_1);
        barChart.animateY(1500);

    }

    void updateLineChart(){
        Realm realm = Realm.getDefaultInstance();
        lineChart = (LineChart) rootView.findViewById(R.id.lineChart_id);
        lineChart.setVisibility(View.VISIBLE);
        int i = 0;
        ArrayList<Entry> entriesTrend = new ArrayList<>();
        ArrayList<String> labelsTrend = new ArrayList<String>();
        final RealmResults<ScreenOffDataMax> resultsScreenDataMax= realm.where(ScreenOffDataMax.class).findAll();
        for (ScreenOffDataMax screenOffDataMax: resultsScreenDataMax) {
            int screenOffTime = (int) ((screenOffDataMax.screenOffDurationMax) / 60000);
            entriesTrend.add(new Entry(screenOffTime, i));
            labelsTrend.add(String.valueOf(screenOffDataMax.getScreenOffMonthMax() + 1) + "/" + String.valueOf(screenOffDataMax.getScreenOffDayMax()));
            i++;
        }

        LineDataSet dataset = new LineDataSet(entriesTrend, "Screen Off Trends");

        LineData data_1 = new LineData(labelsTrend, dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data_1);
        lineChart.animateY(1500);
    }

    private void clear_detail_data() {

        Realm realm = Realm.getDefaultInstance();

        //obtain the results of a query
        final RealmResults<ScreenOffData> results = realm.where(ScreenOffData.class).findAll();
        final RealmResults<ScreenOffDataMax> results_max = realm.where(ScreenOffDataMax.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (ScreenOffData screenOffData: results) {
                    if (screenOffData.getScreenOffDay() == day &&
                            screenOffData.getScreenOffMonth() == month &&
                            screenOffData.getScreenOffYear() == year) {
                        // remove a single object
                        screenOffData.deleteFromRealm();
                    }
                }
                for (ScreenOffDataMax screenOffDataMax: results_max) {
                    if (screenOffDataMax.getScreenOffDayMax() == day &&
                            screenOffDataMax.getScreenOffMonthMax() == month &&
                            screenOffDataMax.getScreenOffYearMax() == year) {
                        // remove a single object
                        screenOffDataMax.deleteFromRealm();
                    }
                }
            }
        });

    }

}
