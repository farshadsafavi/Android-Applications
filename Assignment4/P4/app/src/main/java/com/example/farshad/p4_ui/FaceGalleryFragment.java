package com.example.farshad.p4_ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceGalleryFragment extends Fragment {

    AsyncTaskLoadFaceFiles myAsyncTaskFaceLoadFiles;
    ImageAdapter myImageAdapter;;
    public FaceGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_main, container, false);


        final GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(getActivity());
        gridview.setAdapter(myImageAdapter);

        myAsyncTaskFaceLoadFiles = new AsyncTaskLoadFaceFiles(myImageAdapter);
        myAsyncTaskFaceLoadFiles.execute();

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

//            String path = (String) parent.getItemAtPosition(position) ;
//            Intent i = new Intent(getActivity(), DetailActivity.class);
//            i.putExtra("path", path);
//            startActivity(i);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            DetailFragment f_detailFragment = new DetailFragment();
            f_detailFragment.path = (String) parent.getItemAtPosition(position);
            fragmentTransaction.replace(android.R.id.content, f_detailFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
    };

    public class AsyncTaskLoadFaceFiles extends AsyncTask<Void, String, Void> {
        List_Arrays list = new List_Arrays();
        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFaceFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }
        ProgressDialog PR;
        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            //      String targetPath = ExternalStorageDirectoryPath + "/DCIM";
            //      targetDirector = new File(targetPath);
            myTaskAdapter.clear();
            PR = new ProgressDialog(getActivity());
            PR.setTitle("Face detection in Progress ...");
            PR.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            PR.show();
            PR.setCanceledOnTouchOutside(false);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Set<String> hs = new HashSet<>();
            hs.addAll(list.all_face_paths);
            list.all_face_paths.clear();
            list.all_face_paths.addAll(hs);
            //   File[] files = targetDirector.listFiles();
            for (int i = 0; i < list.all_face_paths.size(); i ++) {
                publishProgress(list.all_face_paths.get(i));
            }
            //       if (isCancelled()) break;
            //   }
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
