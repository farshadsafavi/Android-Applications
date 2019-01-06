package com.journaldev.recyclerviewmultipleviewtype;


import android.widget.ArrayAdapter;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class Model {


    public static final int TEXT_TYPE=0;
    public static final int TIME_PICKER=1;
    public static final int SPINNER_TYPE=2;
    public static final int RADIO_BUTTON =3;
    public static final int SAVE_TYPE =4;
    public static final int RADIO_BUTTON_REFLECT =5;

    public int type;
    public int data;
    public String text;
    public ArrayAdapter<String> adapter;



    public Model(int type, String text, int data, ArrayAdapter<String> adapter)
    {
        this.type=type;
        this.text=text;
        this.data=data;
        this.adapter = adapter;

    }

}
