package ucsoftworks.com.bikestation.services.mock;

import android.location.Location;
import android.location.LocationManager;

import ucsoftworks.com.bikestation.helpers.UITimer;
import ucsoftworks.com.bikestation.services.LocationService;
import ucsoftworks.com.bikestation.utils.LocationUtils;

public class MockLocationService implements LocationService {

    private final UITimer timer;

    private Location srcLocation;
    private Location currentLocation;
    private Location dstLocation;

    public MockLocationService(UITimer timer) {
        this.timer = timer;
        Location omskLocation = produceOmskLocation();
        this.srcLocation = LocationUtils.produceRandomLocation(omskLocation, 0.1);
        this.currentLocation = srcLocation;
        this.dstLocation = LocationUtils.produceRandomLocation(srcLocation, 0.1);
    }

    @Override
    public void start() {
        timer.schedule(new UITimer.Task() {
            @Override
            public void run() {
                currentLocation = LocationUtils.calculateNextStepLocation(currentLocation, dstLocation, 15);
            }
        }, 0, 1000, "timer");
    }

    @Override
    public void stop() {
        timer.cancel("timer");
    }

    @Override
    public Location getLocation() {
        return currentLocation;
    }

    private Location produceOmskLocation() {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(55.0000000);
        location.setLongitude(73.4000000);
        location.setAccuracy(0);
        location.setBearing(90);
        location.setSpeed(15);
        return location;
    }

}
