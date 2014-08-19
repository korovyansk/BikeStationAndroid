package ucsoftworks.com.bikestation.utils;

import android.location.Location;

import java.util.Date;
import java.util.Random;

public class LocationUtils {

    public static Location produceRandomLocation(Location baseLocation, double k) {
        final Random random = new Random(System.currentTimeMillis());
        final Location location = new Location(baseLocation.getProvider());
        location.setTime(new Date().getTime());
        location.setLatitude(baseLocation.getLatitude() + 2 * k * (0.5 - random.nextDouble()));
        location.setLongitude(baseLocation.getLongitude() + 2 * k * (0.5 - random.nextDouble()));
        location.setAltitude(location.getAltitude());
        location.setSpeed(location.getSpeed());
        location.setBearing(location.bearingTo(baseLocation));
        location.setAccuracy(location.getAccuracy());
        return location;
    }

    public static Location calculateNextStepLocation(Location currentLocation, Location targetLocation, double currentSpeed) {
        // http://www.movable-type.co.uk/scripts/latlong.html
        final double currentDistance = currentLocation.distanceTo(targetLocation);
        if (currentSpeed >= currentDistance) {
            return targetLocation;
        }
        final double bearing = Math.toRadians(currentLocation.bearingTo(targetLocation));
        final double lat1 = Math.toRadians(currentLocation.getLatitude());
        final double lon1 = Math.toRadians(currentLocation.getLongitude());
        final double angularDistance = currentSpeed / 6371000.0;
        final double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) + Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearing));
        final double lon2 = lon1 + Math.atan2(Math.sin(bearing) * Math.sin(angularDistance) * Math.cos(lat1), Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2));
        final Location resultLocation = new Location(currentLocation);
        resultLocation.setLatitude(Math.toDegrees(lat2));
        resultLocation.setLongitude(Math.toDegrees(lon2));
        return resultLocation;
    }

    private LocationUtils() {

    }

}
