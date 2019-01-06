package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.PieChartL;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owlia on 11/15/2016.
 */

public class DetailTrendFrag extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frag_trend_detail, container, false);

        Realm realm = Realm.getDefaultInstance();


        LineChart lineChart = (LineChart) rootView.findViewById(R.id.line_chart);

        final RealmResults<SleepData> resultsSleep = realm.where(SleepData.class).findAll();
        ArrayList<Entry> entriesSleep = new ArrayList<>();
        ArrayList<String> labelsSleep = new ArrayList<String>();

        int i = 0;
        for (SleepData sleepData : resultsSleep) {
            entriesSleep.add(new Entry(sleepData.getHoursOfSleep(), i));
            labelsSleep.add(String.valueOf(sleepData.getMonth()) + "/" + String.valueOf(sleepData.getDay()));
            i++;
        }

        LineDataSet datasetSleep = new LineDataSet(entriesSleep, "Sleep (Hours)");

        LineData data = new LineData(labelsSleep, datasetSleep);
        datasetSleep.setColor(getResources().getColor(R.color.colorHexSleep));
        datasetSleep.setDrawCubic(true);
        datasetSleep.setDrawFilled(true);
        datasetSleep.setLineWidth(2);

        lineChart.setData(data);
        lineChart.animateX(1500);



        final RealmResults<SleepData> results2 = realm.where(SleepData.class).equalTo("needSuggestions",false).findAll();

        PieChartL pie = (PieChartL) rootView.findViewById(R.id.piegraph1);
        List<ChartData> values = new ArrayList<>();
        values.add(new ChartData("Yes", (float) results2.size()));
        values.add(new ChartData("No",(float)  resultsSleep.size()-results2.size()));
        pie.setData(values);

        final RealmResults<SleepData> resultsSleepinessVery = realm.where(SleepData.class).equalTo("sleepiness","Very").findAll();
        final RealmResults<SleepData> resultsSleepinessSome = realm.where(SleepData.class).equalTo("sleepiness","Somewhat").findAll();
        final RealmResults<SleepData> resultsSleepinessNot = realm.where(SleepData.class).equalTo("sleepiness","Not at all").findAll();

        pie = (PieChartL) rootView.findViewById(R.id.piegraph2);
        List<ChartData> values2 = new ArrayList<>();
        values2.add(new ChartData("Very", (float) resultsSleepinessVery.size()));
        values2.add(new ChartData("Somewhat", (float) resultsSleepinessSome.size()));
        values2.add(new ChartData("Not at all",(float)  resultsSleepinessNot.size()));
        pie.setData(values2);

        return rootView;
    }
}
