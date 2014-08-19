package ucsoftworks.com.bikestation.ui.activities;

import android.os.Bundle;

import com.squareup.otto.Subscribe;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.events.ReturnToWaitModeEvent;
import ucsoftworks.com.bikestation.events.StartRentEvent;
import ucsoftworks.com.bikestation.events.StopRentEvent;
import ucsoftworks.com.bikestation.ui.base.BikeActivity;
import ucsoftworks.com.bikestation.ui.fragments.RentFragment;
import ucsoftworks.com.bikestation.ui.fragments.WaitModeFragment;

public class WorkActivity extends BikeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_default_container);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.default_container_placeholder, new WaitModeFragment())
                    .commit();
        }
    }

    @SuppressWarnings("UnusedDeclaration") //used by event bus
    @Subscribe
    public void onRentStarted(StartRentEvent event) {
        getFragmentManager().beginTransaction()
                .replace(R.id.default_container_placeholder, RentFragment.newInstance(event.getUsername()))
                .commit();
    }

    @SuppressWarnings("UnusedDeclaration") //used by event bus
    @Subscribe
    public void onRentStopped(StopRentEvent event) {
    }

    @SuppressWarnings("UnusedDeclaration") //used by event bus
    @Subscribe
    public void onReturnToWaitMode(ReturnToWaitModeEvent event) {
        getFragmentManager().beginTransaction()
                .replace(R.id.default_container_placeholder, new WaitModeFragment())
                .commit();
    }
}
