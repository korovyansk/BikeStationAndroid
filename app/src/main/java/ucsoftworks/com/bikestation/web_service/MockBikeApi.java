package ucsoftworks.com.bikestation.web_service;

import retrofit.http.Path;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public class MockBikeApi implements BikeServiceApi {
    @Override
    public int registerBike() {
        return 0;
    }

    @Override
    public int sendLocation(@Path("uuid") String uuid) {
        return 0;
    }
}
