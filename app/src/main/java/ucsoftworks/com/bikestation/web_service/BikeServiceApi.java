package ucsoftworks.com.bikestation.web_service;

import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public interface BikeServiceApi {
    @POST("/api/v1/bikes/register")
    int registerBike();

    @POST("/api/v1/bikes/{uuid}/location")
    int sendLocation(@Path("uuid") String uuid);
}
