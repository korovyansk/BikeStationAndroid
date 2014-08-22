package ucsoftworks.com.bikestation.services.real;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.otto.Bus;

import timber.log.Timber;
import ucsoftworks.com.bikestation.events.LocationChangedEvent;
import ucsoftworks.com.bikestation.services.LocationService;

public class RealLocationService implements LocationService, LocationListener {

    private final LocationClient locationClient;
    private final Bus bus;
    private Location location;

    public RealLocationService(Context context, final int updateIntervalMs, Bus bus) {
        this.bus = bus;
        this.locationClient = new LocationClient(context,
                new GooglePlayServicesClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Timber.d("onConnected to LocationClient");
                        final Location location = locationClient.getLastLocation();
                        Timber.d("retrieve last known %s", location);
                        if (location != null) {
                            updateLocationAndPost(location);
                        }
                        locationClient.requestLocationUpdates(buildLocationRequest(updateIntervalMs), RealLocationService.this);
                    }

                    @Override
                    public void onDisconnected() {
                        Timber.w("onDisconnected from LocationClient");
                    }
                },
                new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Timber.w("onConnectionFailed to LocationClient with connectionResult %s",
                                connectionResult);
                    }
                }
        );
    }

    @Override
    public void start() {
        if (!locationClient.isConnected() && !locationClient.isConnecting()) {
            Timber.d("connecting to LocationClient");
            locationClient.connect();
        } else {
            Timber.w("Called RealLocationService start method, when locationClient is already %s",
                    locationClient.isConnected() ? "connected" : "connecting");
        }
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void stop() {
        if (locationClient.isConnected() || locationClient.isConnecting()) {
            Timber.d("disconnecting from LocationClient");
            locationClient.disconnect();
        } else {
            Timber.w("Called RealLocationService stop method, when locationClient is not connected or connecting");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Timber.d("onLocationChanged %s", location);
        updateLocationAndPost(location);
    }

    private void updateLocationAndPost(Location location) {
        this.location = new Location(location);
        this.bus.post(new LocationChangedEvent(location));
    }

    private static LocationRequest buildLocationRequest(int updateIntervalMs) {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(updateIntervalMs)
                .setFastestInterval(1000)
                .setSmallestDisplacement(10);
    }
}