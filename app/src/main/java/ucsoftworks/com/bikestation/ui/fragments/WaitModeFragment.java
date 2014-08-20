package ucsoftworks.com.bikestation.ui.fragments;


import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.InjectView;
import ucsoftworks.com.bikestation.R;
import ucsoftworks.com.bikestation.events.StartRentEvent;
import ucsoftworks.com.bikestation.events.hack.VolumeUpHackEvent;
import ucsoftworks.com.bikestation.helpers.EndAnimatorListener;
import ucsoftworks.com.bikestation.ui.base.BikeFragment;

public class WaitModeFragment extends BikeFragment {

    @Inject @Named("EnableAnimations")
    boolean enableAnimations;

    @InjectView(R.id.wait_mode_logo)
    ImageView logoImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wait_mode, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (enableAnimations) {
            logoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoImage.animate().cancel();
                    logoImage.animate()
                            .scaleX(1.25f).scaleY(1.25f)
                            .setDuration(500)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .setListener(new EndAnimatorListener() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    logoImage.animate()
                                            .scaleX(1).scaleY(1)
                                            .setDuration(500)
                                            .setInterpolator(new LinearInterpolator())
                                            .setListener(new EndAnimatorListener() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    animation.cancel();
                                                }
                                            });
                                }
                            });
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (enableAnimations) {
            logoImage.animate().scaleX(0.01f).scaleY(0.01f).setDuration(0).setListener(new EndAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    logoImage.animate().scaleX(1).scaleY(1).setDuration(800).setInterpolator(new LinearInterpolator());
                }
            });
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe
    public void onVolumeUpHackEvent(VolumeUpHackEvent event) {
        postToBus(new StartRentEvent("Алексей Коровянский"));
    }

}
