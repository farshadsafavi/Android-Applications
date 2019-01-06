package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SleepFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_sleep, container, false);

        final MainActivity mainActivity=(MainActivity) getActivity();

        SeekBar seekBar=(SeekBar) rootView.findViewById(R.id.sleep_seekbar);
        seekBar.setProgress(1);
        seekBar.setScrollBarSize(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView textView = (TextView) rootView.findViewById(R.id.text_view_sleepiness);
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        seekBar.setThumb(ContextCompat.getDrawable(getActivity(), R.drawable.sleep1));
                        textView.setText("Very");
                        break;
                    case 1:
                        seekBar.setThumb(ContextCompat.getDrawable(getActivity(), R.drawable.sleep2));
                        textView.setText("Somewhat");
                        break;
                    case 2:
                        seekBar.setThumb(ContextCompat.getDrawable(getActivity(), R.drawable.sleep3));
                        textView.setText("Not at all");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.btn_questions_done).setVisibility(View.VISIBLE);
                if (v.getAlpha()!=1)
                    rootView.findViewById(R.id.sleep_satisfied_no).setAlpha(0.3f);
                    rootView.findViewById(R.id.sleep_satisfied_yes).setAlpha(0.3f);
                    v.setAlpha(1);
            }
        };

        rootView.findViewById(R.id.sleep_satisfied_yes).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.sleep_satisfied_no).setOnClickListener(onClickListener);

        rootView.findViewById(R.id.btn_questions_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rootView.findViewById(R.id.sleep_satisfied_no).getAlpha()==1)
                    mainActivity.changeFragment(new DetailQsOfSleepFragment());
                else
                    mainActivity.changeFragment(new MainFrag());
            }
        });
        /*
        ArrayList<String> hours = new ArrayList<String>();
        int thisDay = 24;
        for (int i = 0; i < 15; i++) {
            hours.add(Integer.toString(i));
        }
        ArrayList<Model> list= new ArrayList<>();
        //list.add(new Model(Model.TEXT_TYPE,"My Sleep Record:",0, null));
        list.add(new Model(Model.TIME_PICKER,"1. What time did you go to bed last night?",R.id.timePicker, null));
        list.add(new Model(Model.SPINNER_TYPE,"2. How many hours do you think you slept?",R.id.timeToBed, new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hours)));
        list.add(new Model(Model.RADIO_BUTTON,"3. How sleepy are you today?",R.id.radio_group, null));
        list.add(new Model(Model.RADIO_BUTTON_REFLECT,"4. Reflect on your answers for the above, click on one of the below options that you would agree with? Note that adults should aim to sleep 7-8 hours a night. ",R.id.reflect_radio_group, null));
        list.add(new Model(Model.SAVE_TYPE,"save",R.id.save_info,null));




        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
*/

        return rootView;

    }

}
