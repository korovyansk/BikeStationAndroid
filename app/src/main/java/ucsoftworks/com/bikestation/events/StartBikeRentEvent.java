package ucsoftworks.com.bikestation.events;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */
public class StartBikeRentEvent {

    private String username;
    private Time rentDate;
    private float cost;

    public StartBikeRentEvent(String username, Time rentDate, float cost) {
        this.username = username;
        this.rentDate = rentDate;
        this.cost = cost;
    }

    public String getUsername() {
        return username;
    }

    public Time getRentDate() {
        return rentDate;
    }

    public float getCost() {
        return cost;
    }
}
