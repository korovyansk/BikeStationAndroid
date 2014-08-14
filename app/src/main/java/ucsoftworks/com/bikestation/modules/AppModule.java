package ucsoftworks.com.bikestation.modules;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.application.SharedPreferencesManager;
import ucsoftworks.com.bikestation.gcm.GcmIntentService;
import ucsoftworks.com.bikestation.geolocation.GPSTracker;
import ucsoftworks.com.bikestation.geolocation.GeolocationService;
import ucsoftworks.com.bikestation.main_screen.EndFragment;
import ucsoftworks.com.bikestation.main_screen.MainActivity;
import ucsoftworks.com.bikestation.main_screen.MainFragment;

/**
 * Created by Pasenchuk Victor on 28.07.14 in IntelliJ Idea
 */

@Module(injects = {BikeApp.class, MainActivity.class, MainFragment.class, EndFragment.class, GcmIntentService.class})
public class AppModule {

    private BikeApp bikeApp;

    public AppModule(BikeApp bikeApp) {
        this.bikeApp = bikeApp;
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Provides
    GeolocationService provideGeolocationService() {
        return new GPSTracker(bikeApp);
    }

    @Provides
    SharedPreferencesManager ProvideSharedPreferencesManager() {
        return new SharedPreferencesManager(bikeApp);
    }

}
