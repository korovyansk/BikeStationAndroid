package ucsoftworks.com.bikestation.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Named;

import ucsoftworks.com.bikestation.application.BikeApp;
import ucsoftworks.com.bikestation.events.hack.VolumeDownHackEvent;
import ucsoftworks.com.bikestation.events.hack.VolumeUpHackEvent;


public class BikeActivity extends Activity {

    @Inject
    Bus bus;

    @Inject @Named("AllowHackEvents")
    boolean allowHackEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BikeApp bikeApp = (BikeApp) getApplication();
        bikeApp.inject(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (allowHackEvents) {
            switch(keyCode){
                case KeyEvent.KEYCODE_VOLUME_UP:
                    bus.post(new VolumeUpHackEvent());
                    break;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    bus.post(new VolumeDownHackEvent());
                    break;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);
    }


    //    /**
//     * Check the device to make sure it has the Google Play Services APK. If
//     * it doesn't, display a dialog that allows users to download the APK from
//     * the Google Play Store or enable it in the device's system settings.
//     */
//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Timber.i("This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

//    /**
//     * Gets the current registration ID for application on GCM service.
//     * <p/>
//     * If result is empty, the app needs to register.
//     *
//     * @return registration ID, or empty string if there is no existing
//     * registration ID.
//     */
//    private String getRegistrationId() {
//        String registrationId = sharedPreferencesManager.getRegId();
//        if (registrationId.isEmpty()) {
//            Log.i(TAG, "Registration not found.");
//            return "";
//        }
//        // Check if app was updated; if so, it must clear the registration ID
//        // since the existing regID is not guaranteed to work with the new
//        // app version.
//        int registeredVersion = sharedPreferencesManager.getAppVersion();
//        int currentVersion = getAppVersion(getApplicationContext());
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
//        return registrationId;
//    }
//
//
//    /**
//     * @return Application's version code from the {@code PackageManager}.
//     */
//    private static int getAppVersion(Context context) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager()
//                    .getPackageInfo(context.getPackageName(), 0);
//            return packageInfo.versionCode;
//        } catch (PackageManager.NameNotFoundException e) {
//            // should never happen
//            throw new RuntimeException("Could not get package name: " + e);
//        }
//    }
//
//    /**
//     * Registers the application with GCM servers asynchronously.
//     * <p/>
//     * Stores the registration ID and app versionCode in the application's
//     * shared preferences.
//     */
//    private void registerInBackground() {
//        new AsyncTask<Void, Void, String>() {
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    if (gcm == null) {
//                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
//                    }
//                    regId = gcm.register(SENDER_ID);
//                    msg = "Device registered, registration ID=" + regId;
//
//                    // You should send the registration ID to your server over HTTP,
//                    // so it can use GCM/HTTP or CCS to send messages to your app.
//                    // The request to your server should be authenticated if your app
//                    // is using accounts.
//                    sendRegistrationIdToBackend();
//
//                    // For this demo: we don't need to send it because the device
//                    // will send upstream messages to a server that echo back the
//                    // message using the 'from' address in the message.
//
//                    // Persist the regID - no need to register again.
//                    storeRegistrationId(regId);
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//                    // If there is an error, don't just keep trying to register.
//                    // Require the user to click a button again, or perform
//                    // exponential back-off.
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
//                Log.d(TAG, msg);
//            }
//        }.execute(null, null, null);
//
//    }

//    /**
//     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
//     * or CCS to send messages to your app. Not needed for this demo since the
//     * device sends upstream messages to a server that echoes back the message
//     * using the 'from' address in the message.
//     */
//    private void sendRegistrationIdToBackend() {
//        try {
//            String uri = "/api/v1/bikes/register";
//            String json = new JSONObject()
//                    .accumulate("uuid", getUuid())
//                    .accumulate("app_id", SENDER_ID)
//                    .accumulate("registration_id", regId)
//                    .accumulate("name", Build.MODEL)
//                    .toString();
//            sendRequestToBackend(uri, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendLocationToBackend() {
//        try {
//            String uri = "/api/v1/bikes/" + getUuid() + "/location";
//            String json = new JSONObject()
//                    .accumulate("lat", locationService.getLatitude())
//                    .accumulate("lng", locationService.getLongitude())
//                    .toString();
//            sendRequestToBackend(uri, json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void sendRequestToBackend(String uri, String json) {
//        try {
//            String baseUri = "http://smart-bikes.herokuapp.com";
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost(baseUri + uri);
//
//            StringEntity se = new StringEntity(json);
//            httpPost.setEntity(se);
//
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            HttpResponse httpResponse = httpclient.execute(httpPost);
//
//            InputStream inputStream = httpResponse.getEntity().getContent();
//
//            String result;
//            if (inputStream != null) {
//                result = convertInputStreamToString(inputStream);
//            } else {
//                result = "Did not work!";
//            }
//            Log.d(TAG, "Result: " + result);
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//    }
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String line;
//        String result = "";
//        while ((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }

//    /**
//     * Stores the registration ID and app versionCode in the application's
//     * {@code SharedPreferences}.
//     *
//     * @param regId registration ID
//     */
//    private void storeRegistrationId(String regId) {
//        int appVersion = getAppVersion(getApplicationContext());
//        sharedPreferencesManager.setAppVersion(appVersion);
//        sharedPreferencesManager.setRegId(regId);
//    }


}
