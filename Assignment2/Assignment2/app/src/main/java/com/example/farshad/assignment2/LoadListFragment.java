package com.example.farshad.assignment2;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Fragment;
import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadListFragment extends Fragment {
    String path = Environment.getExternalStorageDirectory().toString()+"/movies";
    static final Integer READ_EXST = 0x4;
    public LoadListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        final View rootView = inflater.inflate(R.layout.activity_load_list, container, false);

        ArrayList<String> listItems_load=new ArrayList<>();
        ArrayAdapter<String> adapter_load;
        ListView lv_load = (ListView) rootView.findViewById(R.id.listLoad);
        adapter_load=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,listItems_load);
        lv_load.setAdapter(adapter_load);

        File f = new File(path);
        File file[] = f.listFiles();
        Log.v("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
            Log.v("Files", "FileName:" + file[i].getName());
            String text = file[i].getName();
            listItems_load.add(text);
        }

        adapter_load.notifyDataSetChanged();

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
}
