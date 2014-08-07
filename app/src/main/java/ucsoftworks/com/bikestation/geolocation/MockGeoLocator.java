package ucsoftworks.com.bikestation.geolocation;

import com.google.common.eventbus.Subscribe;
import com.squareup.otto.Bus;

import java.util.Random;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.TimerEvent;

/**
 * Created by Pasenchuk Victor on 07.08.14
 */
public class MockGeoLocator implements GeolocationService {

    private final Random random = new Random();
    @Inject
    Bus bus;
    private float distance = 0, speed = 0, avgSpeed = 0, time = 0;

    public MockGeoLocator(BikeApp bikeApp) {
        bikeApp.inject(this);
        bus.register(this);
    }

    @Subscribe
    void onTimer(TimerEvent event) {
        speed = random.nextFloat() * 5;
        distance += speed / 3_600;
        time += 1 / 3_600;
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
