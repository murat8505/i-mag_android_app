package by.imag.app;

import android.app.ActionBar;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;

import by.imag.app.classes.Constants;

public class BaseFragment extends Fragment {
    protected ActionBar actionBar;
    protected String subtitle;

    protected boolean isOnline() {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // networkInfo.isConnected
                // networkInfo.isConnectedOrConnecting()
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    protected void setActionBarSubtitle(int menuItem) {
        actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            subtitle = getResources().getStringArray(R.array.menu_items)[menuItem];
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void setActionBarSubtitle(String text) {
        actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(text);
        }
    }

    protected void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }
}
