package ucsoftworks.com.bikestation.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ucsoftworks.com.bikestation.application.BikeApp;

public class BikeFragment extends Fragment {

    @Inject
    Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BikeApp bikeApp = (BikeApp) getActivity().getApplication();
        bikeApp.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    protected BikeApp getApp() {
        return (BikeApp) getActivity().getApplication();
    }

    protected void postToBus(Object object) {
        if (getActivity() != null) {
            bus.post(object);
        }
    }

}
