package ucsoftworks.com.bikestation.web_service;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public class BikeStatus {
    public String username;
    public boolean onRent;
    public Date rentDate;

    public BikeStatus(boolean onRent, String username, Date rentDate) {
        this.onRent = onRent;
        this.username = username;
        this.rentDate = rentDate;
    }
}
