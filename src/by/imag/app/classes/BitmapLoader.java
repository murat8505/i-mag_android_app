package by.imag.app.classes;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public class BitmapLoader implements Callable<Bitmap>{

    private String imageURLString;

    public BitmapLoader(String imageURLString) {
        this.imageURLString = imageURLString;
    }

    @Override
    public Bitmap call() throws Exception {
        Bitmap bitmap = null;
        URL imageURL = new URL(imageURLString);
        HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream stream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
        } else {
            logMsg("error");
        }
//        URLConnection connection = imageURL.openConnection();
//        connection.setUseCaches(true);
//        Object response = connection.getContent();
//        if (response instanceof Bitmap) {
//            bitmap = (Bitmap) response;
//        }
        return bitmap;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }
}
