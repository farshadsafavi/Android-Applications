package com.example.farshad.p4_ui;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.farshad.p4_ui.model.Photo;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    ImageView imageView;
    Bitmap tempBitmap;
    Bitmap myBitmap;
    public String path;
    private Realm realm;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_activity, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.image);
        imageView.setImageDrawable(Drawable.createFromPath(path));

        FaceDetectionTask faceDetectionTask = new FaceDetectionTask();
        faceDetectionTask.execute();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Delete current image", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                List_Arrays list = new List_Arrays();
                deleteNode(path);
                File fdelete = new File(path);
//                for (int i = 0; i < list.all_face_paths.size(); i ++) {
//                    if (list.all_face_paths.get(i) == path){
//                        list.all_face_paths.remove(i);
//                    }
//                }
//                for (int i = 0; i < list.all_new_picture_paths.size(); i ++) {
//                    if (list.all_face_paths.get(i) == path){
//                        list.all_new_picture_paths.remove(i);
//                    }
//                }
                fdelete.delete();

                getActivity().onBackPressed();

            }
        });

        realm = Realm.getDefaultInstance();
        return rootView;
    }

    class FaceDetectionTask extends AsyncTask<String, Integer, String>
    {
        ProgressDialog PR;

        @Override
        protected void onPreExecute() {
            PR = new ProgressDialog(getActivity());
            PR.setTitle("Face detection in Progress ...");
            PR.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            PR.setMax(100);
            PR.setProgress(0);
            PR.show();
            PR.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String... params) {
//            int file_length = 0;
//            String path = params[0];
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
//            myBitmap = BitmapFactory.decodeFile(path, options);
            myBitmap = decodeSampledBitmapFromUri(path, 1280,
                    720);
            Paint myRectPaint = new Paint();
            myRectPaint.setStrokeWidth(5);
            myRectPaint.setColor(Color.RED);
            myRectPaint.setStyle(Paint.Style.STROKE);

            tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas tempCanvas = new Canvas(tempBitmap);
            tempCanvas.drawBitmap(myBitmap, 0, 0, null);

            FaceDetector faceDetector = new
                    FaceDetector.Builder(getActivity().getApplicationContext()).setTrackingEnabled(false)
                    .build();
            if(!faceDetector.isOperational()){
                //   new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detector!").show();
                return "Could not set up the face detector";
            }

            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Face> faces = faceDetector.detect(frame);
            int total = 0;
            int count = 0;
            for(int i=0; i<faces.size(); i++) {
                Face thisFace = faces.valueAt(i);
                float x1 = thisFace.getPosition().x;
                float y1 = thisFace.getPosition().y;
                float x2 = x1 + thisFace.getWidth();
                float y2 = y1 + thisFace.getHeight();
                tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
            }
            //  imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
            return "Face detection Complete ...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            PR.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            PR.hide();
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
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

    ArrayList<Integer> deleted_node_number = new ArrayList<Integer>();
    int delete_single_node;
    int j;
    private void deleteNode(String path) {
        // obtain the results of a query
        final RealmResults<Photo> results = realm.where(Photo.class).findAll();
        j = 0;
        for (int i = 0; i < results.size(); i++) {
            Photo photo = results.get(i);
            //   Log.v("farshadSafavidelete", "" + photo.getPath().toString());
            // ... do something with the object ...
//            Log.v("farshadSafavidelete", "" + photo.getPath().toString());
//            Log.v("farshadSafavidelete", "" + path);
            if (photo.getPath().toString().equals(path)) {
                deleted_node_number.add(i);

                //      Toast.makeText(getActivity().getApplicationContext(), "" + deleted_node_number, Toast.LENGTH_LONG).show();

            }

        }
        Log.v("farshadSafavidelete", "" + deleted_node_number.size());
        // All changes to data must happen in a transaction
        for (int i = 0; i < deleted_node_number.size(); i++){
            delete_single_node = deleted_node_number.get(i);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // Delete all matches
                    Log.v("farshadSafaviexecute", "" + delete_single_node);
                    Log.v("size", "" + deleted_node_number.size());
                    Photo photo_deleted = results.get(delete_single_node);
                    photo_deleted.deleteFromRealm();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
