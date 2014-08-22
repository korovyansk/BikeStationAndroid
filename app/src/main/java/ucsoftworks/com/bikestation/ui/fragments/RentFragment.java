package ucsoftworks.com.bikestation.ui.fragments;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.events.LocationChangedEvent;
import ucsoftworks.com.bikestation.events.ReturnToWaitModeEvent;
import ucsoftworks.com.bikestation.events.StopRentEvent;
import ucsoftworks.com.bikestation.events.hack.VolumeDownHackEvent;
import ucsoftworks.com.bikestation.events.hack.VolumeUpHackEvent;
import ucsoftworks.com.bikestation.helpers.UITimer;
import ucsoftworks.com.bikestation.services.ApiService;
import ucsoftworks.com.bikestation.services.LocationService;
import ucsoftworks.com.bikestation.ui.base.BikeFragment;
import ucsoftworks.com.bikestation.utils.AppUtils;

public class RentFragment extends BikeFragment {

    @Inject @Named("LocationTrackPeriodMs")
    int locationTrackPeriod;

    @Inject @Named("RentOffsetMs")
    int rentOffsetMs;

    @Inject @Named("UUID")
    String uuid;

    @Inject
    UITimer uiTimer;

    @Inject
    LocationService locationService;

    @Inject
    ApiService apiService;

    @InjectView(R.id.rent_message_bar)
    TextView messageBar;

    @InjectView(R.id.rent_username)
    TextView usernameText;

    @InjectView(R.id.rent_time)
    TextView rentTimeText;

    @InjectView(R.id.rent_cost)
    TextView rentCostText;

    private String username;
    private Date startTime;

    public static RentFragment newInstance(String username) {
        RentFragment fragment = new RentFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFromArguments(savedInstanceState == null ? getArguments() : savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rent, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText.setText(username);
        messageBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postToBus(new ReturnToWaitModeEvent());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        locationService.start();
        uiTimer.schedule(new UITimer.Task() {
            @Override
            public void run() {
                Date now = new Date();
                long diff = rentOffsetMs + now.getTime() - startTime.getTime();
                rentTimeText.setText(AppUtils.formatStopWatch(diff));
                rentCostText.setText(AppUtils.calculateCost(diff) + " руб.");
            }
        }, 0, 999, "stopwatch");
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.stop();
        uiTimer.cancel("stopwatch");
    }

    @Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
        outState.putLong("startTime", startTime.getTime());
    }

    @OnClick(R.id.rent_action_music)
    /*injected*/ void onClickActionMusic() {
        Toast.makeText(getActivity(), "Проигрывание любимых треков\r\n(не реализовано в данной версии)",
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rent_action_fitness)
    /*injected*/ void onClickActionFitness() {
        Toast.makeText(getActivity(), "Фитнесс тренер\r\n(не реализовано в данной версии)",
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.rent_action_map)
    /*injected*/ void onClickActionMap() {
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.default_container_placeholder, new CityMapFragment(), "map")
                .addToBackStack("map")
                .commit();
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onLocationChanged(LocationChangedEvent event) {
        final Location location = event.getLocation();
        apiService.track(uuid, location.getLatitude(), location.getLongitude())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                Timber.d("%s tracked successfully", location);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.d(throwable, "exception during tracking location");
                            }
                        });
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onVolumeDownHackEvent(VolumeDownHackEvent event) {
        postToBus(new StopRentEvent());
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onVolumeUpHackEvent(VolumeUpHackEvent event) {
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.default_container_placeholder, new CityMapFragment(), "map")
                .addToBackStack("map")
                .commit();
    }

    @SuppressWarnings("UnusedDeclaration") //used by event bus
    @Subscribe
    public void onRentStopped(StopRentEvent event) {
        uiTimer.cancel("stopwatch");
        uiTimer.cancel("location_tracker");
        messageBar.setVisibility(View.VISIBLE);
        messageBar.setText(R.string.rent_completed);
        messageBar.setY(-messageBar.getHeight());
        messageBar.animate().y(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(400);
        uiTimer.schedule(new UITimer.Task() {
            @Override
            public void run() {
                postToBus(new ReturnToWaitModeEvent());
            }
        }, 15_000, "return_to_wait_mode");
    }

    private void initFromArguments(Bundle bundle) {
        username = bundle.getString("username");
        startTime = bundle.containsKey("startTime") ? new Date(bundle.getLong("startTime")) : new Date();
    }

}
