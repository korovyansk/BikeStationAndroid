package ucsoftworks.com.bikestation.main_screen;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.FinishTimeOutEvent;

/**
 * A simple {@link Fragment} subclass.
 */
public class EndFragment extends Fragment {

    private static final String ARG_END_RENT_TIME = "username";
    private static final String ARG_RENT_TIME = "rentTime";
    private static final String ARG_COST = "cost";

    private Time endRentTime;
    private Time rentTime;
    private Float cost;

    @InjectView(R.id.time_label)
    TextView timeField;
    @InjectView(R.id.cost_label)
    TextView costField;

    @Inject
    Bus bus;

    public EndFragment() {
        // Required empty public constructor
    }

    public static EndFragment newInstance(long rentTime, long endRentTime, float cost) {
        EndFragment fragment = new EndFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_END_RENT_TIME, endRentTime);
        args.putLong(ARG_RENT_TIME, rentTime);
        args.putFloat(ARG_COST, cost);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rentTime = new Time();
        endRentTime = new Time();
        initFromArguments(savedInstanceState == null ? getArguments() : savedInstanceState);
    }

    private void initFromArguments(Bundle bundle) {
        endRentTime.set(bundle.getLong(ARG_END_RENT_TIME));
        rentTime.set(bundle.getLong(ARG_RENT_TIME));
        cost = bundle.getFloat(ARG_COST);
    }

    @Override
    public void onSaveInstanceState(android.os.Bundle outState) {
        outState.putFloat(ARG_COST, cost);
        outState.putLong(ARG_RENT_TIME, rentTime.toMillis(false));
        outState.putLong(ARG_END_RENT_TIME, endRentTime.toMillis(false));
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        BikeApp bikeApp = (BikeApp) getActivity().getApplication();
        bikeApp.inject(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bus.post(new FinishTimeOutEvent());
            }
        }, 300_000);

        View view = inflater.inflate(R.layout.fragment_end, container, false);
        ButterKnife.inject(this, view);

        int hours, minutes, seconds;
        long difference = endRentTime.toMillis(false) - rentTime.toMillis(false);
        hours = (int) (difference / (1000 * 60 * 60));
        minutes = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);

        costField.setText(String.format("%.0f %s", cost * difference / (1000 * 60), "р."));
        timeField.setText(String.format("%d:%02d", hours, minutes));

        return view;
    }

    @OnClick(R.id.root_layout)
    public void backToHomeScreen() {
        bus.post(new FinishTimeOutEvent());
    }
}
