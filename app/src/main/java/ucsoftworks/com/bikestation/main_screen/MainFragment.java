package ucsoftworks.com.bikestation.main_screen;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
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
    private final Timer timer = new Timer();
    private final Time time = new Time();

    @Inject
    GeolocationService geolocationService;
    @InjectView(R.id.username)
    TextView usernameField;
    @InjectView(R.id.time_label)
    TextView timeField;
    @InjectView(R.id.distance_label)
    TextView distanceField;
    @InjectView(R.id.cost_label)
    TextView costField;
    private String username;
    private Time rentTime;
    private Float cost;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int hours, minutes, seconds;
            time.setToNow();
            long difference = time.toMillis(false) - rentTime.toMillis(false);
            hours = (int) (difference / (1000 * 60 * 60));
            minutes = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
            seconds = (int) (difference - (1000 * 60 * 60 * hours) - (1000 * 60 * minutes)) / 1000;

            distanceField.setText(String.format("%.3f %s", geolocationService.getDistance() / 1000, "км"));
            costField.setText(String.format("%.2f %s", cost * difference / (1000 * 60 * 60), "р."));
            timeField.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));
        }
    };

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

        rentTime = new Time();
        initFromArguments(savedInstanceState == null ? getArguments() : savedInstanceState);
    }

    private void initFromArguments(Bundle bundle) {
        username = bundle.getString(ARG_USERNAME);
        rentTime.set(bundle.getLong(ARG_RENT_TIME));
        cost = bundle.getFloat(ARG_COST);
    }

    @Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        outState.putString(ARG_USERNAME, username);
        outState.putFloat(ARG_COST, cost);
        outState.putLong(ARG_RENT_TIME, rentTime.toMillis(false));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BikeApp bikeApp = (BikeApp) getActivity().getApplication();
        bikeApp.inject(this);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        usernameField.setText(username);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, 0, 999);
    }

    @Override
    public void onStop() {
        timer.purge();
        super.onStop();
    }


}
