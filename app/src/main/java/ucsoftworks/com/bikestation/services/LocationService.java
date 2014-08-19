package ucsoftworks.com.bikestation.services;

import android.location.Location;

/**
 * Created by Pasenchuk Victor on 07.08.14
 */
public interface LocationService {
    public void start();
    public Location getLocation();
    public void stop();
}
