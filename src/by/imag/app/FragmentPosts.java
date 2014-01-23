package by.imag.app;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class FragmentPosts extends BaseFragment implements View.OnClickListener{
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
    private String subtitle;
    private String url;
    private String name;
    private TagItem tagItem;
    private ArchiveItem archiveItem;


    public FragmentPosts(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public FragmentPosts(TagItem tagItem) {
        this.tagItem = tagItem;
        this.url = tagItem.getTagURL() + "&paged=";
        this.name = tagItem.getTagName();
        subtitle = tagItem.getTagName();
    }

    public FragmentPosts(ArchiveItem archiveItem) {
        this.archiveItem = archiveItem;
        this.url = archiveItem.getArchUrl() + "&paged=";
        this.name = archiveItem.getArchName();
//        subtitle = getResources().getStringArray(R.array.menu_items)[2];
        subtitle = archiveItem.getArchName();
    }

    public FragmentPosts() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logMsg("onCreateView");
        appDb = new AppDb(getActivity());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.fragment_posts, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        PostAdapter postAdapter = new PostAdapter(getActivity().getApplicationContext(), posts);
        gridView.setAdapter(postAdapter);
        gridView.setNumColumns(getNumColumns());
        gridScroll();
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbGridArt);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt(PAGE);
        }

        if (isOnline() && update) {
            if (url == null) {
                url = Constants.PAGE;
                String parseUrl = url + currentPage;
                subtitle = getResources().getStringArray(R.array.menu_items)[0];
                new PostsLoader().execute(parseUrl);
            } else {
                new PostsLoader().execute(url + currentPage);
            }
        } else {
            logMsg("device offline");
        }
        setView();
        update = true;
//        logMsg("isAdded: "+isAdded());
//        logMsg("isDetached: " + isDetached());
//        logMsg("isHidden: "+isHidden());
//        logMsg("isVisible :" + isVisible());
        return rootView;
    }

    @SuppressWarnings("deprecation")
    private int getNumColumns() {
        float gridSize = getResources().getDimension(R.dimen.grid_size);
//        logMsg("gridSize = "+gridSize);
//        float scaleFactor = getResources().getDisplayMetrics().density * gridSize;
        int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / gridSize);
        return columns;
    }

    private void gridScroll() {
//        logMsg("gridScroll");
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
//        String[] strings = getResources().getStringArray(R.array.menu_items);
//        subtitle = strings[0];
//        getActivity().getActionBar().setSubtitle(subtitle);
//        logMsg("onResume");
//        loadPreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
//        logMsg("onPause");
        update = false;
//        logMsg("update: "+update);
//        savePreferences();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
//        logMsg("onCreate");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        logMsg("onSaveInstanceState");
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
//                logMsg("post: "+articleId);
                Intent viewPostIntent = new Intent(getActivity(), ActivityArticle.class);
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
//        if (name != null && url != null) {
//            getActivity().getActionBar().setSubtitle(name);
//        }
//        logMsg("subtitle: "+subtitle);
        if (subtitle != null) {
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setSubtitle(subtitle);
        } else {
            subtitle = getResources().getStringArray(R.array.menu_items)[0];
            getActivity().getActionBar().setSubtitle(subtitle);
        }
    }

//    private boolean isOnline() {
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            // networkInfo.isConnected
//            // networkInfo.isConnectedOrConnecting()
//            return true;
//        }
//        return false;
//    }
//
//    private void logMsg(String msg) {
//        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
//    }

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
//                logMsg("current: "+currentPage + " last: "+lastPage);
            }
            return articlePreviewList;
        }

        @Override
        protected void onPostExecute(List<ArticlePreview> articlePreviewList) {
            super.onPostExecute(articlePreviewList);
            loadingMore = false;
            if (comparePosts(articlePreviewList)) {
                posts.addAll(articlePreviewList);
                if (isAdded()) {
                    setView();
                }
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
