package com.kloosin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.kloosin.R;
import com.kloosin.activity.MainActivity;

public class TrackFragment extends Fragment {

    private View _view;
    private GoogleMap _googleMap    =   null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if ( null == _view )
            _view   =   inflater.inflate(R.layout.track_fragment_layout, container, false );

        initComponent();

        return _view;
    }

    private void initComponent() {
        Toolbar toolbar = (Toolbar) _view.findViewById(R.id.toolbar);
        toolbar.inflateMenu( R.menu.track_menu );
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                _googleMap  =   googleMap;
                _googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        LatLngBounds.Builder _mBound = LatLngBounds.builder().include(new LatLng(22.5726, 88.3639));
                        _googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds( _mBound.build(), 2 ));
                    }
                });

            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.track_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
