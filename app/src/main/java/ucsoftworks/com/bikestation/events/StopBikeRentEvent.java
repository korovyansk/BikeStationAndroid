package ucsoftworks.com.bikestation.events;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by Pasenchuk Victor on 06.08.14
 */

public class StopBikeRentEvent {
    private final long stopRentDate;
    private final float totalCost;

    public StopBikeRentEvent(long stopRentDate, float totalCost) {
        this.stopRentDate = stopRentDate;
        this.totalCost = totalCost;
    }

    public long getStopRentDate() {
        return stopRentDate;
    }

    public float getTotalCost() {
        return totalCost;
    }
}
