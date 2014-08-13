package ucsoftworks.com.bikestation.application;

import android.app.Application;

import com.squareup.otto.Bus;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.ObjectGraph;
import ucsoftworks.com.bikestation.modules.AppModule;
import ucsoftworks.com.bikestation.modules.MockAppModule;

/**
 * Created by Pasenchuk Victor on 31.07.14
 */
public class BikeApp extends Application {

    private ObjectGraph objectGraph;

    final Timer timer = new Timer();

    @Inject
    Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(new MockAppModule(), new AppModule(this));
        inject(this);

        set30SecTimer();
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    private void set30SecTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                bus.post(new TimerEvent());
            }
        }, 0, 999);
    }


    public void onTerminate() {
        timer.cancel();
    }

}
