package com.example.farshad.p4_ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.farshad.p4_ui.model.Photo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Permissions
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    private Realm realm;
    private ArrayList<String> allFileUris = new ArrayList<String>();
    private ArrayList<String> all_paths_query = new ArrayList<String>();
    boolean gallery = false;
    boolean upload = false;
    private String path = Environment.getExternalStorageDirectory() + "/DCIM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ask for permission
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        realm = Realm.getDefaultInstance();

//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        GalleryFragment f_detailFragment = new GalleryFragment();
//        fragmentTransaction.replace(android.R.id.content, f_detailFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        refresh_views();
        List_Arrays list = new List_Arrays();
        compare_galley_with_database();
        refresh_views();
        if (all_paths_query.size() != 0) {
            gallery = true;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            GalleryFragment f_galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_galleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            upload = true;
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            ImportFragment f_importFragment = new ImportFragment();
            fragmentTransaction.replace(R.id.content_frame, f_importFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            GalleryFragment f_galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_galleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery || gallery == true){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            GalleryFragment f_galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_galleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_faces){
            refresh_views();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FaceGalleryFragment f_faceGalleryFragment = new FaceGalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_faceGalleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if (id == R.id.nav_camera) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            GalleryFragment f_galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_galleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    int id;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            ImportFragment f_importFragment = new ImportFragment();
            fragmentTransaction.replace(R.id.content_frame, f_importFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
//            Intent i = new Intent(MainActivity.this, ImportActivity.class);
//            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            GalleryFragment f_galleryFragment = new GalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_galleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
//            Intent i = new Intent(MainActivity.this, GalleryActivity.class);
//            startActivity(i);
        } else if (id == R.id.nav_faces) {
            refresh_views();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            FaceGalleryFragment f_faceGalleryFragment = new FaceGalleryFragment();
            fragmentTransaction.replace(R.id.content_frame, f_faceGalleryFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
//            Intent i = new Intent(MainActivity.this, FaceGalleryActivity.class);
//            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private void refresh_views() {
        int count = 0;
        List_Arrays list = new List_Arrays();
        list.all_face_paths.clear();
        // read from DB > show in textView_log
        RealmResults<Photo> photoRealmResults = realm.where(Photo.class).findAll();
        Toast.makeText(MainActivity.this, "Restored " + photoRealmResults.size() + " face paths.", Toast.LENGTH_SHORT).show();
        String output = "";
        for (Photo photo: photoRealmResults){
            output += photo.toString();
            all_paths_query.add(photo.getPath());
            if (photo.getFace_x() != 0 && photo.getFace_y() != 0 && photo.getHeight() != 0 && photo.getHeight() != 0) {
                list.all_face_paths.add(photo.getPath());
                Log.v("FarshadSafavi666", "" + count++);
                Log.v("FarshadSafavi666", "" + photo.getPath() );
            }

        }

        Log.v("FarshadSafavi666", output);
//        textView_log.setText(output);
    }

    private void compare_galley_with_database()
    {
        List_Arrays list_arrays = new List_Arrays();

        Set<String> hs = new HashSet<>();
        hs.addAll(all_paths_query);
        all_paths_query.clear();
        all_paths_query.addAll(hs);

        storePaths();

//        Toast.makeText(MainActivity.this, "all database " + all_paths_query.size() + " image paths.", Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, "all Gallery " + allFileUris.size() + " image paths.", Toast.LENGTH_SHORT).show();

        Collection<String> list_allFileUris = allFileUris;
        Collection<String> list_all_paths_query = all_paths_query;


        List<String> sourceList = new ArrayList<String>(list_allFileUris);
        List<String> destinationList = new ArrayList<String>(list_all_paths_query);

        sourceList.removeAll( list_all_paths_query );
        destinationList.removeAll( list_allFileUris );

        list_arrays.all_new_picture_paths = new ArrayList<String>(sourceList);

        System.out.println( "sourceList remove all database files: " + sourceList );
        System.out.println( "destinationList remove all gallery files: " + destinationList );
    }
    File[] listFile;
    // Prepare data for gridview
    private void storePaths() {
//        final ArrayList<String> allFileUris= new ArrayList<>();
        File file= new File(path);
        allFileUris.clear();
        if (file.isDirectory()){
            listFile = file.listFiles();
            if(listFile != null && listFile.length > 0)

                Log.v(getString(R.string.app_name), "listFile.length: "+listFile.length);
            for (File child : file.listFiles()){
                if (child.getName().toString().contains("jpg")) {
                    allFileUris.add(child.getAbsolutePath());
                    Log.v(getString(R.string.app_name), child.getAbsolutePath());
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

}
