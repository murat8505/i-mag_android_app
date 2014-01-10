package by.imag.app;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import by.imag.app.classes.ArchiveItem;
import by.imag.app.classes.ArticlePreview;
import by.imag.app.classes.Constants;
import by.imag.app.classes.DocumentParser;
import by.imag.app.classes.TagItem;

public class PostsFragment extends Fragment implements View.OnClickListener{
    private AppDb appDb;
    private SharedPreferences preferences;
    private GridView gridView;
    private int currentPage = 1;
    private int lastPage;
    private ProgressBar progressBar;
    private  List<ArticlePreview> posts = new ArrayList<ArticlePreview>();
    private final String PAGE = "page";
    private boolean loadingMore = false;
    private boolean update = true;
    private String url;
    private String name;
    private TagItem tagItem;
    private ArchiveItem archiveItem;


    public PostsFragment(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public PostsFragment(TagItem tagItem) {
        this.tagItem = tagItem;
        this.url = tagItem.getTagURL() + "&paged=";
        this.name = tagItem.getTagName();
    }

    public PostsFragment(ArchiveItem archiveItem) {
        this.archiveItem = archiveItem;
        this.url = archiveItem.getArchUrl() + "&paged=";
        this.name = archiveItem.getArchName();
    }

    public PostsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logMsg("onCreateView");
        appDb = new AppDb(getActivity());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.test_frag_grid, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        PostAdapter postAdapter = new PostAdapter(getActivity().getApplicationContext(), posts);
        gridView.setAdapter(postAdapter);
//        gridView.setColumnWidth((int) getResources().getDimension(R.dimen.fragment_grid_size));
        gridScroll();
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbGridArt);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE);
//            update = savedInstanceState.getBoolean(UPDATE);
//            posts = (List<ArticlePreview>) savedInstanceState.getParcelable("posts");
//            setView();
        }
        setView();
//        loadPreferences();
        if (isOnline() && update) {
//            new AsyncPostsLoader().execute(currentPage);
//            if ((archiveItem == null) || (tagItem == null)) {
//                url = Constants.PAGE;
//                logMsg("url: "+url);
//                new PostsLoader().execute(url + currentPage);
//            } else {
//                new PostsLoader().execute(url + currentPage);
//            }
            if (url == null) {
                url = Constants.PAGE + currentPage;
                logMsg("url: "+url);
                new PostsLoader().execute(url);
            } else {
                logMsg("url: "+url);
                new PostsLoader().execute(url + currentPage);
            }

        } else {
            logMsg("device offline");
        }
        update = true;
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
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && (currentPage < lastPage) && (!loadingMore)) {
//                    new AsyncPostsLoader().execute(currentPage + 1);
                    new PostsLoader().execute(url + (currentPage + 1));
                }
            }
        });
    }

//    private void savePreferences() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean(Constants.UPDATE_POSTS, false);
//        editor.commit();
//    }
//
//    private void loadPreferences() {
//        update = preferences.getBoolean(Constants.UPDATE_POSTS, true);
//    }

    @Override
    public void onResume() {
        super.onResume();
        logMsg("onResume");
//        loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        logMsg("onPause");
        update = false;
        logMsg("update: "+update);
//        savePreferences();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        logMsg("onCreate");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logMsg("onSaveInstanceState");
        outState.putInt(PAGE, currentPage);
//        outState.putParcelableArrayList("posts", (ArrayList<? extends Parcelable>) posts);
//        outState.putBoolean(UPDATE, false);
//        outState.putParcelableArrayList("posts", (ArrayList<? extends Parcelable>) posts);
//        int index = gridView.getFirstVisiblePosition();
//        outState.putInt("index", index);
//        int verticalSpacing = gridView.getVerticalSpacing();
//        int offset = (int) (verticalSpacing * getResources().getDisplayMetrics().density);
//        outState.putInt("offset", offset);
    }


    @Override
    public void onClick(View v) {
        //
    }

    private void onGridItemClick() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticlePreview ap = posts.get(position);
                int articleId = ap.getArticleId();
                String postTitle = ap.getArticleTitle();
                logMsg("post: "+articleId);
                Intent viewPostIntent = new Intent(getActivity(), ArticleActivity.class);
                Bundle article = new Bundle();
                article.putInt("articleId", articleId);
                article.putString("postTitle", postTitle);
                article.putString("postText", ap.getPreviewText());
                viewPostIntent.putExtra(Constants.INTENT_POST, article);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.addToBackStack(null);
                startActivity(viewPostIntent);
            }
        });
    }

    private void setView() {
//        PostAdapter postAdapter = new PostAdapter(getActivity().getApplicationContext(), posts);
//        gridView.setAdapter(postAdapter);
        gridView.deferNotifyDataSetChanged();
        onGridItemClick();
        if (name != null && url != null) {
            getActivity().getActionBar().setSubtitle(name);
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

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    private class PostsLoader extends AsyncTask<String, Void, List<ArticlePreview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            loadingMore = true;
        }

        @Override
        protected List<ArticlePreview> doInBackground(String... strings) {
            List<ArticlePreview> articlePreviewList = null;
            String url = strings[0];
            if (isOnline()) {
                DocumentParser documentParser = new DocumentParser(url);
                articlePreviewList = documentParser.getArticlePreviewList();
                int[] pagesNumbers = documentParser.getPages();
                currentPage = pagesNumbers[0];
                lastPage = pagesNumbers[1];
                logMsg("current: "+currentPage + " last: "+lastPage);
            }
            return articlePreviewList;
        }

        @Override
        protected void onPostExecute(List<ArticlePreview> articlePreviewList) {
            super.onPostExecute(articlePreviewList);
            loadingMore = false;
            if (comparePosts(articlePreviewList)) {
                posts.addAll(articlePreviewList);
                setView();
            }
            progressBar.setVisibility(View.GONE);
        }

        private boolean comparePosts(List<ArticlePreview> articlePreviewList) {
            boolean addPosts = true;
            int postsSize = posts.size();
            int articlesSize = articlePreviewList.size();
            if (postsSize > 0 && articlesSize > 0) {
                int lastPostId = posts.get(postsSize - 1).getArticleId();
                int articlesLastId = articlePreviewList.get(articlesSize - 1).getArticleId();
                if (lastPostId == articlesLastId) {
                    addPosts = false;
                } else {
                    addPosts = true;
                }
            }
            return addPosts;
        }
    }

    class AsyncPostsLoader extends AsyncTask<Integer, Void, List<ArticlePreview>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            loadingMore = true;
        }

        @Override
        protected List<ArticlePreview> doInBackground(Integer... pages) {
            List<ArticlePreview> articlePreviewList = null;
            int pageNumber = pages[0];
            if (isOnline()) {
                if (url != null) {
                    DocumentParser documentParser = new DocumentParser(url);
                    articlePreviewList = documentParser.getArticlePreviewList();
                } else {
                    DocumentParser documentParser = new DocumentParser(pageNumber);
                    articlePreviewList = documentParser.getArticlePreviewList();
                    int[] pagesNumbers = documentParser.getPages();
                    currentPage = pagesNumbers[0];
                    lastPage = pagesNumbers[1];
                }

            }
            return articlePreviewList;
        }

        @Override
        protected void onPostExecute(List<ArticlePreview> articlePreviewList) {
            super.onPostExecute(articlePreviewList);
//            logMsg("articles: " + articlePreviewList);
            // get id from posts
            // get id from articlePreviewList
            loadingMore = false;
            if (comparePosts(articlePreviewList)) {
                posts.addAll(articlePreviewList);
                setView();
            }
            progressBar.setVisibility(View.GONE);
        }

        private boolean comparePosts(List<ArticlePreview> articlePreviewList) {
            boolean addPosts = true;
            int postsSize = posts.size();
            int articlesSize = articlePreviewList.size();
            if (postsSize > 0 && articlesSize > 0) {
                int lastPostId = posts.get(postsSize - 1).getArticleId();
                int articlesLastId = articlePreviewList.get(articlesSize - 1).getArticleId();
                if (lastPostId == articlesLastId) {
                    addPosts = false;
                } else {
                    addPosts = true;
                }
            }
            return addPosts;
        }
    }
}