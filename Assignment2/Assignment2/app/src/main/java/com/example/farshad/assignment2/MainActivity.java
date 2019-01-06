package com.example.farshad.assignment2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements EnterTitleFragment.OnDataPass, StoreFileFragment.OnStoreDataPass {
    private boolean newEntriesMain = false;
    private boolean newSaveMain = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View Button
        Button viewButton = (Button) findViewById(R.id.View);
        viewButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent ViewIntent = new Intent(MainActivity.this, ViewList.class);
                startActivity(ViewIntent);
            }
        });


        // Exit Button
        Button exitButton = (Button) findViewById(R.id.Exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                if (newEntriesMain == true && newSaveMain == false) {
                    Toast.makeText(getBaseContext(), "New Entries Not Stored", Toast.LENGTH_LONG).show();
                } else {
                    onStoreDataPass(false);
                    onDataPass(false, false);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


    }

//    @Override
//    public void onBackPressed(){
//        FragmentManager fm = getFragmentManager();
//        fm.popBackStack();
//    }

    public void ChangeFragment(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (view == findViewById(R.id.Enter_Title)) {
            EnterTitleFragment f_enterTitles = new EnterTitleFragment();
            fragmentTransaction.replace(android.R.id.content, f_enterTitles);
            fragmentTransaction.addToBackStack(null);
        } else if (view == findViewById(R.id.Store)) {
            StoreFileFragment f_store = new StoreFileFragment();
            fragmentTransaction.replace(android.R.id.content, f_store);
            fragmentTransaction.addToBackStack(null);
        } else if (view == findViewById(R.id.Load)) {
            LoadListFragment f_load = new LoadListFragment();
            fragmentTransaction.replace(android.R.id.content, f_load);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onDataPass(boolean newEntries, boolean newSaved) {

        Log.d("LOG","hello " + newEntries);
        newEntriesMain = newEntries;
        newSaveMain = newSaved;
        return newEntries;
    }

    @Override
    public boolean onStoreDataPass(boolean newSave) {
        Log.d("LOG","hello " + newSave);
        newSaveMain = newSave;
        return newSave;
    }
}

