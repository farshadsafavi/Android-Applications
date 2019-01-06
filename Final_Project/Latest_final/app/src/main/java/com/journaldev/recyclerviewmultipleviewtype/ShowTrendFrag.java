package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.DonutChart;
import com.numetriclabz.numandroidcharts.PieChartL;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Created by Owlia on 11/3/2016.
 */

public class ShowTrendFrag extends Fragment {

    Boolean[] trendComponentsToShow={false,false,false,false,false,true}; //0=Academics,1=Activity ...
    View rootView;
    MainActivity mainActivity=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.frag_trend, container, false);

        mainActivity =(MainActivity) getActivity();

        View.OnClickListener onClickListener= new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int a=0;
                switch (v.getId()){
                    case R.id.trend_choose_component_academics:
                        a=0;
                        break;
                    case R.id.trend_choose_component_activity:
                        a=1;
                        break;
                    case R.id.trend_choose_component_mood:
                        a=2;
                        break;
                    case R.id.trend_choose_component_network:
                        a=3;
                        break;
                    case R.id.trend_choose_component_food:
                        a=4;
                        break;
                    case R.id.trend_choose_component_sleep:
                        a=5;
                        break;
                }
                trendComponentsToShow[a]=!trendComponentsToShow[a];

                if (trendComponentsToShow[a])
                    v.animate().alpha(1).setDuration(1500);
                else
                    v.animate().alpha(0.3f).setDuration(1500);

                updateBarChart();
            }
        };

        rootView.findViewById(R.id.trend_choose_component_activity).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.trend_choose_component_academics).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.trend_choose_component_mood).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.trend_choose_component_network).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.trend_choose_component_food).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.trend_choose_component_sleep).setOnClickListener(onClickListener);

        /*rootView.findViewById(R.id.trend_choose_component_sleep).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivity.changeFragment(new DetailTrendFrag());
                return true;
            }
        });*/

        rootView.findViewById(R.id.trend_show_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(new DetailTrendFrag());
            }
        });



        updateBarChart();


        return rootView;
    }

    void updateBarChart()
    {
        Realm realm = Realm.getDefaultInstance();

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.line_chart);
        //if (trendComponentsToShow[5]){//Sleep
        final RealmResults<SleepData> resultsSleep = realm.where(SleepData.class).findAll();
        ArrayList<Entry> entriesSleep = new ArrayList<>();
        ArrayList<String> labelsSleep = new ArrayList<String>();
        int[] dataToAnalyze=new int[resultsSleep.size()];

        int i = 0;
        for (SleepData sleepData : resultsSleep) {
            entriesSleep.add(new Entry(sleepData.getHoursOfSleep(), i));
            labelsSleep.add(String.valueOf(sleepData.getMonth()) + "/" + String.valueOf(sleepData.getDay()));

            dataToAnalyze[i]=sleepData.getHoursOfSleep();
            i++;
        }

        TextView textView= (TextView) rootView.findViewById(R.id.trend_more);
        textView.setText("Trend: "+mainActivity.findTrend(dataToAnalyze));


        LineDataSet datasetSleep = new LineDataSet(entriesSleep, "Sleep (Hours)");

        LineData data = new LineData(labelsSleep, datasetSleep);
        datasetSleep.setColor(getResources().getColor(R.color.colorHexSleep));
        datasetSleep.setDrawCubic(true);
        datasetSleep.setDrawFilled(true);
        datasetSleep.setLineWidth(2);
        //}
        if (trendComponentsToShow[2]) {//Mood
            final RealmResults<MoodData> resultsMood = realm.where(MoodData.class).findAll();
            ArrayList<Entry> entriesMood = new ArrayList<>();
            ArrayList<String> labelsMood = new ArrayList<String>();

            i = 0;
            for (MoodData moodData : resultsMood) {
                entriesMood.add(new Entry(moodData.getSatisfaction(), i));
                labelsMood.add(String.valueOf(moodData.getMonth()) + "/" + String.valueOf(moodData.getDay()));
                i++;
            }
            LineDataSet datasetMood = new LineDataSet(entriesMood, "Mood (0-10)");

            datasetMood.setColor(getResources().getColor(R.color.colorHexFood));
            datasetMood.setDrawCubic(true);
            datasetMood.setDrawFilled(true);
            datasetMood.setLineWidth(2);
            data.addDataSet(datasetMood);
            //datasetMood.setColor(R.color.colorHexMood);
        }

        lineChart.setData(data);
        lineChart.animateY(1500);
    }


}
