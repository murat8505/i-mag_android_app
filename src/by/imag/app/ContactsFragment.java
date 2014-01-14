package by.imag.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import by.imag.app.classes.Constants;

public class ContactsFragment extends Fragment implements View.OnClickListener{
    private Button btnMap;
    private String uriStringGeo = "geo:53.906593,27.548191";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contacts_frag_scroll, container, false);
        btnMap = (Button) rootView.findViewById(R.id.btnmap);
        btnMap.setOnClickListener(this);
        setSubtitle();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnmap:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uriStringGeo));
                startActivity(intent);
            break;
        }
    }

    private void setSubtitle() {
        String[] strings = getResources().getStringArray(R.array.menu_items);
        String subtitle = strings[4];
        getActivity().getActionBar().setSubtitle(subtitle);
    }
}
