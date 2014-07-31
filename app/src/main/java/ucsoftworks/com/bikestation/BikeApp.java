package ucsoftworks.com.bikestation;

import android.app.Application;

import dagger.ObjectGraph;
import ucsoftworks.com.bikestation.modules.AppModule;
import ucsoftworks.com.bikestation.modules.MockAppModule;

/**
 * Created by Pasenchuk Victor on 31.07.14
 */
public class BikeApp extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MockAppModule(), new AppModule());
    }

    public void injectToObjectGraph(Object object) {
        objectGraph.inject(object);
    }
}
