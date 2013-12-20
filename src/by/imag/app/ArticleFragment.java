package by.imag.app;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import by.imag.app.classes.Constants;


public class ArticleFragment extends Fragment {
    private WebView webViewArticle;
    private String articleUrl;

    public ArticleFragment(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article, container, false);
        webViewArticle = (WebView) rootView.findViewById(R.id.wvArticle);
        logMsg("url: "+articleUrl);
//        webViewArticle.loadUrl(articleUrl);
        return rootView;
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, getClass().getSimpleName() + ": " + msg);
    }
}
