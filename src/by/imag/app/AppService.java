package by.imag.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;


public class AppService extends Service {
    ServiceBinder serviceBinder = new ServiceBinder();

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // networkInfo.isConnected
            // networkInfo.isConnectedOrConnecting()
            return true;
        }
        return false;
    }

    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    class ServiceBinder extends Binder {
        AppService getService() {
            return AppService.this;
        }
    }
}
