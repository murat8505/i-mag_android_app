package by.imag.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentAdv extends BaseFragment implements View.OnClickListener{
    private Button btnPrice;
//    private String subtitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adv_frag, container, false);
        btnPrice = (Button) rootView.findViewById(R.id.btnPrice);
        btnPrice.setOnClickListener(this);
//        subtitle = getResources().getStringArray(R.array.menu_items)[5];
//        getActivity().getActionBar().setSubtitle(subtitle);
        setActionBarSubtitle(5);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrice:
                Intent advIntent = new Intent(getActivity(), ActivityAdv.class);
                startActivity(advIntent);
        }
    }
}
