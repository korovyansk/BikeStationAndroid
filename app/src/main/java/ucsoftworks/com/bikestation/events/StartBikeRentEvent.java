package ucsoftworks.com.bikestation.events;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */
public class StartBikeRentEvent {

    private final String username;
    private final long rentDate;
    private final float cost;

    public StartBikeRentEvent(String username, long rentDate, float cost) {
        this.username = username;
        this.rentDate = rentDate;
        this.cost = cost;
    }

    public String getUsername() {
        return username;
    }

    public long getRentDate() {
        return rentDate;
    }

    public float getCost() {
        return cost;
    }
}
