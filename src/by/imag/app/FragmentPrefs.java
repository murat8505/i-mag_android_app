package by.imag.app;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class FragmentPrefs extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        String prefName = getActivity().getResources().getString(R.string.pref_mag_style_key);
        Preference preference = findPreference(prefName);
    }
}
