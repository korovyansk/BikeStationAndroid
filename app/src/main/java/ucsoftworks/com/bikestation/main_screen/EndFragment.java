package ucsoftworks.com.bikestation.main_screen;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ucsoftworks.com.bikestation.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EndFragment extends Fragment {


    public EndFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_end, container, false);
    }


}
