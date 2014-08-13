package ucsoftworks.com.bikestation.main_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.application.SharedPreferencesManager;
import ucsoftworks.com.bikestation.events.StartBikeRentEvent;
import ucsoftworks.com.bikestation.events.StopBikeRentEvent;


public class MainActivity extends Activity {

    public static final String TAG = "GcmDemo";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String NOTIFICATION = "notification";

    @Inject
    Bus bus;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null)
            getActionBar().hide();
        setContentView(R.layout.activity_main);

        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);

        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) fragmentManager.beginTransaction()
                .add(R.id.container, new TitleFragment())
                .commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onStartRent(StartBikeRentEvent startBikeRentEvent) {
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(startBikeRentEvent.getUsername(), startBikeRentEvent.getRentDate(), startBikeRentEvent.getCost()))
                .commit();
    }

    @Subscribe
    public void onStopRent(StopBikeRentEvent stopBikeRentEvent) {
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new EndFragment())
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Time now = new Time();
        now.setToNow();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                bus.post(new StartBikeRentEvent("Алексей Коровянский", now, 120));
                return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                bus.post(new StopBikeRentEvent(now, 500));
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
