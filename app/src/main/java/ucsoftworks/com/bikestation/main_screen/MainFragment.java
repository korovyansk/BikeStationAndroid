package ucsoftworks.com.bikestation.main_screen;


import android.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.TimerEvent;
import ucsoftworks.com.bikestation.geolocation.GeolocationService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_RENT_TIME = "rentTime";
    private static final String ARG_COST = "cost";
    @Inject
    Bus bus;
    @Inject
    GeolocationService geolocationService;
    @InjectView(R.id.username)
    TextView usernameField;
    @InjectView(R.id.time_label)
    TextView timeField;
    @InjectView(R.id.speed_label)
    TextView speedField;
    @InjectView(R.id.avg_speed_label)
    TextView avgSpeedField;
    @InjectView(R.id.distance_label)
    TextView distanceField;
    @InjectView(R.id.cost_label)
    TextView costField;
    private String username;
    private Time rentTime;
    private Float cost;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Parameter username.
     * @param rentTime Parameter rentTime.
     * @param cost     Parameter cost.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String username, Time rentTime, float cost) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putLong(ARG_RENT_TIME, rentTime.toMillis(false));
        args.putFloat(ARG_COST, cost);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BikeApp bikeApp = (BikeApp) getActivity().getApplication();
        bikeApp.inject(this);
        bus.register(this);

        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            rentTime = new Time();
            rentTime.set(getArguments().getLong(ARG_RENT_TIME));
            cost = getArguments().getFloat(ARG_COST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Subscribe
    void onTimer(TimerEvent event) {
        //
    }


}
