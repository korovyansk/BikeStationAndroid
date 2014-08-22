package ucsoftworks.com.bikestation.events;

import android.location.Location;

public class LocationChangedEvent {
    private final Location location;

    public LocationChangedEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
