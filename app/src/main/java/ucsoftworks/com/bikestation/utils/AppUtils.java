package ucsoftworks.com.bikestation.utils;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import timber.log.Timber;

public class AppUtils {

    public static String getUuid(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        UUID uuid;
        try {
            uuid = UUID.nameUUIDFromBytes(manager.getDeviceId().getBytes("utf8"));
            Timber.d("Generated uuid %s", uuid);
        } catch (UnsupportedEncodingException e) {
            uuid = UUID.randomUUID();
            Timber.d("Generated random uuid %s", uuid);
        }
        return uuid.toString();
    }

    public static String formatStopWatch(long ms) {
        int seconds = (int) ms / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    public static int calculateCost(long ms) {
        return (int) (ms / (1000 * 60)) + 1;
    }

    public static float dpToPx(Resources resources, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, resources.getDisplayMetrics());
    }

    private AppUtils() {
    }
}
