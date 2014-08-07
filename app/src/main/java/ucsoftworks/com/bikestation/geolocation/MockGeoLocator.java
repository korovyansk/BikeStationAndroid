package ucsoftworks.com.bikestation.geolocation;

import com.google.common.eventbus.Subscribe;
import com.squareup.otto.Bus;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.TimerEvent;

/**
 * Created by Pasenchuk Victor on 07.08.14
 */
public class MockGeoLocator implements GeolocationService {


    private final Random random = new Random();
    private float distance = 0, speed = 0, avgSpeed = 0, time = 0;

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
        speed = random.nextFloat() * 5;
        distance += speed / 3_600.0;
        time += 1.0 / 3_600;
        avgSpeed = distance / time;
    }

    @Override
    public float getDistance() {
        return distance;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAvgSpeed() {
        return avgSpeed;
    }
}
