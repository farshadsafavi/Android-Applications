package com.example.farshad.assignment2;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterTitleFragment extends Fragment {

    ArrayList<String> nameArray = new ArrayList<>();
    int spinnerCurrPos=0;
    boolean newEntries;
    boolean newSaved;
    public EnterTitleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_enter_title, container, false);
//        newEntries = false;
//        passData(newEntries);
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i >=1900; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, years);
        Spinner YearSpinner = (Spinner) rootView.findViewById(R.id.yearSpinner);
        YearSpinner.setAdapter(adapter);

        Button buttonAdd = (Button) rootView.findViewById(R.id.AddPeopleToList);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed_title = (EditText) getActivity().findViewById(R.id.theTitle);
                EditText ed_actor = (EditText) getActivity().findViewById(R.id.theActor);
                Spinner ed_Spinner=(Spinner) getActivity().findViewById(R.id.yearSpinner);
                String stringTitle = ed_title.getText().toString();
                String stringActor = ed_actor.getText().toString();
                String stringYear = ed_Spinner.getSelectedItem().toString();
                if(isNumeric(stringTitle) || isNumeric(stringTitle)){
                    Toast.makeText(getActivity().getBaseContext(), "Please Enter Name not Number!", Toast.LENGTH_LONG).show();
                }
                else if (nameArray.contains(stringTitle)) {
                    Toast.makeText(getActivity().getBaseContext(), "Name Already Exist", Toast.LENGTH_LONG).show();
                } else if (stringTitle.trim().equals("") || stringActor.trim().equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "You missed something", Toast.LENGTH_LONG).show();
                } else {
                    new List();
                    List.TitleArray.add(stringTitle);
                    List.ActorArray.add(stringActor);
                    List.YearArray.add(stringYear);
                    newEntries = true;
                    newSaved = false;
                    passData(newEntries, newSaved);
                }
                ed_title.setText("");
                ed_actor.setText("");
            }
        });

        Button buttonDone = (Button) rootView.findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });
        return rootView;

    }

    OnDataPass dataPasser;

    // Container Activity must implement this interface
    public interface OnDataPass {
        public boolean onDataPass(boolean newEntries, boolean newSaved);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            dataPasser = (OnDataPass) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public boolean passData(boolean newEntries, boolean newSaved) {
        dataPasser.onDataPass(newEntries, newSaved);
//        Log.d("LOG","Salam " + newEntries);
        return newEntries;
    }

    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }


}
