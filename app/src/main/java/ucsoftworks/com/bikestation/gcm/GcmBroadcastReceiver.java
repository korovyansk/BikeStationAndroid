package ucsoftworks.com.bikestation.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import ucsoftworks.com.bikestation.main_screen.MainActivity;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        // Explicitly specify that GcmIntentService will handle the intent.
        Log.d(MainActivity.TAG, "Received message: " + intent.getExtras().toString());
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
