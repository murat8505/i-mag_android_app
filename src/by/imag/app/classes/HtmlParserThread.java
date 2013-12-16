package by.imag.app.classes;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class HtmlParserThread implements Callable<Document>{
    private int pageNumber;
    private String pageURLString;

    public HtmlParserThread(int pageNumber) {
        this.pageNumber = pageNumber;
        this.pageURLString = Constants.PAGE + pageNumber;
    }

    @Override
    public Document call() throws Exception {
        Document htmlDocument = null;
        logMsg("url: "+pageURLString);
        URL url = new URL(pageURLString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            htmlDocument = Jsoup.connect(pageURLString).get();
        } else {
            logMsg("response code: "+connection.getResponseCode());
        }
        return htmlDocument;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
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
