package ucsoftworks.com.bikestation.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ucsoftworks.com.bikestation.geolocation.GeolocationService;
import ucsoftworks.com.bikestation.geolocation.MockGeoLocator;
import ucsoftworks.com.bikestation.web_service.BikeServiceApi;
import ucsoftworks.com.bikestation.web_service.MockBikeApi;

/**
 * Created by Pasenchuk Victor on 28.07.14 in IntelliJ Idea
 */

@Module(
        complete = false,
        library = true,
        overrides = true
)
public class MockAppModule {
    @Provides
    @Singleton
    BikeServiceApi provideBikeService() {
        return new MockBikeApi();
    }

    @Provides
    GeolocationService provideGeolocationService() {
        return new MockGeoLocator();
    }
}
