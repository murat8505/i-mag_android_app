package by.imag.app.classes;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class HtmlParserThread implements Callable<Document>{
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;

    private int pageNumber;
    private String pageURLString;

//    public HtmlParserThread(int pageNumber) {
//        this.pageNumber = pageNumber;
//        this.pageURLString = Constants.PAGE + pageNumber;
//    }

    public HtmlParserThread(String pageURLString) {
        this.pageURLString = pageURLString;
    }

    @Override
    public Document call() throws Exception {
        Document htmlDocument = null;
        logMsg("url: "+pageURLString);
        URL url = new URL(pageURLString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(WAIT_TIMEOUT);
        connection.setReadTimeout(WAIT_TIMEOUT);
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            htmlDocument = Jsoup.connect(pageURLString).get();
        } else {
            logMsg("response code: "+connection.getResponseCode());
        }
        connection.disconnect();
        return htmlDocument;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageURLString() {
        return pageURLString;
    }

    public void setPageURLString(String pageURLString) {
        this.pageURLString = pageURLString;
    }
}
