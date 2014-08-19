package ucsoftworks.com.bikestation.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;

import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.events.RegisteredSuccessfullyEvent;
import ucsoftworks.com.bikestation.ui.base.BikeActivity;
import ucsoftworks.com.bikestation.ui.fragments.RegistrationFragment;

public class RegisterActivity extends BikeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_default_container);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.default_container_placeholder, new RegistrationFragment())
                    .commit();
        }
    }

    @Subscribe
    public void onRegisteredEvent(RegisteredSuccessfullyEvent event) {
        startActivity(new Intent(this, WorkActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
