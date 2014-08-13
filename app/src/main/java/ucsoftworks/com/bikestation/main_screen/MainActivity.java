package ucsoftworks.com.bikestation.main_screen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.application.SharedPreferencesManager;
import ucsoftworks.com.bikestation.events.StartBikeRentEvent;
import ucsoftworks.com.bikestation.events.StopBikeRentEvent;


public class MainActivity extends Activity {

    public static final String TAG = "GcmDemo";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String NOTIFICATION = "notification";

    String SENDER_ID = "895458163781";

    GoogleCloudMessaging gcm;
    String regId;

    @Inject
    Bus bus;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null)
            getActionBar().hide();
        setContentView(R.layout.activity_main);

        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);


        Intent intent = getIntent();
        if (intent.hasExtra(NOTIFICATION)) {
            String notification = intent.getStringExtra(NOTIFICATION);
            Log.d(TAG, "Notification text: " + notification);
        }

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regId = getRegistrationId();

            if (regId.isEmpty()) {
                registerInBackground();
            }
        }

        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) fragmentManager.beginTransaction()
                .add(R.id.container, new TitleFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        bus.unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onStartRent(StartBikeRentEvent startBikeRentEvent) {
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(startBikeRentEvent.getUsername(), startBikeRentEvent.getRentDate(), startBikeRentEvent.getCost()))
                .commit();
    }

    @Subscribe
    public void onStopRent(StopBikeRentEvent stopBikeRentEvent) {
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new EndFragment())
                .commit();
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    private String getRegistrationId() {
        String registrationId = sharedPreferencesManager.getRegId();
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = sharedPreferencesManager.getAppVersion();
        int currentVersion = getAppVersion(getApplicationContext());
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private String getUuid() {
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        UUID uuid;
        try {
            uuid = UUID.nameUUIDFromBytes(manager.getDeviceId().getBytes("utf8"));
            Log.d(TAG, "Generated uuid " + uuid.toString());
        } catch (UnsupportedEncodingException e) {
            uuid = UUID.randomUUID();
            Log.d(TAG, "Generated random uuid " + uuid.toString());
        }
        return uuid.toString();
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, msg);
            }
        }.execute(null, null, null);

    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        try {
            String uri = "http://smartbike.herokuapp.com/api/v1/bikes/register";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            String json = new JSONObject()
                    .accumulate("uuid", getUuid())
                    .accumulate("app_id", SENDER_ID)
                    .accumulate("registration_id", regId)
                    .accumulate("name", Build.MODEL)
                    .toString();

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse = httpclient.execute(httpPost);

            InputStream inputStream = httpResponse.getEntity().getContent();

            String result;
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
            Log.d(TAG, "Result: " + result);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId) {
        int appVersion = getAppVersion(getApplicationContext());
        sharedPreferencesManager.setAppVersion(appVersion);
        sharedPreferencesManager.setRegId(regId);
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        Time now = new Time();
        now.setToNow();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                bus.post(new StartBikeRentEvent("Алексей Коровянский", now, 120));
                return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                bus.post(new StopBikeRentEvent(now, 500));
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
