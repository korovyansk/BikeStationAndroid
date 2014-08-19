package ucsoftworks.com.bikestation.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.otto.Bus;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import timber.log.Timber;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.StartRentEvent;
import ucsoftworks.com.bikestation.events.StopRentEvent;


public class GcmIntentService extends IntentService {

    @Inject
    Bus bus;

    @Inject
    Handler handler;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!(extras != null && extras.isEmpty())) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                try {
                    final String code = extras.getString("code");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    if (code.equalsIgnoreCase("new")) {
                        final JSONObject user = new JSONObject(extras.getString("user"));
                        final String name = user.getString("name");
                        final String surname = user.getString("surname");
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                bus.post(new StartRentEvent(String.format("%s %s", name, surname)));
                            }
                        });
                    }
                    if (code.equalsIgnoreCase("close")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                bus.post(new StopRentEvent());
                            }
                        });
                    }
                } catch (Exception e) {
                    Timber.e(e, "exception in GcmIntentService.handleIntent");
                }

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}
