package com.example.farshad.assignment2;


import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFileFragment extends Fragment {
    String data;
    private static final String TAG = "StoreFile";
    String path = Environment.getExternalStorageDirectory().toString()+"/movies";
    static final Integer WRITE_EXST = 0x3;
    private boolean newSave;
    public StoreFileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        final View rootView = inflater.inflate(R.layout.activity_store_file, container, false);
//        newSave = false;
//        storePassData(newSave);
        Button button = (Button) rootView.findViewById(R.id.buttonSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) getActivity().findViewById(R.id.enterFileName);
                String message = editText.getText().toString();
                String filename = message + ".txt";

                if(isNumeric(message)){
                    Toast.makeText(getActivity().getBaseContext(), "Please Enter Name not Number!", Toast.LENGTH_LONG).show();
                }
                else if (filename.trim().equals(".txt")) {
                    Toast.makeText(getActivity().getBaseContext(), "You missed filename", Toast.LENGTH_LONG).show();
                } else {
                    new List();
                    String[] titles = List.TitleArray.toArray(new String[List.TitleArray.size()]);
                    String[] actors = List.ActorArray.toArray(new String[List.ActorArray.size()]);
                    String[] years = List.YearArray.toArray(new String[List.YearArray.size()]);
                    data = "";
                    for (int i = 0; i < titles.length; i++) {
                        data += "Movie name: " + List.TitleArray.get(i) +
                                ", The Actotr: " + List.ActorArray.get(i) +
                                "Year Of production: " + List.YearArray.get(i);
                    }
                    try {
                        File fOut = new File(path, filename);
                        fOut.getParentFile().mkdirs();
                        FileOutputStream fos = new FileOutputStream(fOut);
                        Writer w = new BufferedWriter(new OutputStreamWriter(fos));
                        try {
                            w.write(data);
                            w.flush();
                            fos.getFD().sync();
                        } finally {
                            w.close();
                        }
                        Toast.makeText(getActivity().getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
                        // returns path names for files and directory

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    newSave = true;
                    storePassData(newSave);
                }
            }
        });
        return rootView;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {
        //    Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isNumeric(String str)
    {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    OnStoreDataPass storeDataPasser;

    // Container Activity must implement this interface
    public interface OnStoreDataPass {
        public boolean onStoreDataPass(boolean newSave);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            storeDataPasser = (OnStoreDataPass) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public boolean storePassData(boolean newSave) {
        storeDataPasser.onStoreDataPass(newSave);
        Log.d("LOG","save " + newSave);
        return newSave;
    }

}
