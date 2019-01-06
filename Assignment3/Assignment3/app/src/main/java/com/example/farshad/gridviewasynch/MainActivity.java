package com.example.farshad.gridviewasynch;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class MainActivity extends AppCompatActivity {

    AsyncTaskLoadFiles myAsyncTaskLoadFiles;
    public ImageAdapter myImageAdapter;
    static int n = 0;

    // Permissions
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA_EXST = 0x5;
    static int TAKE_PICTURE = 5;
    // Firebase code for uploading image
    private ProgressDialog mProgressDialog;
    // creating an instance of Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    //creating a storage reference. Replace the below URL with your Firebase storage URL.

    // Paths
    StorageReference storageRef = storage.getReferenceFromUrl("gs://imagestorage-1b113.appspot.com");
    String FIREBASE_URL = "https://imagestorage-1b113.firebaseio.com/";
    String path = Environment.getExternalStorageDirectory()+ "/DCIM";
    static String timeStamp;
    File file;
    ArrayList<String> keys = new ArrayList<String>();
    ArrayList<String> Addresses = new ArrayList<String>();
    ArrayList<String> allFileNames = new ArrayList<String>();
    ArrayList<String> files_existed= new ArrayList<String>();

    // Anonymous Authentication
    private static final String TAG = "AnonymousAuth";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Ask for permission
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        askForPermission(Manifest.permission.CAMERA, CAMERA_EXST);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);


        final GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(this, R.layout.grid_item_layout);
        Log.v("FarshadSafavi: ", "ImageAdapter Size: " + myImageAdapter.getCount());
        gridview.setAdapter(myImageAdapter);

        //---------------------------------------------------------------------------
        // Authentication
        //---------------------------------------------------------------------------

        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();
        gridview.setOnItemClickListener(myOnItemClickListener);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // updateUI(user);
                // [END_EXCLUDE]
            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            //          Toast.makeText(MainActivity.this, "Authentication failed.",
                            //                  Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]

        //---------------------------------------------------------------------------
        // Reload Files
        //---------------------------------------------------------------------------
        Button buttonReload = (Button) findViewById(R.id.reload);
        buttonReload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Cancel the previous running task, if exist.
                myAsyncTaskLoadFiles.cancel(true);

                //new another ImageAdapter, to prevent the adapter have
                //mixed files
                myImageAdapter = new ImageAdapter(MainActivity.this, R.layout.grid_item_layout);
                Log.v("Farshad:", "" + myImageAdapter.getCount());
                gridview.setAdapter(myImageAdapter);
                myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
                myAsyncTaskLoadFiles.execute();
            }
        });

        // Create an instance of Firebase Storage
        Button ButtonPicture = (Button) findViewById(R.id.picture);
        ButtonPicture.setOnClickListener(new View.OnClickListener() {
            String path = Environment.getExternalStorageDirectory() + "/DCIM";

            @Override
            public void onClick(View v) {
                timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                file = new File(path, timeStamp + ".jpg");
                Uri uriSavedImage = Uri.fromFile(file);
                imageIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uriSavedImage);

                // start camera activity
                startActivityForResult(imageIntent, TAKE_PICTURE);
                // n++;

            }
        });

        //---------------------------------------------------------------------------
        // Restore Files
        //---------------------------------------------------------------------------
        Button ButtonRestore = (Button) findViewById(R.id.restore);
        ButtonRestore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Firebase refListName = new Firebase(FIREBASE_URL);
                refListName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            System.out.println(snapshot.getValue());
                       //     Toast.makeText(MainActivity.this, "Restore " + snapshot.getChildrenCount() + " images.", Toast.LENGTH_SHORT).show();
                            System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                            int i = 0;
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Addresses.add(postSnapshot.getValue().toString());
                                keys.add(postSnapshot.getKey().toString());
                            }
                        }
                        for (int i = 0; i < Addresses.size(); i++) {
                            String image_url = Addresses.get(i).toString();
                            downloadFile(Uri.fromFile(new File(image_url)));
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
                Toast.makeText(MainActivity.this, "wait to reload all images!", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Press Reload After some time!", Toast.LENGTH_SHORT).show();

            }


        });
    }

    //---------------------------------------------------------------------------
    // Delete File
    //---------------------------------------------------------------------------
    AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
        //String fileName;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
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
    // On Start
    //---------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //---------------------------------------------------------------------------
    // On Stop
    //---------------------------------------------------------------------------
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //---------------------------------------------------------------------------
    // On Photo Activity Result
    //---------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == TAKE_PICTURE) {
            uploadFile(Uri.fromFile(file));
            myImageAdapter.notifyDataSetChanged();
            myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
            myAsyncTaskLoadFiles.execute();

  //          Toast.makeText(MainActivity.this, "Press Reload after upload complete!", Toast.LENGTH_SHORT).show();
        }

    }


    //---------------------------------------------------------------------------
    // Permission methods
    //---------------------------------------------------------------------------
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //    Toast.makeText(getActivity(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
       //     Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
     //       Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    //---------------------------------------------------------------------------
    // Upload File
    //---------------------------------------------------------------------------
    // Upload a file to firebase
    private void uploadFile(Uri uri) {
        StorageReference myfileRef = storageRef.child(uri.getLastPathSegment());
        final com.google.firebase.storage.UploadTask uploadTask = myfileRef.putFile(uri);
        showHorizontalProgressDialog("Uploading", "Please wait...");
        uploadTask
                .addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                        hideProgressDialog();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String DOWNLOAD_URL = downloadUrl.getPath();
                        Firebase ref = new Firebase(FIREBASE_URL);
                        ref.child(timeStamp).setValue(DOWNLOAD_URL );
                        Log.v("MainActivity", downloadUrl.toString());
                    //    Toast.makeText(MainActivity.this, DOWNLOAD_URL , Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(MainActivity.this, "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();
                        n++;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        // Handle unsuccessful uploads
               //         Toast.makeText(MainActivity.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                })
                .addOnProgressListener(MainActivity.this, new OnProgressListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) (100 * (float) taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        Log.i("Progress", progress + "");
                        updateProgress(progress);
                    }
                });
    }

    //---------------------------------------------------------------------------
    // Download file
    //---------------------------------------------------------------------------
    private void downloadFile(Uri uri) {
        StorageReference storageReferenceImage = storageRef.child(uri.getLastPathSegment());
        String path = Environment.getExternalStorageDirectory()+ "/DCIM";
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MainActivity", "failed to create Firebase Storage directory");
            }
        }
        final File localFile = new File(mediaStorageDir, uri.getLastPathSegment());
        try {
            localFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //    showHorizontalProgressDialog("Downloading", "Please wait...");

        storageReferenceImage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                //hideProgressDialog();
                Glide.with(MainActivity.this)
                        .load(localFile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                hideProgressDialog();
                exception.printStackTrace();

            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                   // int progress = (int) (100 * (float) taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                   // Log.i("Progress", progress + "");
                   // updateProgress(progress);
            }
        });
    }

    //---------------------------------------------------------------------------
    // Reload Method
    //---------------------------------------------------------------------------
    private void reload(){

        //Cancel the previous running task, if exist.
        myAsyncTaskLoadFiles.cancel(true);

        //new another ImageAdapter, to prevent the adapter have
        //mixed files
        final GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(MainActivity.this, R.layout.grid_item_layout);
        Log.v("Farshad:", ""+ myImageAdapter.getCount());
        gridview.setAdapter(myImageAdapter);
        gridview.setOnItemClickListener(myOnItemClickListener);
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();
    }


    //---------------------------------------------------------------------------
    // Show Progress Dialog
    //---------------------------------------------------------------------------
    private void showProgressDialog(String title, String message) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.setMessage(message);
        else
            mProgressDialog = ProgressDialog.show(this, title, message, true, false);
    }

    public void showHorizontalProgressDialog(String title, String body) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(body);
        } else {
            mProgressDialog = new ProgressDialog(this);
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

    //---------------------------------------------------------------------------
    // Hide progress dialog
    //---------------------------------------------------------------------------
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //---------------------------------------------------------------------------
    // Show Alert
    //---------------------------------------------------------------------------
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

    //---------------------------------------------------------------------------
    // Update Progress
    //---------------------------------------------------------------------------
    public void updateProgress(int progress) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setProgress(progress);
        }
    }

    //---------------------------------------------------------------------------
    // Show Toast
    //---------------------------------------------------------------------------
    private void showToast(String message) {
      //  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getlistOfFiles(File directory) {
        if (directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                String file_name = child.getName();
                String[] separated_1 = file_name.split(".jpg");
                String image_name=  separated_1[0]; // this will contain "Fruit"
                Log.v("Files no jpg", separated_1[0]);
                Log.v("Files no jpg", image_name);
                allFileNames.add(image_name);
            }
        }
    }

    //---------------------------------------------------------------------------
    // Compare database with directory
    //---------------------------------------------------------------------------
    private ArrayList<String> compare_database_with_directory() {
        ArrayList<String> files_should_downloaded  = new ArrayList<String>();
        File mediaStorageDir = new File(path);
        getlistOfFiles(mediaStorageDir);

        files_should_downloaded = keys;
        for (String temp : keys) {
            Log.v("Farshad keys:", temp);
            if(allFileNames.contains(temp)) {
                files_existed.add(temp);
            }
        }
        files_should_downloaded.removeAll(files_existed);
        System.out.println("Salam" + files_should_downloaded);
        for (String temp : files_should_downloaded) {
            Log.v("Farshad:"," files should download: " + temp);
        }

        return files_should_downloaded;
    }

    //---------------------------------------------------------------------------
    // on Backpress
    //---------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        reload();
    }
}