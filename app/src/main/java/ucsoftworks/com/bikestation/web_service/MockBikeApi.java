package ucsoftworks.com.bikestation.web_service;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public class MockBikeApi implements BikeServiceApi {
    @Override
    public BikeStatus getBikeStatus() {
        return new BikeStatus(false, null, null);
    }
}
