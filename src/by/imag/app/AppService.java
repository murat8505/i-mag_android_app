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
import java.util.regex.Pattern;

import by.imag.app.classes.ArticlePreview;
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
        logMsg("onDestroy");
        currentPage = 1;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                int page = 1;
                if (isOnline()) {
                    parseDocument(page);
                }
            }
        }).start();
    }

    public void parseNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    logMsg("parse next");
                    if (currentPage != lastPage) {
                        parseDocument(currentPage + 1);
                    }
                }
            }
        }).start();

    }

    public void parsePrevious() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isOnline()) {
                    logMsg("parse previous");
                    if (currentPage != 1) {
                        parseDocument(currentPage - 1);
                    }
                }
            }
        }).start();

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
        getArticlePreviewList(document);
        List<ArticlePreview> articlePreviewList = getArticlePreviewList(document);
        boolean isArticlesUpdated = appDb.writeArticlesTable(articlePreviewList);
        if (isArticlesUpdated) {
            Intent intentArticles = new Intent(Constants.BROADCAST_ACTION);
            intentArticles.putExtra(Constants.INTENT_ARTICLES, isArticlesUpdated);
            sendBroadcast(intentArticles);
        }

        setPages(document);
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

    private List<ArticlePreview> getArticlePreviewList(Document document) {
        List<ArticlePreview> articlePreviews = new ArrayList<ArticlePreview>();
        if (document != null) {
            Elements previews = document.select("div[class=preview]");
            logMsg("previews: " + previews.size());
            for (Element e: previews) {
                String articleTitle = "";
                String previewText = "";
                String articleURL = "";
                String imageURL = "";
                Elements hrefs = e.select("a[href]");
                if (hrefs.size() == 1) {
                    articleURL = hrefs.get(0).attr("href");
                }
                Elements imgs = hrefs.select("img");
                if (imgs.size() == 1) {
                    articleTitle = imgs.get(0).attr("title");
                    previewText = imgs.get(0).attr("alt");
                    imageURL = imgs.get(0).attr("src");
                }
                ArticlePreview articlePreview = new ArticlePreview(articleTitle, previewText,
                        articleURL, imageURL);
//                logMsg(articlePreview+"");
                articlePreviews.add(articlePreview);
            }
        }
        return articlePreviews;
    }

//    private int getCurrentPage(Document document) {
//        int currentPage = 0;
//        if (document != null) {
//            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
//            Elements span = elementsNavigation.get(0).select("span[class=current]");
//            String pageStr = span.get(0).text();
//            currentPage = Integer.parseInt(pageStr);
//        }
//        return currentPage;
//    }

    private void setPages(Document document) {
        if (document != null) {
            Elements elementsNavigation = document.select("div[class=wp-pagenavi]");
            Elements span = elementsNavigation.get(0).select("span[class=current]");
            String pageStr = span.get(0).text();
            currentPage = Integer.parseInt(pageStr);
            Elements last = elementsNavigation.get(0).select("a[class=last]");
//            logMsg("last: "+last.size());
            String lastStr = last.get(0).attr("href");
            logMsg("last: "+lastStr);
            String[] strings = lastStr.split("=");
//            logMsg(strings[0]+"\n"+strings[1]);
            lastStr = strings[1];
            lastPage = Integer.parseInt(lastStr);
        }
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
