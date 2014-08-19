package ucsoftworks.com.bikestation.ui.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.Config;
import ucsoftworks.com.bikestation.events.RegisteredSuccessfullyEvent;
import ucsoftworks.com.bikestation.model.Station;
import ucsoftworks.com.bikestation.services.ApiService;
import ucsoftworks.com.bikestation.ui.base.BikeFragment;
import ucsoftworks.com.bikestation.utils.AppUtils;

public class RegistrationFragment extends BikeFragment {

    @Inject
    ApiService apiService;

    @Inject
    Resources resources;

    @InjectView(R.id.registration_progress_frame)
    FrameLayout operationInProgressFrame;

    @InjectView(R.id.registration_bike_model)
    Spinner bikeModelSpinner;

    @InjectView(R.id.registration_bike_station)
    Spinner bikeStationSpinner;

    private Subscription getStationsSubscription;
    private Subscription registerSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        operationInProgressFrame.setVisibility(View.INVISIBLE);
        operationInProgressFrame.bringToFront();
        operationInProgressFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        final List<String> models = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.registration_bikes_models)));
        models.add(0, resources.getString(R.string.registration_bike_model_placeholder));

        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                models);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bikeModelSpinner.setAdapter(modelAdapter);

        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                new String[]{resources.getString(R.string.registration_bike_station_placeholder)});
        stationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bikeStationSpinner.setAdapter(stationAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        operationRunning(true);
        getStationsSubscription = AndroidObservable.bindFragment(this, apiService.getStations()
                .subscribeOn(Schedulers.io()))
                .subscribe(
                        new Action1<List<Station>>() {
                            @Override
                            public void call(List<Station> resultStations) {
                                List<Map<String, ?>> data = new ArrayList<>();
                                for (Station station : resultStations) {
                                    final Map<String, Object> map = new HashMap<>();
                                    map.put("station_id", station.id);
                                    map.put("station_name", station.name);
                                    data.add(map);
                                }
                                final Map<String, Object> map = new HashMap<>();
                                map.put("station_id", -1);
                                map.put("station_name", resources.getString(R.string.registration_bike_station_placeholder));
                                data.add(0, map);
                                SimpleAdapter stationsAdapter = new SimpleAdapter(getActivity(), data, android.R.layout.simple_spinner_item,
                                        new String[]{"station_name"}, new int[]{android.R.id.text1});
                                stationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                bikeStationSpinner.setAdapter(stationsAdapter);
                                operationRunning(false);
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Timber.d(throwable, "Exception during getStations request");
                                operationRunning(false);
                            }
                        }
                );
    }

    @Override
    public void onStop() {
        super.onStop();
        if (registerSubscription != null) {
            registerSubscription.unsubscribe();
        }
        if (getStationsSubscription != null) {
            getStationsSubscription.unsubscribe();
        }
    }

    @SuppressWarnings("UnusedDeclaration") // used by injector
    @OnClick(R.id.registration_register)
    /*injected*/ void onClickRegisterButton() {
        final String bikeName = bikeModelSpinner.getSelectedItem().toString();
        final Map<String, Object> bikeStationMap = (Map<String, Object>) bikeStationSpinner.getSelectedItem();
        final int stationId = (int) bikeStationMap.get("station_id");

        if (bikeName.equals(resources.getString(R.string.registration_bike_model_placeholder))) {
            bikeModelSpinner.animate()
                    .x(bikeModelSpinner.getX() + AppUtils.dpToPx(resources, 10))
                    .setInterpolator(new CycleInterpolator(5));
            return;
        }
        if (stationId == -1) {
            bikeStationSpinner.animate()
                    .x(bikeStationSpinner.getX() + AppUtils.dpToPx(resources, 10))
                    .setInterpolator(new CycleInterpolator(5));
            return;
        }
        final String uuid = AppUtils.getUuid(getApp());
        operationRunning(true);
        registerSubscription = AndroidObservable.bindFragment(this, Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
                    String regId = gcm.register(Config.GCM_APP_ID);
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(regId);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        }).flatMap(new Func1<String, Observable<?>>() {
            @Override
            public Observable<?> call(String regId) {
                return apiService.register(uuid, Config.GCM_APP_ID, regId, bikeName, stationId);
            }
        }))
        .subscribeOn(Schedulers.io())
        .subscribe(
                new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        getApp().registered(uuid);
                        operationRunning(false);
                        postToBus(new RegisteredSuccessfullyEvent());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        operationRunning(false);
                        Toast.makeText(getActivity(), "Error during registration", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void operationRunning(boolean running) {
        operationInProgressFrame.setVisibility(running ? View.VISIBLE : View.INVISIBLE);
    }

}
