package ucsoftworks.com.bikestation.modules;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.data.PersistentStorage;
import ucsoftworks.com.bikestation.data.RealPersistentStorage;
import ucsoftworks.com.bikestation.gcm.GcmIntentService;
import ucsoftworks.com.bikestation.helpers.UITimer;
import ucsoftworks.com.bikestation.services.ApiService;
import ucsoftworks.com.bikestation.services.LocationService;
import ucsoftworks.com.bikestation.services.mock.MockLocationService;
import ucsoftworks.com.bikestation.ui.activities.EntryActivity;
import ucsoftworks.com.bikestation.ui.activities.RegisterActivity;
import ucsoftworks.com.bikestation.ui.activities.WorkActivity;
import ucsoftworks.com.bikestation.ui.fragments.RegistrationFragment;
import ucsoftworks.com.bikestation.ui.fragments.WaitModeFragment;

@Module(
        library = true,
        injects = {
            BikeApp.class, GcmIntentService.class,
            EntryActivity.class, RegisterActivity.class, WorkActivity.class,
            RegistrationFragment.class, WaitModeFragment.class
        }
)
public class AppModule {

    private BikeApp bikeApp;

    public AppModule(BikeApp bikeApp) {
        this.bikeApp = bikeApp;
    }

    @Provides @Singleton
    Bus provideBus() {
        return new Bus();
    }

    @Provides
    Handler provideHandler() {
        return new Handler(Looper.getMainLooper());
    }

    @Provides
    Resources provdeResources() {
        return bikeApp.getResources();
    }

//    @Provides
//    SharedPreferencesManager provideSharedPreferencesManager() {
//        return new SharedPreferencesManager(bikeApp);
//    }

    @Provides @Singleton
    UITimer provideUITimer() {
        return new UITimer();
    }

    @Provides @Singleton
    LocationService provideLocationService(UITimer uiTimer) {
        return new MockLocationService(uiTimer);
    }

    @Provides @Singleton
    ApiService provideApiService() {
//        return new MockApiService(500);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://smart-bikes.herokuapp.com/api/v1/")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                    }
                })
                .build();
        return restAdapter.create(ApiService.class);
    }

    @Provides @Singleton
    PersistentStorage providePersistentStorage() {
        return new RealPersistentStorage(bikeApp);
    }

    @Provides @Named("AllowHackEvents")
     boolean allowHackEvents() {
        return true;
    }

    @Provides @Named("LocationTrackPeriodMs")
    int provideLocationTrackPeriodMs() {
        return 1000;
    }
}
