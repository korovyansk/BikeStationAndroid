package ucsoftworks.com.bikestation.services.mock;

import android.os.SystemClock;

import java.util.Arrays;
import java.util.List;

import retrofit.http.Field;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscriber;
import ucsoftworks.com.bikestation.model.Station;
import ucsoftworks.com.bikestation.services.ApiService;

public class MockApiService implements ApiService {

    private final int operationDelayMs;

    public MockApiService(int operationDelayMs) {
        this.operationDelayMs = operationDelayMs;
    }

    @Override
    public Observable<Void> register(@Field("uuid") String uuid, @Field("app_id") String appId, @Field("registration_id") String registrationId,
                                     @Field("name") String name, @Field("station_id") int stationId) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SystemClock.sleep(operationDelayMs);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<List<Station>> getStations() {
        return Observable.create(new Observable.OnSubscribe<List<Station>>() {
            @Override
            public void call(Subscriber<? super List<Station>> subscriber) {
                SystemClock.sleep(operationDelayMs);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(produceMockStationsList());
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<Void> track(@Path("uuid") String uuid, @Field("lat") double lat, @Field("lon") double lon) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                SystemClock.sleep(operationDelayMs);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                }
            }
        });
    }

    private static List<Station> produceMockStationsList() {
        return Arrays.asList(new Station(1, "Иртышская Набережная"), new Station(2, "Омская Крепость"));
    }
}
