package com.journaldev.recyclerviewmultipleviewtype;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.recyclerviewmultipleviewtype.Strategy.StrategyFragment;

/**
 * Created by Owlia on 10/24/2016.
 */

public class MainFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.frag_main, container, false);

        final MainActivity mainActivity=(MainActivity) getActivity();

        rootView.findViewById(R.id.hex_image).setAlpha(0);
        rootView.findViewById(R.id.hex_image).animate().rotation(1080).alpha(1).setDuration(1000);

        rootView.findViewById(R.id.hex_btn_add_subbmission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(new SleepFragment());
            }
        });


        rootView.findViewById(R.id.hex_btn_add_strategy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.changeFragment(new StrategyFragment());
            }
        });

        return rootView;
    }


}
