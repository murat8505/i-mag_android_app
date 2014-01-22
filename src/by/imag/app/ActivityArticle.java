package by.imag.app;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.Constants;
import by.imag.app.classes.HtmlParserThread;

public class ActivityArticle extends Activity {
    private WebView webViewArticle;
    private String articleUrl;
    private ProgressBar pbArticle;
    private int articleId;
    private String postTitle;
    private String postText;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        webViewArticle = (WebView) findViewById(R.id.wvArticle);
        webViewArticle.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (savedInstanceState != null) {
            webViewArticle.restoreState(savedInstanceState);
        }
        pbArticle = (ProgressBar) findViewById(R.id.pbArticle);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.INTENT_POST);
//        int articleId = intent.getIntExtra(Constants.INTENT_POST, 0);
        articleId = bundle.getInt("articleId");
        postTitle = bundle.getString("postTitle");
        postText = bundle.getString("postText");
        if (articleId != 0) {
            articleUrl = Constants.ARTICLE + articleId;
            getActionBar().setSubtitle(postTitle);
//            webViewArticle.loadData(articleUrl, "text/html; charset=UTF-8", null);
            new ArticleLoader().execute(articleUrl);
        } else {
            logMsg("articleId: "+articleId);
        }
    }

//    public void onClickShare(View view) {
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(Intent.EXTRA_TITLE, postTitle);
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, postTitle);
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, articleUrl + "\n" +postText);
//        sharingIntent.putExtra(Intent.EXTRA_HTML_TEXT, post);
//        startActivity(Intent.createChooser(sharingIntent, "share via"));
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_post, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        shareActionProvider = (ShareActionProvider) item.getActionProvider();
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);
        setShareIntent(sharingIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
//            case R.id.menu_item_share:
//                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                setShareIntent(sharingIntent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webViewArticle.saveState(outState);
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    private class ArticleLoader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbArticle.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Document document = null;
            String result = "";
            articleUrl = strings[0];
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Document> documentFuture = executorService.submit(
                    new HtmlParserThread(articleUrl));
            try {
                document = documentFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            if (document != null) {
                Elements fbClasses = document.select(".fb-social-plugin");
//                logMsg("fbClasses: "+fbClasses);
                for (Element e: fbClasses) {
                    e.remove();
                }
                Elements noStyle = document.select(".nostyle");
//                logMsg("noStyle: "+noStyle);
                for (Element e: noStyle) {
                    e.remove();
                }
                Elements article = document.select(".article");

//                logMsg("activity_article: "+activity_article.get(0));
                result = article.get(0).toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            webViewArticle.loadData(result, "text/html; charset=UTF-8", null);
            pbArticle.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }
}
