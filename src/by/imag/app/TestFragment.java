package by.imag.app;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private List<ArticlePreview> posts = new ArrayList<ArticlePreview>();
    private static final String GRID_STATE = "gridState";
    private static final String IS_UPDATED = "isUpdated";
    private static final String PAGE = "page";
    private boolean loadingMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logMsg("onCreateView");
        appDb = new AppDb(getActivity());
        View rootView = inflater.inflate(R.layout.test_frag_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setColumnWidth((int) getResources().getDimension(R.dimen.fragment_grid_size));
        gridScroll();
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbGridArt);
        btnNext = (ImageButton) rootView.findViewById(R.id.btnNext);
        btnPrev = (ImageButton) rootView.findViewById(R.id.btnPrev);
        btnRefresh = (ImageButton) rootView.findViewById(R.id.btnRefresh);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE);
//            int index = savedInstanceState.getInt("index");
//            gridView.setSelection(index);
//            int offset = savedInstanceState.getInt("offset");
//            final View first = container.getChildAt(0);
//            if (first != null) {
//                offset -= first.getTop();
//            }
//            setView();
//            gridView.scrollBy(0, offset);
//            logMsg("offset: "+offset);
        } else {
            setView();
        }
        new ArticleSAsync().execute(currentPage);
        return rootView;
    }

    private void gridScroll() {
        logMsg("gridScroll");
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//                logMsg("first visible: "+firstVisibleItem);
//                logMsg("visible items: "+visibleItemCount);
//                logMsg("total items: "+totalItemCount);
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && (currentPage < lastPage) && (!loadingMore)) {
                    new ArticleSAsync().execute(currentPage + 1);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        logMsg("onResume");
//        setView();
    }

    @Override
    public void onPause() {
        super.onPause();
        logMsg("onPause");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
//        int index = gridView.getFirstVisiblePosition();
//        outState.putInt("index", index);
//        int verticalSpacing = gridView.getVerticalSpacing();
//        int offset = (int) (verticalSpacing * getResources().getDisplayMetrics().density);
//        outState.putInt("offset", offset);
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
                break;
            case R.id.btnNext :
//                loadMore();
                if (currentPage < lastPage) {
                    new ArticleSAsync().execute(currentPage + 1);
                }
                break;
        }
    }

//    private void loadMore() {
//        if (currentPage != lastPage) {
//            int index = gridView.getFirstVisiblePosition();
//            new ArticleSAsync().execute(currentPage + 1);
////            setView();
//            int artOnPage = Constants.ARTICLES_ON_PAGE;
//            int limit = currentPage * artOnPage;
//            int offset = currentPage * artOnPage - (artOnPage);
//            Cursor cursor = appDb.getArticlesCursor(limit, offset);
//            ArticleCursorAdapter cursorAdapter = new ArticleCursorAdapter(
//                    getActivity(), cursor, true);
//            gridView.setAdapter(cursorAdapter);
//            gridView.setSelection(index);
//        }
//    }

    private void setView() {
//        Cursor cursor = appDb.getArticlesCursor(currentPage);
//        ArticleCursorAdapter cursorAdapter = new ArticleCursorAdapter(
//                getActivity(), cursor, true);
//        gridView.setAdapter(cursorAdapter);
        PostAdapter postAdapter = new PostAdapter(getActivity().getApplicationContext(), posts);
        gridView.setAdapter(postAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
            }
        });

//        logMsg("current page: "+currentPage);
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

    class ArticleSAsync extends AsyncTask<Integer, Void, List<ArticlePreview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
            btnRefresh.setEnabled(false);
            loadingMore = true;
        }

        @Override
        protected List<ArticlePreview> doInBackground(Integer... pages) {
            List<ArticlePreview> articlePreviewList = null;
            int pageNumber = pages[0];
            if (isOnline()) {
                DocumentParser documentParser = new DocumentParser(pageNumber);
                articlePreviewList = documentParser.getArticlePreviewList();
                int[] pagesNumbers = documentParser.getPages();
//                currentPage = documentParser.getCurrentPage();
                currentPage = pagesNumbers[0];
//                lastPage = documentParser.getLastPage();
                lastPage = pagesNumbers[1];
//                isArticlesUpdated = appDb.writeArticlesTable(articlePreviewList);
            }
            return articlePreviewList;
        }

        @Override
        protected void onPostExecute(List<ArticlePreview> articlePreviewList) {
            super.onPostExecute(articlePreviewList);
            posts.addAll(articlePreviewList);
            gridView.deferNotifyDataSetChanged();
            loadingMore = false;
            progressBar.setVisibility(View.GONE);
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
            btnRefresh.setEnabled(true);
        }
    }

//    class ArticleSAsync extends AsyncTask<Integer, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBar.setVisibility(View.VISIBLE);
//            btnNext.setEnabled(false);
//            btnPrev.setEnabled(false);
//            btnRefresh.setEnabled(false);
//        }
//
//        @Override
//        protected Boolean doInBackground(Integer... pages) {
//            boolean isArticlesUpdated = false;
//            int pageNumber = pages[0];
//            if (isOnline()) {
//                DocumentParser documentParser = new DocumentParser(pageNumber);
//                List<ArticlePreview> articlePreviewList =
//                        documentParser.getArticlePreviewList();
//                int[] pagesNumbers = documentParser.getPages();
////                currentPage = documentParser.getCurrentPage();
//                currentPage = pagesNumbers[0];
////                lastPage = documentParser.getLastPage();
//                lastPage = pagesNumbers[1];
//                isArticlesUpdated = appDb.writeArticlesTable(articlePreviewList);
//            }
//            return isArticlesUpdated;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean isArticlesUpdated) {
//            super.onPostExecute(isArticlesUpdated);
//            if (isArticlesUpdated) {
//                setView();
//            }
//            progressBar.setVisibility(View.GONE);
//            btnNext.setEnabled(true);
//            btnPrev.setEnabled(true);
//            btnRefresh.setEnabled(true);
//        }
//    }

    class ArticleCursorAdapter extends CursorAdapter {

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
