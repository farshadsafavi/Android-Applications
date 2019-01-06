package com.journaldev.recyclerviewmultipleviewtype.Strategy;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.journaldev.recyclerviewmultipleviewtype.R;

import java.util.ArrayList;

public class StrategyFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_strategy, container, false);
//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        getActivity().setActionBar(toolbar);

        ArrayList<StrategyModel> list= new ArrayList<>();
        list.add(new StrategyModel(StrategyModel.TEXT_TYPE,"Set The Strategy:",0, null));
        list.add(new StrategyModel(StrategyModel.IMAGE_TYPE,"1. Target bedtime tonight…", R.drawable.clock, null));
        list.add(new StrategyModel(StrategyModel.SPINNER_TYPE,"2. My sleep target tonight …", R.drawable.sleep, null));

        StrategyAdapter adapter = new StrategyAdapter(list,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        return rootView;

    }
}