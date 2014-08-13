package ucsoftworks.com.bikestation.geolocation;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pasenchuk Victor on 07.08.14
 */
public class MockGeoLocator implements GeolocationService {


    private final Random random = new Random();
    private float distance = 0;
    private float time = 0;

    public MockGeoLocator() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onTimer();
            }
        }, 0, 999);

    }

    void onTimer() {
        float speed = random.nextFloat() * 5;
        distance += speed / 3_600.0;
        time += 1.0 / 3_600;
    }

    @Override
    public float getDistance() {
        return distance;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

}
