package ucsoftworks.com.bikestation.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import javax.inject.Inject;

import ucsoftworks.com.bikestation.data.PersistentStorage;
import ucsoftworks.com.bikestation.ui.base.BikeActivity;

public class EntryActivity extends BikeActivity {

    @Inject
    PersistentStorage persistentStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (persistentStorage.isRegistered()) {
            startActivity(new Intent(this, WorkActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        }
    }
}
