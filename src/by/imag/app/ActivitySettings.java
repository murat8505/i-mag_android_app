package by.imag.app;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class ActivitySettings extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FragmentPrefs()).commit();
    }
}
