package ucsoftworks.com.bikestation.services;

import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;
import ucsoftworks.com.bikestation.model.Station;

public interface ApiService {

    @FormUrlEncoded
    @POST("/bikes")
    public Observable<Void> register(@Field("uuid") String uuid, @Field("app_id") String appId,
                                     @Field("registration_id") String registrationId,
                                     @Field("name") String name, @Field("station_id") int stationId);

    @GET("/stations")
    public Observable<List<Station>> getStations();

    @FormUrlEncoded
    @POST("/bikes/{uuid}/location")
    public Observable<Void> track(@Path("uuid") String uuid, @Field("lat") double lat, @Field("lng") double lon);
}
