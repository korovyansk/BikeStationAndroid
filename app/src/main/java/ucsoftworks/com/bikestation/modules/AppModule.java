package ucsoftworks.com.bikestation.modules;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.geolocation.GeoLocator;
import ucsoftworks.com.bikestation.geolocation.GeolocationService;
import ucsoftworks.com.bikestation.geolocation.MockGeoLocator;
import ucsoftworks.com.bikestation.main_screen.MainActivity;
import ucsoftworks.com.bikestation.main_screen.MainFragment;
import ucsoftworks.com.bikestation.web_service.BikeApi;
import ucsoftworks.com.bikestation.web_service.BikeServiceApi;

/**
 * Created by Pasenchuk Victor on 28.07.14 in IntelliJ Idea
 */

@Module(injects = {BikeApp.class, MainActivity.class, MainFragment.class, MockGeoLocator.class}, library = true)
public class AppModule {
    @Provides
    @Singleton
    BikeServiceApi provideBikeService() {
        return new BikeApi();
    }


    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    GeolocationService provideGeolocationService() {
        return new GeoLocator();
    }

}
