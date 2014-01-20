package by.imag.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends BaseFragment{
//    private String subtitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_frag, container, false);
        subtitle = getResources().getStringArray(R.array.menu_items)[3];
//        getActivity().getActionBar().setSubtitle(subtitle);
        setActionBarSubtitle(3);
        return rootView;
    }
}
