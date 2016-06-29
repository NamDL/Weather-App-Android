package com.example.getafix.myapplication;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.maps.interfaces.OnAerisMarkerInfoWindowClickListener;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.tiles.AerisTile;

/**
 * Created by Getafix on 12/8/2015.
 */
public class MapFragment extends MapViewFragment implements
        OnAerisMapLongClickListener, AerisCallback
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //AerisEngine.initWithKeys("9SDxCiYapye04J85VuYYr","0Moebz71dUuDQFCKrSEgBV8TTW9mtRCFJYYkRv22", "com.example.getafix.myapplication");
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);
        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        Location loc = new Location("dummyprovider");
        String lat= getArguments().getString("latitude");
        String lng=getArguments().getString("longitude");
        loc.setLatitude(Float.parseFloat(lat));
        loc.setLongitude(Float.parseFloat(lng));
        mapView.moveToLocation(loc,8);
        mapView.addLayer(AerisTile.RADSAT);
        mapView.setOnAerisMapLongClickListener(this);
        return view;
    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }
}
