package by.imag.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentContacts extends BaseFragment implements View.OnClickListener{
    private Button btnMap;
    private String uriStringGeo = "geo:53.906593,27.548191";
//    private String subtitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        btnMap = (Button) rootView.findViewById(R.id.btnmap);
        btnMap.setOnClickListener(this);
//        subtitle = getResources().getStringArray(R.array.menu_items)[4];
//        getActivity().getActionBar().setSubtitle(subtitle);
        setActionBarSubtitle(5);
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
}
