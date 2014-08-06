package ucsoftworks.com.bikestation.events;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */

public class StopBikeRentEvent {
    Date stopRentDate;
    float totalCost;

    public StopBikeRentEvent(Date stopRentDate, float totalCost) {
        this.stopRentDate = stopRentDate;
        this.totalCost = totalCost;
    }

    public Date getStopRentDate() {
        return stopRentDate;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
