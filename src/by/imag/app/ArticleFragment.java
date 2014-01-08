package by.imag.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.Constants;
import by.imag.app.classes.HtmlParserPageThread;


public class ArticleFragment extends Fragment {
    private WebView webViewArticle;
    private String articleUrl;
    private ProgressBar pbArticle;

    public ArticleFragment(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public ArticleFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article, container, false);
        webViewArticle = (WebView) rootView.findViewById(R.id.wvArticle);
        webViewArticle.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        pbArticle = (ProgressBar) rootView.findViewById(R.id.pbArticle);
        logMsg("url: "+articleUrl);
        PageLoader pageLoader = new PageLoader();
        pageLoader.execute(articleUrl);
//        webViewArticle.loadUrl(articleUrl);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webViewArticle.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webViewArticle.restoreState(savedInstanceState);
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    class PageLoader extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbArticle.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            Document document = null;
            String result = "";
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Document> documentFuture = executorService.submit(
                    new HtmlParserPageThread(articleUrl));
            try {
                document = documentFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
            if (document != null) {
                Elements article = document.select(".article");
//                logMsg("article " + article.size());
                article.get(0).removeClass("fb-social-plugin fb-like fb_edge_widget_with_comment fb_iframe_widget");
                article.get(0).removeClass("nostyle");

//                logMsg("article: "+article.get(0));
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
