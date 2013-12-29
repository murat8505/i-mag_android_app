package by.imag.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import by.imag.app.classes.ArticlePreview;
import by.imag.app.classes.Constants;
import by.imag.app.classes.DocumentParser;
import by.imag.app.classes.HtmlParserThread;

// todo: CursorLoader
public class FragmentListArticles extends Fragment implements View.OnClickListener{
    private AppDb appDb;
    private ListView listView;
    private boolean isRegistered = false;
    private BroadcastReceiver broadcastReceiver;
    private ImageButton btnNext;
//    private ImageButton btnPrev;
    private ProgressBar progressBar;
    private Intent intentArticle;
    private int currentPage = 1;
    private int lastPage;
    private ArticlesLoader articlesLoader;
    private SharedPreferences preferences;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logMsg("onActivityCreated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logMsg("onCreateView");
//        return super.onCreateView(inflater, container, savedInstanceState);
        appDb = new AppDb(getActivity());
        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.lvTestArt);
        btnNext = (ImageButton) rootView.findViewById(R.id.btnNext);
//        btnPrev = (ImageButton) rootView.findViewById(R.id.btnPrev);
        btnNext.setOnClickListener(this);
//        btnPrev.setOnClickListener(this);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbArticles);
        progressBar.setVisibility(View.GONE);
        intentArticle = new Intent(getActivity().getApplicationContext(), ArticleFragment.class);
//        receiveData();
        articlesLoader = new ArticlesLoader();
        articlesLoader.execute(currentPage);
        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregister();
    }

    private void setView() {
        Cursor cursor = appDb.getArticlesCursor();
        ArticleCursorAdapter cursorAdapter = new ArticleCursorAdapter(
                getActivity(), cursor, true);
        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,
                                    long id) {
//                logMsg("item click: " + position + ", id: " + id);
                ArticlePreview articlePreview = appDb.getArticlePreview(id);
                String articleUrl = articlePreview.getArticleURL();
                logMsg("url: " + articleUrl);
                intentArticle.putExtra(Constants.INTENT_ARTICLES, articleUrl);
                Fragment fragmentArticle = new ArticleFragment(articleUrl);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.content_frame, fragmentArticle);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


    }



    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    @Override
    public void onClick(View view) {
//        PageClicker pageClicker = new PageClicker();
        switch (view.getId()) {
            case R.id.btnNext:
                logMsg("click button Next");

                if (currentPage != lastPage) {
                    logMsg("item id: " + listView.getId());
                    new ArticlesLoader().execute(currentPage +1);
                    listView.smoothScrollToPosition(20);
                }
            break;

        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // networkInfo.isConnected
            // networkInfo.isConnectedOrConnecting()
            return true;
        }
        return false;
    }


    class ArticlesLoader extends AsyncTask<Integer, Void, Boolean> {
        boolean isArticlesUpdated = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
//            btnPrev.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int pageNumber = integers[0];
            if (isOnline()) {
                DocumentParser documentParser = new DocumentParser(pageNumber);
                List<ArticlePreview> articlePreviewList =
                        documentParser.getArticlePreviewList();
                currentPage = documentParser.getCurrentPage();
                lastPage = documentParser.getLastPage();
                isArticlesUpdated = appDb.writeArticlesTable(articlePreviewList);
            }
            return isArticlesUpdated;
        }

        @Override
        protected void onPostExecute(Boolean isArticlesUpdated) {
            super.onPostExecute(isArticlesUpdated);
            progressBar.setVisibility(View.GONE);
            btnNext.setEnabled(true);
            if (isArticlesUpdated) {
                setView();
            }
        }
    }

    class ArticleCursorAdapter extends CursorAdapter {

        public ArticleCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.article_list_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ImageView imgArticlePreview = (ImageView) view.findViewById(R.id.imgArticlePreview);
            TextView tvArticleTitle = (TextView) view.findViewById(R.id.tvArticleTitle);
            TextView tvArticleText = (TextView) view.findViewById(R.id.tvArticleText);

            String imgUrl = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_IMAGE_URL));
            String articleTitle = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TITLE));
            String articleText = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TEXT));


            Picasso.with(getActivity().getApplicationContext()).load(imgUrl)
                    .error(R.drawable.ic_launcher)
                    .into(imgArticlePreview);
//            Picasso.with(getActivity().getApplicationContext()).setDebugging(true);
            tvArticleTitle.setText(articleTitle);
            tvArticleText.setText(articleText);
        }
    }
}
