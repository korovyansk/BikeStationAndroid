package ucsoftworks.com.bikestation.events;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */

public class StopBikeRentEvent {
    Time stopRentDate;
    float totalCost;

    public StopBikeRentEvent(Time stopRentDate, float totalCost) {
        this.stopRentDate = stopRentDate;
        this.totalCost = totalCost;
    }

    public Time getStopRentDate() {
        return stopRentDate;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
