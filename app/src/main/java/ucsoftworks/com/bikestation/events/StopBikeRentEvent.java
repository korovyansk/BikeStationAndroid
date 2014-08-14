package ucsoftworks.com.bikestation.events;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */

public class StopBikeRentEvent {
    private final long stopRentDate;
    private final long rentDate;
    private final float totalCost;

    public StopBikeRentEvent(long stopRentDate, long rentDate, float totalCost) {
        this.stopRentDate = stopRentDate;
        this.rentDate = rentDate;
        this.totalCost = totalCost;
    }

    public long getRentDate() {
        return rentDate;
    }

    public long getStopRentDate() {
        return stopRentDate;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
