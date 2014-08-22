package ucsoftworks.com.bikestation.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import butterknife.OnClick;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.services.LocationService;
import ucsoftworks.com.bikestation.ui.base.BikeFragment;

public class CityMapFragment extends BikeFragment {

    @Inject
    LocationService locationService;

    private MapView mapView;
    private GoogleMap map;
    private boolean firstMove;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstMove = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) view.findViewById(R.id.city_map_mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setScrollGesturesEnabled(false);


        MapsInitializer.initialize(this.getActivity());
        if (locationService.getLocation() != null) {
            moveCameraToLocation(locationService.getLocation());
        }

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                moveCameraToLocation(location);
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressWarnings("UnusedDeclaration")
    @OnClick(R.id.city_map_back)
    /*injected*/ void onClickBack() {
        getActivity().getFragmentManager().popBackStack();
    }

    private void moveCameraToLocation(Location location) {
        if (firstMove) {
            map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()),
                            18.0f
                    )
            );
        } else {
            map.moveCamera(
                    CameraUpdateFactory.newLatLng(
                            new LatLng(location.getLatitude(), location.getLongitude())
                    )
            );
        }
    }

}
