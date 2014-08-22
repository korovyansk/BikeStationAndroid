package ucsoftworks.com.bikestation.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;
import ucsoftworks.com.bikestation.BuildConfig;
import ucsoftworks.com.bikestation.data.PersistentStorage;
import ucsoftworks.com.bikestation.modules.AppModule;
import ucsoftworks.com.bikestation.modules.RegisteredModule;

public class BikeApp extends Application {

    @Inject
    PersistentStorage storage;

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
        if (storage.isRegistered()) {
            plus(new RegisteredModule(storage.getUUID()));
        }
        prepare();
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public void registered(String uuid, String bikeModel) {
        storage.setUUID(uuid);
        storage.setBikeModel(bikeModel);
        plus(new RegisteredModule(uuid));
    }

    private void plus(Object... modules) {
        objectGraph = objectGraph.plus(modules);
    }

    private void prepare() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Crashlytics.start(this);
    }

}
