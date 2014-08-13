package ucsoftworks.com.bikestation.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pasenchuk Victor on 01.08.14
 */
public class SharedPreferencesManager {
    private static final String PREFS_KEY = "bike", ID_KEY = "ID_KEY";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(Application application) {
        sharedPreferences = application.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getRegId() {
        return sharedPreferences.getString(ID_KEY, null);
    }

    public void setRegId(String RegId) {
        editor.putString(ID_KEY, RegId)
                .apply();
    }

}
