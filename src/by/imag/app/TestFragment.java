package by.imag.app;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import by.imag.app.classes.ArticlePreview;
import by.imag.app.classes.Constants;
import by.imag.app.classes.DocumentParser;

public class TestFragment extends Fragment implements View.OnClickListener{
    private AppDb appDb;
    private GridView gridView;
    private int currentPage = 1;
    private int lastPage;
    private ProgressBar progressBar;
    private ImageButton btnNext;
    private ImageButton btnPrev;
    private ImageButton btnRefresh;
    private static final String GRID_STATE = "gridState";
    private static final String IS_UPDATED = "isUpdated";
    private static final String PAGE = "page";
    private static int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logMsg("onCreateView");
        appDb = new AppDb(getActivity());
        View rootView = inflater.inflate(R.layout.test_frag_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbGridArt);
        btnNext = (ImageButton) rootView.findViewById(R.id.btnNext);
        btnPrev = (ImageButton) rootView.findViewById(R.id.btnPrev);
        btnRefresh = (ImageButton) rootView.findViewById(R.id.btnRefresh);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
 //        new ArticleSAsync().execute(currentPage);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE);
        }
        setView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        logMsg("onResume");
        setView();
    }

    @Override
    public void onPause() {
        super.onPause();
        logMsg("onPause");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMsg("onCreate");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logMsg("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        logMsg("onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logMsg("onSaveInstanceState");
        outState.putInt(PAGE, currentPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRefresh :
                new ArticleSAsync().execute(currentPage);
//                setView();
                break;
            case R.id.btnPrev :
                if (currentPage > 1) {
                    new ArticleSAsync().execute(currentPage - 1);
                }
//                setView();
                break;
            case R.id.btnNext :
                if (currentPage != lastPage) {
                    new ArticleSAsync().execute(currentPage + 1);
                }
//                setView();
                break;
        }
    }

    private void setView() {
        Cursor cursor = appDb.getArticlesCursor(currentPage);
        ArticleCursorAdapter cursorAdapter = new ArticleCursorAdapter(
                getActivity(), cursor, true);
        gridView.setAdapter(cursorAdapter);
        logMsg("current page: "+currentPage);
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

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }



    class ArticleSAsync extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
            btnRefresh.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Integer... pages) {
            boolean isArticlesUpdated = false;
            int pageNumber = pages[0];
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
            if (isArticlesUpdated) {
                setView();
            }
            progressBar.setVisibility(View.GONE);
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
            btnRefresh.setEnabled(true);
        }
    }

    class ArticleCursorAdapter extends android.support.v4.widget.CursorAdapter {

        public ArticleCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
            super(context, cursor, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.art_grid_item, viewGroup, false);
            bindView(view, context, cursor);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ImageView imgArticlePreview = (ImageView) view.findViewById(R.id.imageView);
            TextView tvArticleTitle = (TextView) view.findViewById(R.id.tvTitle);

            String imgUrl = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_IMAGE_URL));
            String articleTitle = cursor.getString(cursor.getColumnIndex(AppDb.ARTICLE_TITLE));


            Picasso.with(getActivity().getApplicationContext()).load(imgUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.logo_red)
                    .into(imgArticlePreview);
            tvArticleTitle.setText(articleTitle);
        }
    }
}
