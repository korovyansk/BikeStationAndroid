package ucsoftworks.com.bikestation.main_screen;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ucsoftworks.com.bikestation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleFragment extends Fragment {


    public TitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title, container, false);
        return rootView;
    }

}
