package ucsoftworks.com.bikestation.data;

import android.content.Context;
import android.content.SharedPreferences;

public class RealPersistentStorage implements PersistentStorage {

    private final SharedPreferences sharedPreferences;

    public RealPersistentStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences("persistent", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isRegistered() {
        return getUUID() != null;
    }

    @Override
    public String getUUID() {
        return sharedPreferences.getString("uuid", null);
    }

    @Override
    public void setUUID(String uuid) {
        sharedPreferences.edit().putString("uuid", uuid).apply();
    }

    @Override
    public String getBikeModel() {
        return sharedPreferences.getString("bike_model", "unknown");
    }

    @Override
    public void setBikeModel(String bikeModel) {
        sharedPreferences.edit().putString("bike_model", bikeModel).apply();
    }
}
