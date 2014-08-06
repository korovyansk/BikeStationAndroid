package ucsoftworks.com.bikestation.events;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */
public class StartBikeRentEvent {

    private String username;
    private Date rentDate;
    private float cost;

    public StartBikeRentEvent(String username, Date rentDate, float cost) {
        this.username = username;
        this.rentDate = rentDate;
        this.cost = cost;
    }

    public String getUsername() {
        return username;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public float getCost() {
        return cost;
    }
}
