package com.example.farshad.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        ArrayList<String> listItems=new ArrayList<>();
        ArrayAdapter<String> adapter;
        ListView lv = (ListView)findViewById(R.id.listView);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listItems);
        lv.setAdapter(adapter);
        new List();
        for(int i = 0; i < List.TitleArray.size(); i++){
            listItems.add("The Movie Title: " +List.TitleArray.get(i)
                    +"; The Actor: "+List.ActorArray.get(i)
                    +"\nYear Of Production: "+List.YearArray.get(i) + ".");
        }
        adapter.notifyDataSetChanged();
    }
}
