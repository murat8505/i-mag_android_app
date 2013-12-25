package by.imag.app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
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

import by.imag.app.classes.ArticlePreview;
import by.imag.app.classes.Constants;

// todo: CursorLoader
public class FragmentListArticles extends Fragment implements View.OnClickListener{
    private AppDb appDb;
    private ListView listView;
    private boolean isRegistered = false;
    private BroadcastReceiver broadcastReceiver;
    private ImageButton btnNext;
    private ImageButton btnPrev;
    private ProgressBar progressBar;
    private Intent intentArticle;

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
        btnPrev = (ImageButton) rootView.findViewById(R.id.btnPrev);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pbArticles);
        progressBar.setVisibility(View.GONE);
        intentArticle = new Intent(getActivity().getApplicationContext(), ArticleFragment.class);

        receiveData();
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
        unregister();
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                logMsg("last: "+lastInScreen);
            }
        });
    }

    private void receiveData() {
        logMsg("receive data");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isUpdated = false;
                isUpdated = intent.getBooleanExtra(Constants.INTENT_ARTICLES, false);
                if (isUpdated) {
                    if (isAdded()) {
                        setView();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
        isRegistered = true;
    }

    protected void unregister() {
        if (isRegistered) {
            getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
            isRegistered = false;
        }
    }

    private void logMsg(String msg) {
        Log.d(Constants.LOG_TAG, ((Object) this).getClass().getSimpleName() + ": " + msg);
    }

    @Override
    public void onClick(View view) {
        PageClicker pageClicker = new PageClicker();
        switch (view.getId()) {
            case R.id.btnNext:
                logMsg("click button Next");
//                ((MainActivity)getActivity()).serviceParseNext();
                pageClicker.execute(Constants.PAGE_NEXT);
            break;
            case R.id.btnPrev:
                logMsg("click button Prev");
                pageClicker.execute(Constants.PAGE_PREV);
            break;
        }
    }

    class PageClicker extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnNext.setEnabled(false);
            btnPrev.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int pageClick = integers[0];
            switch (pageClick) {
                case Constants.PAGE_NEXT:
                    ((MainActivity)getActivity()).serviceParseNext();
                break;
//                case Constants.PAGE_PREV:
//                    ((MainActivity)getActivity()).serviceParsePrev();
//                break;
                default: logMsg("error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            btnNext.setEnabled(true);
            btnPrev.setEnabled(true);
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
