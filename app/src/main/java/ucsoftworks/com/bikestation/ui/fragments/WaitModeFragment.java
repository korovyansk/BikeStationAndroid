package ucsoftworks.com.bikestation.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.events.StartRentEvent;
import ucsoftworks.com.bikestation.events.hack.VolumeUpHackEvent;
import ucsoftworks.com.bikestation.ui.base.BikeFragment;

public class WaitModeFragment extends BikeFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wait_mode, container, false);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onVolumeUpHackEvent(VolumeUpHackEvent event) {
        postToBus(new StartRentEvent("Алексей Коровянский"));
    }

}
