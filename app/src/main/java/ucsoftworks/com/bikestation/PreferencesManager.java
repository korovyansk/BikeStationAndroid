package ucsoftworks.com.bikestation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public class PreferencesManager {
    private static final String PREFS_KEY = "bike";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesManager(Application application)
    {
        sharedPreferences = application.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

}
