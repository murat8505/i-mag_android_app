package by.imag.app.classes;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class HtmlParserPageThread implements Callable<Document> {
    private String articleUrl;

    public HtmlParserPageThread(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public Document call() throws Exception {
        Document document = null;
        URL url = new URL(articleUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            document = Jsoup.connect(articleUrl).get();
        }  else {
            logMsg("response code: "+connection.getResponseCode());
        }
        return document;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }
}
