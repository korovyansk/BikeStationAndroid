package ucsoftworks.com.bikestation.main_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.StartBikeRentEvent;
import ucsoftworks.com.bikestation.events.StopBikeRentEvent;


public class MainActivity extends Activity {

    @Inject
    Bus bus;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_main);

        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);
        bus.register(this);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, new TitleFragment())
                .commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
