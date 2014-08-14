package ucsoftworks.com.bikestation.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.otto.Bus;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.StartBikeRentEvent;
import ucsoftworks.com.bikestation.events.StopBikeRentEvent;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class GcmIntentService extends IntentService {

    private static final String CODE_KEY = "code", RENT_KEY = "rent", USER_KEY = "user";
    private static final String CODE_NEW = "new", CODE_CLOSE = "close";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Inject
    Bus bus;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!(extras != null && extras.isEmpty())) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                bus.register(this);
//
//                bus.unregister(this);

                try {
                    final JSONObject rent = new JSONObject(extras.getString(RENT_KEY));
                    final String code = extras.getString(CODE_KEY);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);
                    Date date = simpleDateFormat.parse(rent.getString("openned_at"));
                    if (code.equalsIgnoreCase(CODE_NEW)) {
                        final JSONObject user = new JSONObject(extras.getString(USER_KEY));
                        bus.post(new StartBikeRentEvent(
                                String.format("%s %s", user.getString("name"), user.getString("surname")),
                                date.getTime(),
                                Float.parseFloat(rent.getString("cost"))
                        ));
                    }
                    if (code.equalsIgnoreCase(CODE_CLOSE)) {
                        Date endDate = simpleDateFormat.parse(rent.getString("closed_at"));
                        bus.post(new StopBikeRentEvent(
                                endDate.getTime(),
                                date.getTime(),
                                Float.parseFloat(rent.getString("total_cost"))
                        ));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

}
