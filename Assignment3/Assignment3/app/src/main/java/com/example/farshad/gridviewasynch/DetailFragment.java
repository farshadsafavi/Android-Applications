package com.example.farshad.gridviewasynch;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ProgressDialog mProgressDialog;
    boolean deleted_flag = false;
    String FIREBASE_URL = "https://imagestorage-1b113.firebaseio.com/";
    StorageReference storageRef_1 = storage.getReferenceFromUrl("gs://imagestorage-1b113.appspot.com");
    public String path;
    private String paths;
    private String url_image_deleted_from_firebase;
    public DetailFragment() {
        // Required empty public constructor
   //     String path;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_activity, container, false);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
        imageView.setImageDrawable(Drawable.createFromPath(path));
        Button ButtonDelete=(Button)rootView.findViewById(R.id.delete);
      //  Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
        final String filename = getFileName(path);

        // Get all reference names
        Firebase refListName = new Firebase(FIREBASE_URL);
        refListName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {

                    System.out.println(snapshot.getValue());
                    paths = snapshot.getValue().toString();
                    // Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                    int i = 0;
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        //       Toast.makeText(DetailActivity.this, postSnapshot.getKey().toString() + "  " +filename, Toast.LENGTH_SHORT).show();
                        if(postSnapshot.getKey().toString().equals(filename)) {
                            url_image_deleted_from_firebase = postSnapshot.getValue().toString();
                        }
                        //       Toast.makeText(DetailActivity.this, url_image_deleted_from_firebase, Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        ButtonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // custom dialog
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Do you also wish to delete this photo from the cloud?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        File fdelete = new File(path);
                        fdelete.delete();
                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
                        imageView.setImageDrawable(Drawable.createFromPath(path));

                        // Remove the picture from storage
                        if (url_image_deleted_from_firebase != null){
                  //          Toast.makeText(getActivity(), url_image_deleted_from_firebase, Toast.LENGTH_SHORT).show();
                            File deleted_file = new File(url_image_deleted_from_firebase);
                            deleteFile(Uri.fromFile(deleted_file));
                        }

//                // Remove path in the database
                        Firebase ref = new Firebase(FIREBASE_URL);
                        ref.child(filename).removeValue();
                      //  Toast.makeText(getActivity(),"BackPress to Reload Gallery!",Toast.LENGTH_LONG).show();
                      //  hideProgressDialog();
                      //  showProgressDialog("Deleting", "Please wait...");
                        getActivity().onBackPressed();

                    }

                });

                alertDialogBuilder.setNegativeButton("No, Delete Local copy only",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
    //                    String path = getActivity().getIntent().getStringExtra("path");
////                int position_of_grid = getIntent().getIntExtra("position_of_grid", 0);
                        File fdelete = new File(path);
                        fdelete.delete();
                        ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
                        imageView.setImageDrawable(Drawable.createFromPath(path));
                        getActivity().onBackPressed();
                    }
                });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        return rootView;
    }

    private void deleteFile(Uri uri) {
      //  showProgressDialog("Deleting", "Please wait...");
        StorageReference storageReferenceImage = storageRef_1.child(uri.getLastPathSegment());

        storageReferenceImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getActivity(), "The file is deleted from firebase" , Toast.LENGTH_SHORT).show();
//                new CountDownTimer(3000, 1000) {
//                    int progress ;
//                    public void onTick(long millisUntilFinished) {
//                        progress = (int) (1/(millisUntilFinished / 1000));
//                        updateProgress(progress*100);
//                    }
//                    public void onFinish() {
//                        hideProgressDialog();
//                    }
//
//                }.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });


    }

    private String getFileName(String path)
    {
        File f = new File(path);
        Log.v("Files", f.getName());
        String file_name = f.getName();
        String[] separated_1 = file_name.split(".jpg");
        String image_name=  separated_1[0]; // this will contain "Fruit"
        Log.v("Files no jpg", separated_1[0]);
        Log.v("Files no jpg", image_name);
        return image_name;
    }

    public void open(View view){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure,You wanted to make decision");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
      //          Toast.makeText(getActivity(),"You clicked yes button",Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog(String title, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.setMessage(message);
        else
            mProgressDialog = ProgressDialog.show(getActivity(), title, message, true, false);
    }

    public void showHorizontalProgressDialog(String title, String body) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(body);
        } else {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(body);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    // Hide progress dialog
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    // Show Alert
    private void showAlertDialog(Context ctx, String title, String body, DialogInterface.OnClickListener okListener) {

        if (okListener == null) {
            okListener = new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            };
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(body).setPositiveButton("OK", okListener).setCancelable(false);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.show();
    }

    public void updateProgress(int progress) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setProgress(progress);
        }
    }

}
