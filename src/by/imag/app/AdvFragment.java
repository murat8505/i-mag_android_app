package by.imag.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdvFragment extends Fragment implements View.OnClickListener{
    private Button btnPrice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adv_frag, container, false);
        btnPrice = (Button) rootView.findViewById(R.id.btnPrice);
        btnPrice.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrice:
                Intent advIntent = new Intent(getActivity(), AdvActivity.class);
                startActivity(advIntent);
        }
    }
}
