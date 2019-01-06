package com.journaldev.recyclerviewmultipleviewtype;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Owlia on 11/28/2016.
 */

public class MapFragment extends com.google.android.gms.maps.MapFragment {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity mainActivity=(MainActivity) getActivity();

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Resource> realmResults = realm.where(Resource.class).equalTo("title",mainActivity.resourceToShowTitle).findAll();

        Resource resource=realmResults.get(0);

        if (resource.getLocationLat()!=0.0) {
            Location loc = new Location("dummyprovider");
            loc.setLatitude(resource.getLocationLat());
            loc.setLongitude(resource.getLocationLog());
            changeCamera(loc);
        }
    }



    public void changeCamera(final Location location ) {
        LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                CameraPosition position = CameraPosition.builder()
                        .target( new LatLng( location.getLatitude(),
                                location.getLongitude() ) )
                        .zoom( 16f )
                        .bearing( 0.0f )
                        .tilt( 0.0f )
                        .build();

                googleMap.animateCamera( CameraUpdateFactory
                        .newCameraPosition( position ), null );

                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //googleMap.setMyLocationEnabled( false );
                googleMap.getUiSettings().setZoomControlsEnabled( false );
            }
        });



    }

}