package com.journaldev.recyclerviewmultipleviewtype.Strategy;


import android.widget.ArrayAdapter;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class StrategyModel {


    public static final int TEXT_TYPE=0;
    public static final int IMAGE_TYPE=1;
    public static final int SPINNER_TYPE=2;
    public static final int SAVE_TYPE =4;

    public int type;
    public int data;
    public String text;
    public ArrayAdapter<String> adapter;



    public StrategyModel(int type, String text, int data, ArrayAdapter<String> adapter)
    {
        this.type=type;
        this.text=text;
        this.data=data;
        this.adapter = adapter;

    }

}
