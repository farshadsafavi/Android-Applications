package com.example.farshad.p4_ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    AsyncTaskLoadFiles myAsyncTaskLoadFiles;
    ImageAdapter myImageAdapter;
    GridView gridview;
    View rootView;
    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gallery_main, container, false);;


        gridview = (GridView) rootView.findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(myImageAdapter);

        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        gridview.setOnItemClickListener(myOnItemClickListener);
        return rootView;
    }

    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            String prompt = "remove " + (String) parent.getItemAtPosition(position);
//            Toast.makeText(getApplicationContext(), prompt, Toast.LENGTH_SHORT)
//                    .show();

//            myImageAdapter.remove(position);
//            myImageAdapter.notifyDataSetChanged();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            DetailFragment f_detailFragment = new DetailFragment();
            f_detailFragment.path = (String) parent.getItemAtPosition(position);
            fragmentTransaction.replace(android.R.id.content, f_detailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    //---------------------------------------------------------------------------
    // Reload Method
    //---------------------------------------------------------------------------
    public void reload(){

        //Cancel the previous running task, if exist.
        myAsyncTaskLoadFiles.cancel(true);

        //new another ImageAdapter, to prevent the adapter have
        //mixed files
        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(getActivity());
        Log.v("Farshad:", ""+ myImageAdapter.getCount());
        gridview.setAdapter(myImageAdapter);
        gridview.setOnItemClickListener(myOnItemClickListener);
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();
    }
    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }
        ProgressDialog PR;
        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            String targetPath = ExternalStorageDirectoryPath + "/DCIM";
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();
            myTaskAdapter.clear();
            PR = new ProgressDialog(getActivity());
            PR.setTitle("Gallery update in Progress ...");
            PR.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            PR.show();
            PR.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".JPG"))) {
                    publishProgress(file.getAbsolutePath());
                }
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            PR.hide();
            super.onPostExecute(result);
        }
    }


}
