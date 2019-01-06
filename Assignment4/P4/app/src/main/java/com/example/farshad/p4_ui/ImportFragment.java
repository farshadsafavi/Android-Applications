package com.example.farshad.p4_ui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.farshad.p4_ui.model.Photo;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImportFragment extends Fragment {
    private Realm realm;
    public ArrayList<String> allFileUris = new ArrayList<String>();
    public ArrayList<Integer> all_x = new ArrayList<Integer>();
    public ArrayList<Integer> all_y = new ArrayList<Integer>();
    public ArrayList<Integer> all_width = new ArrayList<Integer>();
    public ArrayList<Integer> all_height = new ArrayList<Integer>();
    public ArrayList<String> all_paths = new ArrayList<String>();
    public ArrayList<String> all_paths_query = new ArrayList<String>();
    TextView textView;
    public String path = Environment.getExternalStorageDirectory() + "/DCIM";

    // Permissions
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA_EXST = 0x5;
    View rootView;
    int coresWorking;
    public ImportFragment() {
        // Required empty public constructor
    }

    AlertDialog levelDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.import_main, container, false);
        // Ask for permission
        askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        askForPermission(android.Manifest.permission.CAMERA, CAMERA_EXST);

//        textView_log = (TextView) findViewById(R.id.textView_log);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete all data from realm", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //        refresh_views();
                deleteAllNodes();
                List_Arrays list_arrays = new List_Arrays();
                list_arrays.all_new_picture_paths.clear();

                //        refresh_views();
            }
        });

        FloatingActionButton fab_2 = (FloatingActionButton) rootView.findViewById(R.id.thread_options);
        fab_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Choose Number of Threads", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {" 1 "," 2 "," 4 "," 8 "};
                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Number of Pool Threads");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch(item)
                        {
                            case 0:
                                // Your code when first option seletced
                                coresToUse = 1;
                                textView = (TextView) getActivity().findViewById(R.id.Thread_text);
                                textView.setText("Number Of Thread To Upload Images: " + coresToUse);
                                break;
                            case 1:
                                // Your code when 2nd  option seletced
                                coresToUse = 2;
                                textView = (TextView) getActivity().findViewById(R.id.Thread_text);
                                textView.setText("Number Of Thread To Upload Images: " + coresToUse);
                                break;
                            case 2:
                                // Your code when 3rd option seletced
                                coresToUse = 4;
                                textView = (TextView) getActivity().findViewById(R.id.Thread_text);
                                textView.setText("Number Of Thread To Upload Images: " + coresToUse);
                                break;
                            case 3:
                                // Your code when 4th  option seletced
                                coresToUse = 8;
                                textView = (TextView) getActivity().findViewById(R.id.Thread_text);
                                textView.setText("Number Of Thread To Upload Images: " + coresToUse);
                                break;
                            default:
                                coresToUse = 1;
                                textView = (TextView) getActivity().findViewById(R.id.Thread_text);
                                textView.setText("Number Of Thread To Upload Images: " + coresToUse);
                                break;

                        }
                      //  levelDialog.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button
                    }
                }).show();

            }
        });


        realm = Realm.getDefaultInstance();

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.import_icon);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Some validation for stuff.
                List_Arrays list_arrays = new List_Arrays();
                refresh_views();
                compare_gallery_with_database();
                Log.v("ImportFragment", "" + list_arrays.all_new_picture_paths.size() );
                if (list_arrays.all_new_picture_paths.size() != 0) {
                    UploadTask uploadTask = new UploadTask();
                    uploadTask.execute();
                }

            }
        });
        return rootView;
    }

    private void refresh_views() {
        int count = 0;
        List_Arrays list = new List_Arrays();
        list.all_face_paths.clear();
        all_paths_query.clear();
        // read from DB > show in textView_log
        RealmResults<Photo> photoRealmResults = realm.where(Photo.class).findAll();
//        Toast.makeText(getActivity(), "Restore " + photoRealmResults.size() + " image paths.", Toast.LENGTH_SHORT).show();
        String output = "";
        for (Photo photo : photoRealmResults) {
            output += photo.toString();
            all_paths_query.add(photo.getPath());
            if (photo.getFace_x() != 0 && photo.getFace_y() != 0 && photo.getHeight() != 0 && photo.getHeight() != 0) {
                list.all_face_paths.add(photo.getPath());
                Log.v("FarshadSafavi666", "" + count++);
                Log.v("FarshadSafavi666", "" + photo.getPath());
            }

        }

        Log.v("FarshadSafavi666", output);
//        textView_log.setText(output);
    }

    private void save_into_database(final String path,
                                    final int face_x,
                                    final int face_y,
                                    final int width,
                                    final int height) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Photo photo = bgRealm.createObject(Photo.class);
                photo.setPath(path);
                photo.setFace_x(face_x);
                photo.setFace_y(face_y);
                photo.setHeight(height);
                photo.setWidth(width);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v("database", ">>>>>>>> stored ok <<<<<<<<");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("database", error.getMessage());
            }
        });
    }


    private void deleteAllNodes() {
        // obtain the results of a query
        final RealmResults<Photo> results = realm.where(Photo.class).findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                results.deleteAllFromRealm();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    // Prepare data for gridview
    private void storePaths() {
//        final ArrayList<String> allFileUris= new ArrayList<>();
        File file = new File(path);
        allFileUris.clear();
        if (file.isDirectory()) {
            listFile = file.listFiles();
            if (listFile != null && listFile.length > 0)

                Log.v(getString(R.string.app_name), "listFile.length: " + listFile.length);
            for (File child : file.listFiles()) {
                if (child.getName().toString().contains("jpg")) {
                    allFileUris.add(child.getAbsolutePath());
                    Log.v(getString(R.string.app_name), child.getAbsolutePath());
                }
            }
        }

    }

    //---------------------------------------------------------------------------
    // Permission methods
    //---------------------------------------------------------------------------
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
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
        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            //     Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            //       Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    int total = 0;
    int count = 0;
    File[] listFile;
    File file = new File(path);
    List_Arrays list_arrays = new List_Arrays();
    Bitmap tempBitmap;
    Bitmap myBitmap;
    int coresToUse = 1;
    Runnable r;
    boolean for_loop_finished = false;
    class UploadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog PR;

        @Override
        protected void onPreExecute() {
            listFile = file.listFiles();
            PR = new ProgressDialog(getActivity());
            PR.setTitle("Upload in Progress ...");
            PR.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            PR.setMax(list_arrays.all_new_picture_paths.size());
            PR.setProgress(0);
            PR.show();
            PR.setCanceledOnTouchOutside(false);
        }


        @Override
        protected String doInBackground(String... params) {
//            int file_length = 0;
//            String path = params[0];

            allFileUris.clear();
            coresWorking=0;
            ExecutorService executor = Executors.newFixedThreadPool(coresToUse);
            Log.v("FarshadJan", "" + coresToUse);
            if (list_arrays.all_new_picture_paths.size() > 0)
                Log.v(getString(R.string.app_name), "listFile.length: " + list_arrays.all_new_picture_paths.size());
            for (String child : list_arrays.all_new_picture_paths) {
                if (isCancelled())
                    break;
                if (child.contains("jpg")) {
                    allFileUris.add(child);



                    while (coresWorking>=coresToUse){};
                    coresWorking++;
                    executor.execute(new Image_detection(child));
                    total++;
//                    int progress = (int) (total * 100 / listFile.length);
                    publishProgress(total);
                    while (coresWorking!=0){};
                }
   //             Log.v("Farshad111", "" + all_paths.size());
            }
            executor.shutdown();
            return "Upload Complete ...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (!isCancelled()) {
//                TextView textView = (TextView) getActivity().findViewById(R.id.Thread_text);
//                textView.setText(String.valueOf(values[0].intValue()) + "/" + listFile.length + " Images Processed");

                PR.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            PR.hide();
            Log.v("Farshad111", "" + all_paths.size());
            for (int i = 0; i < all_paths.size(); i++) {
                String uri = all_paths.get(i).toString();
                Log.v("FarshadSafavi1", uri);
                save_into_database(uri, all_x.get(i), all_y.get(i), all_width.get(i), all_width.get(i));
            }
            getActivity().onBackPressed();
        }
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                             int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height
                        / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    private void compare_gallery_with_database() {
        List_Arrays list_arrays = new List_Arrays();

        Set<String> hs = new HashSet<>();
        hs.addAll(all_paths_query);
        all_paths_query.clear();
        all_paths_query.addAll(hs);

        storePaths();

//        Toast.makeText(getActivity(), "all database " + all_paths_query.size() + " image paths.", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "all Gallery " + allFileUris.size() + " image paths.", Toast.LENGTH_SHORT).show();

        Collection<String> list_allFileUris = allFileUris;
        Collection<String> list_all_paths_query = all_paths_query;


        List<String> sourceList = new ArrayList<String>(list_allFileUris);
        List<String> destinationList = new ArrayList<String>(list_all_paths_query);

        sourceList.removeAll(list_all_paths_query);
        destinationList.removeAll(list_allFileUris);

        list_arrays.all_new_picture_paths = new ArrayList<String>(sourceList);

        System.out.println("sourceList remove all database files: " + sourceList);
        System.out.println("destinationList remove all gallery files: " + destinationList);
    }
    int count_cores = 0;

    public class Image_detection implements Runnable {
        String child;
        public Image_detection(String child){
            this.child = child;
        }

        @Override
        public void run (){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            myBitmap = decodeSampledBitmapFromUri(child, 1280,
                    720);

            tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);


            FaceDetector faceDetector = new
                    FaceDetector.Builder(getActivity().getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if (!faceDetector.isOperational()) {
                //   new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detector!").show();
//                 Toast(rootView, "Could not set up the face detector");
            }

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Face> faces = faceDetector.detect(frame);

            if (faces.size() == 0) {
                all_x.add(0);
                all_y.add(0);
                all_width.add(0);
                all_height.add(0);
                all_paths.add(child);
            } else {
                Log.v("FarshadSafavi:", "Number of faces:" + faces.size());
                for (int i = 0; i < faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();
                    Log.v("FarshadSafavi:", "The face positions: " + x1 + "; " + y1 + "; " + thisFace.getWidth() + "; " + thisFace.getHeight());

                    all_x.add((int) x1);
                    all_y.add((int) y1);
                    all_width.add((int) thisFace.getWidth());
                    all_height.add((int) thisFace.getHeight());
                    all_paths.add(child);
                }

//                myBitmap.recycle();
//                myBitmap = null;

//                Log.d("ImportFragment", Thread.currentThread().getName()+ " Finish");

                //publishProgress(Double.valueOf(count));
            }
//            Log.d("ImportFragment", Thread.currentThread().getName()+ " Finish");
            coresWorking--;
            faceDetector.release();
        }
    }


}
