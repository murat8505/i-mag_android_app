package by.imag.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.Constants;
import by.imag.app.classes.HtmlParserThread;
import by.imag.app.classes.TagItem;


public class AppService extends Service {
    ServiceBinder serviceBinder = new ServiceBinder();
    private int currentPage = 1;
    private int lastPage;
    private AppDb appDb;

    @Override
    public void onCreate() {
        super.onCreate();
        appDb = new AppDb(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

    public void parse() {
        int page = 1;
        if (isOnline()) {
            parseDocument(page);
        }
    }

    public void parseNext() {
        if (isOnline()) {
            // TODO: get current page
            // TODO: if current page != last page -> parseDocument(current page + 1)
        }
    }

    private void parseDocument(int page) {
        Document document = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Document> documentFuture = executorService.submit(new
                HtmlParserThread(page));
        try {
            document = documentFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        List<TagItem> tags = getTags(document);
        boolean isTagsUpdated = appDb.writeTagTable(tags);
        if (isTagsUpdated) {
            Intent intentTags = new Intent(Constants.BROADCAST_ACTION);
            intentTags.putExtra(Constants.INTENT_TAGS, isTagsUpdated);
            sendBroadcast(intentTags);
        }
        // todo: get article headers
        // todo: write article headers to db
        // todo: if written -> send broadcast
        // todo: set current page
        // todo: set last page
    }

    private List<TagItem> getTags(Document document) {
        List<TagItem> tags = new ArrayList<TagItem>();
        if (document != null ){
            Elements categories = document.select("div[id=categories]");
            Elements titles = categories.select("a[title]");
            for (Element e: titles) {
                Elements span = e.select("span");
                Element spanElement = span.get(0);
                String tagNamePostCount = e.text();
                String tagName = tagNamePostCount.replaceAll("\\s\\d*$", "");
                String tagURL = e.attr("href");
                int postCount = Integer.parseInt(spanElement.text());
                TagItem tagItem = new TagItem(tagName, tagURL, postCount);
                tags.add(tagItem);
            }
        }
        return  tags;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
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
